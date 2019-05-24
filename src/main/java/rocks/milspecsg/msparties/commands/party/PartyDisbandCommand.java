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
        Optional<String> optionalName = context.getOne(Text.of("name"));

        if (source instanceof Player) {
            Player player = (Player) source;

            // should not happen
            if (optionalName.isPresent()) {
                handleName(optionalName.get(), player);
            } else {
                partyRepository.getAllForMember(player.getUniqueId()).thenAcceptAsync(parties -> {
                            if (parties.size() == 0) {
                                player.sendMessage(Text.of(TextColors.RED, "You are not currently in a party"));
                            } else if (parties.size() == 1) {
                                handleName(parties.get(0).name, player);
                            } else {
                                player.sendMessage(Text.of(
                                        PluginInfo.PluginPrefix, TextColors.RED, "You are in the following parties:\n",
                                        TextColors.GOLD, parties.stream().map(party -> party.name).collect(Collectors.joining(", ")),
                                        TextColors.RED, "\nYou must pick one by using /p disband <name>"
                                ));
                            }
                        }
                );
            }

            return CommandResult.success();
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Command can only be run as player"));
        }
    }

    private void handleName(String name, Player player) {
        partyRepository.disbandParty(name, player).thenAcceptAsync(permissibleResult -> player.sendMessage(PluginInfo.PluginPrefix.concat(permissibleResult.getMessage())));
    }
}
