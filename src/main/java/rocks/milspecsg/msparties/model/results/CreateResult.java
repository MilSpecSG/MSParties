package rocks.milspecsg.msparties.model.results;

import java.util.Optional;

public class CreateResult<T> extends Result {

    private T value;

    public CreateResult(T value, boolean success, String errorMessage) {
        this.value = value;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public CreateResult(String errorMessage) {
        this.errorMessage = errorMessage;
        this.success = false;
        this.value = null;
    }

    // IntelliJ says I shouldn't use an Optional as a type parameter,
    // but this whole class is basically an optional with an error message
    public CreateResult(Optional<? extends T> value, String errorMessage) {
        this.success = value.isPresent();
        this.value = value.orElse(null);
        this.errorMessage = errorMessage;
    }

    public CreateResult(T value) {
        this.value = value;
        success = true;
        errorMessage = "";
    }

    public Optional<T> getValue() {
        return success ? Optional.ofNullable(value) : Optional.empty();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
