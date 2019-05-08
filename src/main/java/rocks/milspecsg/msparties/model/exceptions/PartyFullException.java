package rocks.milspecsg.msparties.model.exceptions;

public class PartyFullException extends Exception{

    public PartyFullException() {
        super("Party full");
    }
}
