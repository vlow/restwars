package restwars.storage.ship;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restwars.service.planet.Location;
import restwars.service.ship.*;
import restwars.service.unitofwork.UnitOfWorkService;
import restwars.storage.jooq.AbstractJooqDAO;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static restwars.storage.jooq.Tables.FLIGHT;
import static restwars.storage.jooq.Tables.FLIGHT_SHIPS;

public class JooqFlightDAO extends AbstractJooqDAO implements FlightDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(JooqFlightDAO.class);

    @Inject
    public JooqFlightDAO(UnitOfWorkService unitOfWorkService) {
        super(unitOfWorkService);
    }

    @Override
    public void insert(Flight flight) {
        Preconditions.checkNotNull(flight, "flight");

        LOGGER.debug("Inserting flight {}", flight);

        // TODO: Remove the casts!
        context()
                .insertInto(
                        FLIGHT, FLIGHT.ID, FLIGHT.PLAYER_ID, FLIGHT.START_GALAXY, FLIGHT.START_SOLAR_SYSTEM, FLIGHT.START_PLANET,
                        FLIGHT.DESTINATION_GALAXY, FLIGHT.DESTINATION_SOLAR_SYSTEM, FLIGHT.DESTINATION_PLANET, FLIGHT.STARTED_IN_ROUND,
                        FLIGHT.ARRIVAL_IN_ROUND, FLIGHT.ENERGY_NEEDED, FLIGHT.TYPE, FLIGHT.DIRECTION
                )
                .values(
                        flight.getId(), flight.getPlayerId(), flight.getStart().getGalaxy(), flight.getStart().getSolarSystem(), flight.getStart().getPlanet(),
                        flight.getDestination().getGalaxy(), flight.getDestination().getSolarSystem(), flight.getDestination().getPlanet(),
                        (int) flight.getStartedInRound(), (int) flight.getArrivalInRound(), (int) flight.getEnergyNeeded(), flight.getType().getId(), flight.getDirection().getId()
                )
                .execute();

        insertShips(flight);
    }

    @Override
    public List<Flight> findWithPlayerId(UUID playerId) {
        Preconditions.checkNotNull(playerId, "playerId");

        LOGGER.debug("Finding flights with player id {}", playerId);

        Result<Record> result = context().select().from(FLIGHT).join(FLIGHT_SHIPS).on(FLIGHT_SHIPS.FLIGHT_ID.eq(FLIGHT.ID)).where(FLIGHT.PLAYER_ID.eq(playerId)).fetch();
        return readFlights(result);
    }

    @Override
    public List<Flight> findWithArrival(long arrival) {
        LOGGER.debug("Finding flights with arrival {}", arrival);

        // TODO: Remove the cast
        Result<Record> result = context().select().from(FLIGHT).join(FLIGHT_SHIPS).on(FLIGHT_SHIPS.FLIGHT_ID.eq(FLIGHT.ID)).where(FLIGHT.ARRIVAL_IN_ROUND.eq((int) arrival)).fetch();
        return readFlights(result);
    }

    @Override
    public void update(Flight flight) {
        Preconditions.checkNotNull(flight, "flight");

        LOGGER.debug("Updating flight {}", flight);

        // TODO: Remove the casts
        context()
                .update(FLIGHT)
                .set(FLIGHT.PLAYER_ID, flight.getPlayerId())
                .set(FLIGHT.START_GALAXY, flight.getStart().getGalaxy())
                .set(FLIGHT.START_SOLAR_SYSTEM, flight.getStart().getSolarSystem())
                .set(FLIGHT.START_PLANET, flight.getStart().getPlanet())
                .set(FLIGHT.DESTINATION_GALAXY, flight.getDestination().getGalaxy())
                .set(FLIGHT.DESTINATION_SOLAR_SYSTEM, flight.getDestination().getSolarSystem())
                .set(FLIGHT.DESTINATION_PLANET, flight.getDestination().getPlanet())
                .set(FLIGHT.STARTED_IN_ROUND, (int) flight.getStartedInRound())
                .set(FLIGHT.ARRIVAL_IN_ROUND, (int) flight.getArrivalInRound())
                .set(FLIGHT.ENERGY_NEEDED, (int) flight.getEnergyNeeded())
                .set(FLIGHT.TYPE, flight.getType().getId())
                .set(FLIGHT.DIRECTION, flight.getDirection().getId())
                .execute();

        deleteShips(flight);
        insertShips(flight);
    }

    @Override
    public void delete(Flight flight) {
        Preconditions.checkNotNull(flight, "flight");

        LOGGER.debug("Deleting flight {}", flight);

        context()
                .delete(FLIGHT)
                .where(FLIGHT.ID.eq(flight.getId()))
                .execute();

        deleteShips(flight);
    }

    @Override
    public List<Flight> findWithStart(Location location) {
        LOGGER.debug("Finding flights with start {}", location);

        Result<Record> result = context().select().from(FLIGHT).join(FLIGHT_SHIPS).on(FLIGHT_SHIPS.FLIGHT_ID.eq(FLIGHT.ID))
                .where(FLIGHT.START_GALAXY.eq(location.getGalaxy()))
                .and(FLIGHT.START_SOLAR_SYSTEM.eq(location.getSolarSystem()))
                .and(FLIGHT.START_PLANET.eq(location.getPlanet()))
                .fetch();
        return readFlights(result);
    }

    private List<Flight> readFlights(Result<Record> result) {
        Map<UUID, Ships> flightShips = Maps.newHashMap();
        Map<UUID, Flight> flights = Maps.newHashMap();

        for (Record record : result) {
            UUID id = record.getValue(FLIGHT.ID);
            // If flight is not already known, create add it to map
            if (!flights.containsKey(id)) {
                Location start = new Location(record.getValue(FLIGHT.START_GALAXY), record.getValue(FLIGHT.START_SOLAR_SYSTEM), record.getValue(FLIGHT.START_PLANET));
                Location destination = new Location(record.getValue(FLIGHT.DESTINATION_GALAXY), record.getValue(FLIGHT.DESTINATION_SOLAR_SYSTEM), record.getValue(FLIGHT.DESTINATION_PLANET));
                long startedInRound = record.getValue(FLIGHT.STARTED_IN_ROUND);
                long arrivalInRound = record.getValue(FLIGHT.ARRIVAL_IN_ROUND);
                long energyNeeded = record.getValue(FLIGHT.ENERGY_NEEDED);
                FlightType type = FlightType.fromId(record.getValue(FLIGHT.TYPE));
                FlightDirection direction = FlightDirection.fromId(record.getValue(FLIGHT.DIRECTION));
                UUID recordPlayerId = record.getValue(FLIGHT.PLAYER_ID);

                Flight flight = new Flight(id, start, destination, startedInRound, arrivalInRound, new Ships(), energyNeeded, type, recordPlayerId, direction);
                flights.put(id, flight);

                flightShips.put(id, new Ships());
            }

            // Add the ships to the flight
            Ships ships = flightShips.get(id);
            assert ships != null;

            int amount = record.getValue(FLIGHT_SHIPS.AMOUNT);
            ShipType shipType = ShipType.fromId(record.getValue(FLIGHT_SHIPS.TYPE));

            Ships updatedShips = ships.plus(shipType, amount);
            flightShips.put(id, updatedShips);
        }

        // Merge the flights with the ships map
        return flights.values().stream().map(f -> f.withShips(flightShips.get(f.getId()))).collect(Collectors.toList());
    }

    private void insertShips(Flight flight) {
        for (Ship ship : flight.getShips()) {
            context()
                    .insertInto(FLIGHT_SHIPS, FLIGHT_SHIPS.FLIGHT_ID, FLIGHT_SHIPS.TYPE, FLIGHT_SHIPS.AMOUNT)
                    .values(flight.getId(), ship.getType().getId(), ship.getAmount())
                    .execute();
        }
    }

    private void deleteShips(Flight flight) {
        context()
                .delete(FLIGHT_SHIPS)
                .where(FLIGHT_SHIPS.FLIGHT_ID.eq(flight.getId()))
                .execute();
    }
}
