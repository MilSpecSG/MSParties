package rocks.milspecsg.msparties.model.results;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class CreateResult<T> extends Result {

    private T value;

    public CreateResult(T value, boolean success, Text errorMessage) {
        this.value = value;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public CreateResult(Text errorMessage) {
        this.errorMessage = errorMessage;
        this.value = null;
    }

    public CreateResult(String errorMessage) {
        this(Text.of(TextColors.RED, errorMessage));
    }

    // IntelliJ says I shouldn't use an Optional as a type parameter,
    // but this whole class is basically an optional with an error message
    public CreateResult(Optional<? extends T> value, Text errorMessage) {
        this.success = value.isPresent();
        this.value = value.orElse(null);
        this.errorMessage = errorMessage;
    }

    public CreateResult(Optional<? extends T> value, String errorMessage) {
        this(value, Text.of(TextColors.RED, errorMessage));
    }

    public CreateResult(T value) {
        this.value = value;
        success = true;
    }

    public Optional<T> getValue() {
        return success ? Optional.ofNullable(value) : Optional.empty();
    }


}
