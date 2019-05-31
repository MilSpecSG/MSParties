package rocks.milspecsg.msparties.service.member;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bson.types.ObjectId;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.MSParties;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.member.TeleportationCacheService;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.misc.TeleportationRequest;
import rocks.milspecsg.msparties.service.ApiCacheInvalidationService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Singleton
public class ApiTeleportationCacheService<M extends Member> extends ApiCacheInvalidationService<TeleportationRequest> implements TeleportationCacheService {

    protected MemberRepository<M> memberRepository;

    @Inject
    public ApiTeleportationCacheService(ConfigurationService configurationService, MemberRepository<M> memberRepository) {
        super(configurationService);
        this.memberRepository = memberRepository;
    }

    @Override
    public Optional<TeleportationRequest> getOne(UUID teleportingPlayer, ObjectId partyId) {
        return getOne(request -> request.teleportingPlayer.equals(teleportingPlayer) && request.targetPartyId.equals(partyId));
    }

    public Optional<TeleportationRequest> getOne(UUID teleportingPlayer) {
        return getOne(request -> request.teleportingPlayer.equals(teleportingPlayer));
    }

    @Override
    public List<TeleportationRequest> getAll(UUID teleportingPlayer) {
        return getAll(request -> request.teleportingPlayer.equals(teleportingPlayer));
    }

    @Override
    public void accept(TeleportationRequest request) {
        memberRepository.getUser(request.targetPlayer).ifPresent(targetUser -> targetUser.getPlayer().ifPresent(target -> {
            memberRepository.getUser(request.teleportingPlayer).ifPresent(user -> user.getPlayer().ifPresent(player -> {
                player.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.GRAY, "You have successfully teleported to ", TextColors.YELLOW, target.getName(), TextColors.GRAY));
                Task.builder().execute(() -> {
                    player.setLocation(target.getLocation());
                }).submit(MSParties.plugin);
            }));
        }));
        remove(request);
    }

    @Override
    public CompletableFuture<List<Player>> sendRequest(User receiver, Party party) {
        return CompletableFuture.supplyAsync(() -> party.members.keySet().stream().map(id -> memberRepository.getUser(id).thenApplyAsync(optionalUser -> optionalUser.flatMap(User::getPlayer)).join()
                .map(teleporter -> {
                    if (teleporter.getUniqueId().equals(receiver.getUniqueId())) return null;
                    teleporter.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.YELLOW, receiver.getName(), TextColors.GRAY, " from ", TextColors.YELLOW, party.name, TextColors.GRAY, " has requested that you teleport to them.\n",
                            "type ", TextColors.GREEN, "/p accept", TextColors.GRAY, " to accept"));
                    TeleportationRequest request = new TeleportationRequest();
                    request.teleportingPlayer = teleporter.getUniqueId();
                    request.targetPlayer = receiver.getUniqueId();
                    request.targetPartyId = party.getId();
                    request.message = "";
                    put(request);
                    return teleporter;
                }).orElse(null)).filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
