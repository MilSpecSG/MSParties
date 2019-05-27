package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.Optional;
import java.util.stream.Collectors;

public class PartyDisbandCommand implements CommandExecutor {

    private PartyRepository partyRepository;

    @Inject
    public PartyDisbandCommand(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public CommandResult execute(@Root CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalName = context.getOne(Text.of("party"));

        if (source instanceof Player) {
            Player player = (Player) source;

            PartyCommandManager.handleMultiplePartyCommand(() -> optionalName, player, this::handleName, partyRepository, "/p disband <name>");

            return CommandResult.success();
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Command can only be run as player"));
        }
    }

    private void handleName(String name, Player player) {
        partyRepository.disbandParty(name, player).thenAcceptAsync(permissibleResult -> player.sendMessage(PluginInfo.PluginPrefix.concat(permissibleResult.getMessage())));
    }
}
