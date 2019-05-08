package rocks.milspecsg.msparties.model.exceptions;

public class CannotLeavePartyAsLeaderException extends Exception {

    public CannotLeavePartyAsLeaderException() {
        super("Cannot leave party as leader");
    }
}
