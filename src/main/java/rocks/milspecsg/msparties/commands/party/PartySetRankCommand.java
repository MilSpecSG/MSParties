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
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.party.PartyRepository;

import java.util.Optional;

public class PartySetRankCommand implements CommandExecutor {


    protected PartyRepository partyRepository;

    @Inject
    public PartySetRankCommand(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<Player> optionalPlayer = context.getOne(Text.of("player"));
        Optional<Integer> optionalRankIndex = context.getOne(Text.of("rankIndex"));
        Optional<String> optionalParty = context.getOne(Text.of("party"));
        if (source instanceof Player) {
            if (!optionalPlayer.isPresent()) {
                throw new CommandException(Text.of(TextColors.RED, "Missing player"));
            }

            if (!optionalRankIndex.isPresent()) {
                throw new CommandException(Text.of(TextColors.RED, "Missing rankIndex"));
            }

            if (!optionalParty.isPresent()) {
                throw new CommandException(Text.of(TextColors.RED, "Missing party"));
            }

            // TODO: use PartyCommandManager methods to clean this up

            partyRepository.setRank(optionalParty.get(), (Player) source, optionalPlayer.get().getUniqueId(), optionalRankIndex.get()).thenAcceptAsync(permissibleResult -> source.sendMessage(PluginInfo.PluginPrefix.concat(permissibleResult.getMessage())));

            return CommandResult.success();
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Command can only be run as player"));
        }

    }
}
