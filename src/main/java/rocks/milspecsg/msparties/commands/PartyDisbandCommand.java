package rocks.milspecsg.msparties.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.util.annotation.NonnullByDefault;

public class PartyDisbandCommand implements CommandExecutor {

    @Override
    @NonnullByDefault
    public CommandResult execute(@NonnullByDefault @Root CommandSource src, @NonnullByDefault CommandContext args) throws CommandException {
        return null;
    }
}
