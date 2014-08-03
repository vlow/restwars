package restwars.service.ship;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class Hangar {
    private final UUID id;

    private final UUID planetId;

    private final UUID playerId;

    private final Map<ShipType, Long> ships;

    public Hangar(UUID id, UUID planetId, UUID playerId, Map<ShipType, Long> ships) {
        this.id = Preconditions.checkNotNull(id, "id");
        this.planetId = Preconditions.checkNotNull(planetId, "planetId");
        this.playerId = Preconditions.checkNotNull(playerId, "playerId");
        this.ships = Preconditions.checkNotNull(ships, "ships");
    }

    public UUID getId() {
        return id;
    }

    public UUID getPlanetId() {
        return planetId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Map<ShipType, Long> getShips() {
        return Collections.unmodifiableMap(ships);
    }

    public long getShipCount(ShipType type) {
        Preconditions.checkNotNull(type, "type");

        Long value = ships.get(type);
        return value == null ? 0L : value;
    }

    public Hangar withShips(Map<ShipType, Long> ships) {
        Preconditions.checkNotNull(ships, "ships");

        return new Hangar(id, planetId, playerId, ships);
    }
}
