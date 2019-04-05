package rocks.milspecsg.msparties.api.exception;

public class BannedFromPartyException extends Exception{

    public BannedFromPartyException(String message) {
        super("Banned from party");
    }
}
