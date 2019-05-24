package rocks.milspecsg.msparties.model.results;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Result {

    protected boolean success;
    protected Text successMessage, errorMessage;


    public Result() {
        success = false;
        successMessage = Text.of(TextColors.GREEN, "success");
        errorMessage = Text.of(TextColors.RED, "error");
    }

    public boolean isSuccess() {
        return success;
    }

    public Text getMessage() {
        return success ? successMessage : errorMessage;
    }

    public Text getErrorMessage() {
        return errorMessage;
    }


}
