package rocks.milspecsg.msparties.service.member;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bson.types.ObjectId;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.MSParties;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.member.TeleportationCacheService;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.misc.TeleportationRequest;
import rocks.milspecsg.msparties.service.ApiCacheInvalidationService;

import java.util.*;

@Singleton
public class ApiTeleportationCacheService extends ApiCacheInvalidationService<TeleportationRequest> implements TeleportationCacheService {

    protected MemberRepository memberRepository;

    @Inject
    public ApiTeleportationCacheService(ConfigurationService configurationService, MemberRepository memberRepository) {
        super(configurationService);
        this.memberRepository = memberRepository;
    }

    @Override
    public Optional<? extends TeleportationRequest> getOne(UUID teleportingPlayer, ObjectId partyId) {
        return getOne(request -> request.teleportingPlayer.equals(teleportingPlayer) && request.targetPartyId.equals(partyId));
    }

    public Optional<? extends TeleportationRequest> getOne(UUID teleportingPlayer) {
        return getOne(request -> request.teleportingPlayer.equals(teleportingPlayer));
    }

    @Override
    public List<? extends TeleportationRequest> getAll(UUID teleportingPlayer) {
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
    public Optional<? extends TeleportationRequest> sendRequest(Player teleporter, Player receiver, Party party) {
        teleporter.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.YELLOW, receiver.getName(), TextColors.GRAY, " from ", TextColors.YELLOW, party.name, TextColors.GRAY, " has requested that you teleport to them.\n",
                "type ", TextColors.GREEN, "/p accept", TextColors.GRAY, " to accept"));
        TeleportationRequest request = new TeleportationRequest();
        request.teleportingPlayer = teleporter.getUniqueId();
        request.targetPlayer = receiver.getUniqueId();
        request.targetPartyId = party.getId();
        request.message = "";
        return put(request);
    }
}
