package rocks.milspecsg.msparties.api.exception;

public class IllegalNameException extends Exception {

    public IllegalNameException(String message) {
        super("Illegal name");
    }
}
