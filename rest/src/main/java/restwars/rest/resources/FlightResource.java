package restwars.rest.resources;

import com.google.common.base.Preconditions;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.Authorization;
import io.dropwizard.auth.Auth;
import restwars.model.flight.DetectedFlightWithSender;
import restwars.model.flight.Flight;
import restwars.model.player.Player;
import restwars.rest.mapper.FlightMapper;
import restwars.restapi.dto.ship.DetectedFlightResponse;
import restwars.restapi.dto.ship.FlightResponse;
import restwars.service.flight.FlightService;
import restwars.service.telescope.TelescopeService;
import restwars.util.Functional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Resource for flights.
 */
@Path("/v1/flight")
@Api(value = "/v1/flight", description = "Flights", authorizations = {
        @Authorization("basicAuth")
})
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FlightResource {
    private final FlightService flightService;
    private final TelescopeService telescopeService;

    @Inject
    public FlightResource(TelescopeService telescopeService, FlightService flightService) {
        this.flightService = Preconditions.checkNotNull(flightService, "flightService");
        this.telescopeService = Preconditions.checkNotNull(telescopeService, "telescopeService");
    }

    /**
     * Lists all flights from the player.
     *
     * @param player Player.
     * @return All flights from the player.
     */
    @GET
    @Path("/own")
    @ApiOperation("Lists all own flights")
    public List<FlightResponse> ownFlights(@Auth @ApiParam(access = "internal") Player player) {
        Preconditions.checkNotNull(player, "player");

        List<Flight> flights = flightService.findFlightsForPlayer(player);

        return Functional.mapToList(flights, FlightMapper::fromFlight);
    }

    /**
     * Lists all incoming flight on a planet from the player.
     *
     * @param player Player.
     * @return All incoming flights.
     */
    @GET
    @Path("/incoming")
    @ApiOperation("Lists all incoming flights")
    public List<DetectedFlightResponse> incomingFlights(@Auth @ApiParam(access = "internal") Player player) {
        Preconditions.checkNotNull(player, "player");

        List<DetectedFlightWithSender> incomingFlights = telescopeService.findDetectedFlights(player);

        return Functional.mapToList(incomingFlights, FlightMapper::fromDetectedFlight);
    }
}
