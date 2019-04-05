package rocks.milspecsg.msparties.api.exception;

public class PartyFullException extends Exception{

    public PartyFullException(String message) {
        super("Party full");
    }
}
