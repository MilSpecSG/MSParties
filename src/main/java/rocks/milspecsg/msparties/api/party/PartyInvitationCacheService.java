package rocks.milspecsg.msparties.api.party;

import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.api.CacheInvalidationService;
import rocks.milspecsg.msparties.model.misc.PartyInvitation;

import java.util.List;
import java.util.UUID;

public interface PartyInvitationCacheService extends CacheInvalidationService<PartyInvitation> {

    List<? extends PartyInvitation> getAll(UUID userUUID);

    boolean hasInvitation(ObjectId partyId, UUID userUUID);

    void put(ObjectId partyId, UUID userUUID);

    void remove(ObjectId partyId, UUID userUUID);

    void clearInvitations(UUID userUUID);

}
