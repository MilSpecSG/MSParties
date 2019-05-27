package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.member.TeleportationCacheService;
import rocks.milspecsg.msparties.api.party.PartyRepository;

import java.util.Optional;
import java.util.Set;

public class PartyTpaallCommand implements CommandExecutor {

    protected PartyRepository partyRepository;
    protected MemberRepository memberRepository;
    protected TeleportationCacheService teleportationCacheService;

    @Inject
    public PartyTpaallCommand(PartyRepository partyRepository, MemberRepository memberRepository, TeleportationCacheService teleportationCacheService) {
        this.partyRepository = partyRepository;
        this.memberRepository = memberRepository;
        this.teleportationCacheService = teleportationCacheService;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalName = context.getOne(Text.of("name"));
        if (source instanceof Player) {
            PartyCommandManager.handleMultiplePartyCommand(() -> optionalName, (Player) source, this::handleName, partyRepository, "/p accept <name>");
            return CommandResult.success();
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Command can only be run as player"));
        }
    }

    private void handleName(String name, Player player) {
        partyRepository.getOne(name).thenAcceptAsync(optionalParty -> optionalParty
                .ifPresent(party -> party.members.keySet().forEach(id -> memberRepository.getUser(id)
                        .thenAcceptAsync(optionalUser -> optionalUser.flatMap(User::getPlayer)
                                .ifPresent(teleporter -> teleportationCacheService.sendRequest(teleporter, player, party))))));
    }
}
