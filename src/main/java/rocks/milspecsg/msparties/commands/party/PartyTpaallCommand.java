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
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.Optional;
import java.util.Set;

public class PartyTpaallCommand<P extends Party, M extends Member> implements CommandExecutor {

    protected PartyRepository<P> partyRepository;
    protected MemberRepository<M> memberRepository;
    protected TeleportationCacheService teleportationCacheService;

    @Inject
    public PartyTpaallCommand(PartyRepository<P> partyRepository, MemberRepository<M> memberRepository, TeleportationCacheService teleportationCacheService) {
        this.partyRepository = partyRepository;
        this.memberRepository = memberRepository;
        this.teleportationCacheService = teleportationCacheService;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalName = context.getOne(Text.of("party"));
        if (source instanceof Player) {
            Player player = (Player) source;
            PartyCommandManager.handleMultiplePartyCommand(() -> optionalName, player, this::handle, partyRepository, "/p here [<party>]");
            return CommandResult.success();
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Command can only be run as player"));
        }
    }

    private void handle(String name, Player source) {
        partyRepository.tpaall(name, source).thenAcceptAsync(permissibleResult -> source.sendMessage(permissibleResult.getMessage()));
    }
}
