package rocks.milspecsg.msparties.model.exceptions;

public class CannotLeavePartyAsLeaderException extends Exception {

    public CannotLeavePartyAsLeaderException(String message) {
        super("Cannot leave party as leader");
    }
}
