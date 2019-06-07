package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.Optional;

public class PartyDisbandCommand<P extends Party> implements CommandExecutor {

    private PartyRepository<P> partyRepository;

    @Inject
    public PartyDisbandCommand(PartyRepository<P> partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public CommandResult execute(@Root CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalParty = context.getOne(Text.of("party"));

        if (source instanceof Player) {
            Player player = (Player) source;

            PartyCommandManager.handleMultiplePartyCommand(() -> optionalParty, player, this::handleName, partyRepository, "/p disband [<party>]");

            return CommandResult.success();
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Command can only be run as player"));
        }
    }

    private void handleName(String name, Player player) {
        partyRepository.disband(name, player).thenAcceptAsync(permissibleResult -> player.sendMessage(PluginInfo.PluginPrefix.concat(permissibleResult.getMessage())));
    }
}
