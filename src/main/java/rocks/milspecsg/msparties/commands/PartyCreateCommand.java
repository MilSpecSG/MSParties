package rocks.milspecsg.msparties.commands;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.api.party.BasePartyRepository;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.Optional;

public class PartyCreateCommand implements CommandExecutor {

    @Inject
    private BasePartyRepository<User> partyManager;

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalName = context.getOne(Text.of("name"));
        if (source instanceof Player) {
            Player player = (Player) source;

            // should not happen
            if (!optionalName.isPresent()) {
                source.sendMessage(Text.of(TextColors.RED, "Missing: name"));
                return CommandResult.success();
            }

            Party created = partyManager.createParty(optionalName.get(), player);
            player.sendMessage(Text.of(TextColors.GREEN, "Successfully created: ", created.getName()));
        } else {
            source.sendMessage(Text.of(TextColors.RED, "Command can only be run as player"));
        }
        return CommandResult.success();
    }
}
