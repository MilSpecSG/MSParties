package rocks.milspecsg.msparties.model.exceptions;

public class IllegalNameException extends Exception {

    public IllegalNameException(String message) {
        super("Illegal name");
    }
}
