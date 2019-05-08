package rocks.milspecsg.msparties.model.exceptions;

public class PartyNotFoundException extends Exception {

    public PartyNotFoundException() {
        super("Party not found");
    }
}
