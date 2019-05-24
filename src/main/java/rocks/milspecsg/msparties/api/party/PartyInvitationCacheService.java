package rocks.milspecsg.msparties.api.party;

import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.model.misc.PartyInvitation;

import java.util.List;
import java.util.UUID;

public interface PartyInvitationCacheService {

    List<PartyInvitation> getInvitations(UUID userUUID);

    boolean hasInvitation(ObjectId partyId, UUID userUUID);

    void addInvitation(PartyInvitation partyInvitation, UUID userUUID);

    boolean removeInvitation(ObjectId partyId, UUID userUUID);

    void clearInvitations(UUID userUUID);

}
