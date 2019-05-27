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
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.member.TeleportationCacheService;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.misc.TeleportationRequest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PartyAcceptCommand implements CommandExecutor {

    protected TeleportationCacheService teleportationCacheService;
    protected PartyRepository partyRepository;
    protected MemberRepository memberRepository;

    @Inject
    public PartyAcceptCommand(TeleportationCacheService teleportationCacheService, PartyRepository partyRepository, MemberRepository memberRepository) {
        this.teleportationCacheService = teleportationCacheService;
        this.partyRepository = partyRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalName = context.getOne(Text.of("party"));

        if (source instanceof Player) {
            Player player = (Player) source;


            CompletableFuture.runAsync(() -> {
                if (optionalName.isPresent()) {
                    partyRepository.getOne(optionalName.get()).join().ifPresent(party -> teleportationCacheService.getOne(player.getUniqueId(), party.getId()).ifPresent(request -> handle(request, player)));
                } else {
                    List<? extends TeleportationRequest> requests = teleportationCacheService.getAll(player.getUniqueId());
                    PartyCommandManager.handleManyOptional(player, requests, Optional::of, this::handle, Text.of(TextColors.RED, "You do not have any teleportation requests"), () -> PartyCommandManager.listCurrentTeleportationRequestsToPlayer(player, requests, partyRepository));
                }
            });

            return CommandResult.success();
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Command can only be run as player"));
        }
    }

    private void handle(TeleportationRequest request, Player player) {
        teleportationCacheService.accept(request);
    }
}
