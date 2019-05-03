package rocks.milspecsg.msparties.model.exceptions;

public class TooManyPartiesException extends Exception {

    public TooManyPartiesException(String message) {
        super("Too many parties");
    }
}
