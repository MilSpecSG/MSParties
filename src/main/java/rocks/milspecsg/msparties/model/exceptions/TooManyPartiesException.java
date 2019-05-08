package rocks.milspecsg.msparties.model.exceptions;

public class TooManyPartiesException extends Exception {

    public TooManyPartiesException() {
        super("Too many parties");
    }
}
