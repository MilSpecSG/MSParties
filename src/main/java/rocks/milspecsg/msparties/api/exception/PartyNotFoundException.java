package rocks.milspecsg.msparties.api.exception;

public class PartyNotFoundException extends Exception {

    public PartyNotFoundException(String message) {
        super("Party not found");
    }
}
