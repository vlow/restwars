package restwars.model.ship;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.UUID;

public class ShipInConstruction {
    private final UUID id;

    private final ShipType type;

    private final UUID planetId;

    private final UUID playerId;

    private final long started;

    private final long done;

    public ShipInConstruction(UUID id, ShipType type, UUID planetId, UUID playerId, long started, long done) {
        Preconditions.checkArgument(started > 0, "started must be > 0");
        Preconditions.checkArgument(done > 0, "done must be > 0");

        this.id = Preconditions.checkNotNull(id, "id");
        this.type = Preconditions.checkNotNull(type, "type");
        this.planetId = Preconditions.checkNotNull(planetId, "planetId");
        this.playerId = Preconditions.checkNotNull(playerId, "playerId");
        this.started = started;
        this.done = done;
    }

    public UUID getId() {
        return id;
    }

    public ShipType getType() {
        return type;
    }

    public UUID getPlanetId() {
        return planetId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public long getStarted() {
        return started;
    }

    public long getDone() {
        return done;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .add("planetId", planetId)
                .add("playerId", playerId)
                .add("started", started)
                .add("done", done)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipInConstruction that = (ShipInConstruction) o;

        return Objects.equal(this.id, that.id) &&
                Objects.equal(this.type, that.type) &&
                Objects.equal(this.planetId, that.planetId) &&
                Objects.equal(this.playerId, that.playerId) &&
                Objects.equal(this.started, that.started) &&
                Objects.equal(this.done, that.done);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, planetId, playerId, started, done);
    }
}
