package rocks.milspecsg.msparties.api.exception;

public class NotInPartyException extends Exception {

    public NotInPartyException(String message) {
        super("Not in party");
    }
}
