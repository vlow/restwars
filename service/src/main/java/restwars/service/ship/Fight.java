package restwars.service.ship;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.UUID;

public class Fight {
    private final UUID id;

    private final UUID attackerId;

    private final UUID defenderId;

    private final UUID planetId;

    private final Ships attackingShips;

    private final Ships defendingShips;

    private final Ships remainingAttackerShips;

    private final Ships remainingDefenderShips;

    private final long round;

    public Fight(UUID id, UUID attackerId, UUID defenderId, UUID planetId, Ships attackingShips, Ships defendingShips, Ships remainingAttackerShips, Ships remainingDefenderShips, long round) {
        this.round = round;
        this.id = Preconditions.checkNotNull(id, "id");
        this.attackerId = Preconditions.checkNotNull(attackerId, "attackerId");
        this.defenderId = Preconditions.checkNotNull(defenderId, "defenderId");
        this.planetId = Preconditions.checkNotNull(planetId, "planetId");
        this.attackingShips = Preconditions.checkNotNull(attackingShips, "attackingShips");
        this.defendingShips = Preconditions.checkNotNull(defendingShips, "defendingShips");
        this.remainingAttackerShips = Preconditions.checkNotNull(remainingAttackerShips, "remainingAttackerShips");
        this.remainingDefenderShips = Preconditions.checkNotNull(remainingDefenderShips, "remainingDefenderShips");
    }

    public UUID getId() {
        return id;
    }

    public UUID getAttackerId() {
        return attackerId;
    }

    public UUID getDefenderId() {
        return defenderId;
    }

    public UUID getPlanetId() {
        return planetId;
    }

    public Ships getAttackingShips() {
        return attackingShips;
    }

    public Ships getDefendingShips() {
        return defendingShips;
    }

    public Ships getRemainingAttackerShips() {
        return remainingAttackerShips;
    }

    public Ships getRemainingDefenderShips() {
        return remainingDefenderShips;
    }

    public long getRound() {
        return round;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("attackerId", attackerId)
                .add("defenderId", defenderId)
                .add("planetId", planetId)
                .add("attackingShips", attackingShips)
                .add("defendingShips", defendingShips)
                .add("remainingAttackerShips", remainingAttackerShips)
                .add("remainingDefenderShips", remainingDefenderShips)
                .add("round", round)
                .toString();
    }
}
