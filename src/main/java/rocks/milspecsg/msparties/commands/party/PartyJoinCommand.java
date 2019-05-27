package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.api.party.PartyRepository;

import java.util.Optional;

public class PartyJoinCommand implements CommandExecutor {

    protected PartyRepository partyRepository;

    @Inject
    public PartyJoinCommand(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalName = context.getOne(Text.of("name"));

        if (source instanceof Player) {
            Player player = (Player) source;
            if (!optionalName.isPresent()) throw new CommandException(Text.of(TextColors.RED, "Missing name"));

            partyRepository.joinParty(optionalName.get(), player).thenAcceptAsync(result -> player.sendMessage(result.getMessage()));

            return CommandResult.success();
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Command can only be run as player"));
        }    }
}
