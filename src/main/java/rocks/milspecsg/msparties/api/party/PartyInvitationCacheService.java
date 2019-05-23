package rocks.milspecsg.msparties.api.party;

import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.model.misc.PartyInvitation;

import java.util.List;
import java.util.UUID;

public interface PartyInvitationCacheService {

    List<PartyInvitation> getInvitations(UUID userUUID);

    boolean hasInvitation(UUID userUUID, ObjectId partyId);

    void addInvitation(UUID userUUID, PartyInvitation partyInvitation);

    boolean removeInvitation(UUID userUUID, ObjectId partyId);

    void clearInvitations(UUID userUUID);

}
