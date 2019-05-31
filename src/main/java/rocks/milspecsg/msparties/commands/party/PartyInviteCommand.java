package rocks.milspecsg.msparties.commands.party;

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
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.party.PartyInvitationCacheService;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PartyInviteCommand<P extends Party> implements CommandExecutor {

    protected PartyRepository<P> partyRepository;

    @Inject
    public PartyInviteCommand(PartyRepository<P> partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalName = context.getOne(Text.of("party"));
        Optional<Player> optionalPlayer = context.getOne(Text.of("player"));

        if (source instanceof Player) {
            Player player = (Player) source;
            if (!optionalPlayer.isPresent()) throw new CommandException(Text.of(TextColors.RED, "Missing player"));
            if (optionalName.isPresent()) {
                handle(optionalName.get(), player, optionalPlayer.get());
            } else {
                partyRepository.getAllForMember(player.getUniqueId()).thenAcceptAsync(parties -> PartyCommandManager.handleManyOptional(player, optionalPlayer.get(), parties, party -> Optional.of(party.name), this::handle, Text.of(TextColors.RED, "You are not currently in a party"), () -> PartyCommandManager.listCurrentPartiesToPlayer(player, parties, "/p invite <name> [<party>]")));
            }

            return CommandResult.success();
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Command can only be run as player"));
        }
    }

    private void handle(String name, Player source, Player targetPlayer) {
        partyRepository.inviteUser(name, source, targetPlayer).thenAcceptAsync(permissibleResult -> source.sendMessage(PluginInfo.PluginPrefix.concat(permissibleResult.getMessage())));
    }
}
