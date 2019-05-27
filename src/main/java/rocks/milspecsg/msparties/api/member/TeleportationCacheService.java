package rocks.milspecsg.msparties.api.member;

import org.bson.types.ObjectId;
import org.spongepowered.api.entity.living.player.Player;
import rocks.milspecsg.msparties.api.CacheInvalidationService;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.misc.TeleportationRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeleportationCacheService extends CacheInvalidationService<TeleportationRequest> {

    Optional<? extends TeleportationRequest> getOne(UUID teleportingPlayer, ObjectId partyId);

    Optional<? extends TeleportationRequest> getOne(UUID teleportingPlayer);

    List<? extends TeleportationRequest> getAll(UUID teleportingPlayer);

    void accept(TeleportationRequest request);

    Optional<? extends TeleportationRequest> sendRequest(Player teleporter, Player receiver, Party party);

}
