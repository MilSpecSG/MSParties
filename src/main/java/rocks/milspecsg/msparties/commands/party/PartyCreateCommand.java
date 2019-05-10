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
import rocks.milspecsg.msparties.model.exceptions.CommandExceptionFactory;
import rocks.milspecsg.msparties.model.exceptions.InvalidNameException;

import java.util.Optional;

public class PartyCreateCommand implements CommandExecutor {

    private PartyRepository partyRepository;

    @Inject
    public PartyCreateCommand(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalName = context.getOne(Text.of("name"));
        if (source instanceof Player) {
            Player player = (Player) source;

            // should not happen
            if (!optionalName.isPresent()) throw new CommandException(Text.of("Missing name"));
            try {
                partyRepository.createParty(optionalName.get(), player).thenAcceptAsync(
                        party -> player.sendMessage(party.map(party1 -> Text.of(TextColors.GREEN, "Successfully created: ", party1.name))
                                .orElseGet(() -> Text.of(TextColors.RED, "An error occurred")))
                );
            } catch (InvalidNameException e) {
                throw CommandExceptionFactory.fromException(e);
            }
            return CommandResult.success();
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Command can only be run as player"));
        }
    }
}
