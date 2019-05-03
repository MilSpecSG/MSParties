package rocks.milspecsg.msparties.model.exceptions;

public class PartyFullException extends Exception{

    public PartyFullException(String message) {
        super("Party full");
    }
}
