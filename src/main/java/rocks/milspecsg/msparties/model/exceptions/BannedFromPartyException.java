package rocks.milspecsg.msparties.model.exceptions;

public class BannedFromPartyException extends Exception{

    public BannedFromPartyException(String message) {
        super("Banned from party");
    }
}
