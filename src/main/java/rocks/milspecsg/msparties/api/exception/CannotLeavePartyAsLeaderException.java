package rocks.milspecsg.msparties.api.exception;

public class CannotLeavePartyAsLeaderException extends Exception {

    public CannotLeavePartyAsLeaderException(String message) {
        super("Cannot leave party as leader");
    }
}
