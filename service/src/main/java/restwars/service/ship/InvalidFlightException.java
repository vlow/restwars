package restwars.service.ship;

import restwars.service.ServiceException;

public class InvalidFlightException extends ServiceException {
    public enum Reason {
        NO_SHIPS,
        NO_COLONY_SHIP,
        NO_CARGO_ALLOWED,
        NOT_ENOUGH_CARGO_SPACE
    }

    private final Reason reason;

    public InvalidFlightException(Reason reason) {
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
