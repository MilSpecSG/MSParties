package rocks.milspecsg.msparties.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

public class HelpCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

        Player player = (Player)source;



        return null;

    }
}
