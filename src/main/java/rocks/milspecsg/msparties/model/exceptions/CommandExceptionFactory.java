package rocks.milspecsg.msparties.model.exceptions;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.text.Text;

public abstract class CommandExceptionFactory {
    public static CommandException fromException(Exception e) {
        return new CommandException(Text.of(e.getMessage()));
    }
}
