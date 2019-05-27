package rocks.milspecsg.msparties.api.party;

import org.bson.types.ObjectId;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import rocks.milspecsg.msparties.api.CacheInvalidationService;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.misc.PartyInvitation;
import rocks.milspecsg.msparties.model.misc.TeleportationRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartyInvitationCacheService extends CacheInvalidationService<PartyInvitation> {

    List<? extends PartyInvitation> getAll(UUID userUUID);

    boolean hasInvitation(ObjectId partyId, UUID userUUID);

    void remove(ObjectId partyId, UUID targetPlayer);

    void accept(PartyInvitation request);

    Optional<? extends PartyInvitation> sendRequest(User inviter, Player invitee, Party party);

    void clearInvitations(UUID userUUID);
}
