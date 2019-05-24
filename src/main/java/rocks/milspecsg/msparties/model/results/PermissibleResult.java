package rocks.milspecsg.msparties.model.results;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PermissibleResult extends Result {

    private PermissibleResult() {
    }

    public static PermissibleResult success(Text successMessage) {
        PermissibleResult result = new PermissibleResult();
        result.success = true;
        result.successMessage = successMessage;
        return result;
    }

    public static PermissibleResult fail(Text errorMessage) {
        PermissibleResult result = new PermissibleResult();
        result.success = false;
        result.errorMessage = errorMessage;
        return result;
    }

    public static PermissibleResult fail(String errorMessage) {
        return fail(Text.of(TextColors.RED, errorMessage));
    }

    public static PermissibleResult fail() {
        return fail("You do not have permission to do that");
    }
}
