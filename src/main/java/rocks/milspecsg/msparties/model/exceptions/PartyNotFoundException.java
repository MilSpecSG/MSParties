package rocks.milspecsg.msparties.model.exceptions;

public class PartyNotFoundException extends Exception {

    public PartyNotFoundException(String message) {
        super("Party not found");
    }
}
