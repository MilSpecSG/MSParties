package rocks.milspecsg.msparties.service.party;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.api.party.PartyInvitationCacheService;
import rocks.milspecsg.msparties.model.misc.PartyInvitation;

import java.util.*;

@Singleton
public class ApiPartyInvitationCacheService implements PartyInvitationCacheService {

    protected Map<UUID, List<PartyInvitation>> userInvitationMap;

    @Inject
    public ApiPartyInvitationCacheService() {
        this.userInvitationMap = new HashMap<>();
    }

    @Override
    public List<PartyInvitation> getInvitations(UUID userUUID) {
        List<PartyInvitation> invitations = userInvitationMap.get(userUUID);
        if (invitations == null) return new ArrayList<>();
        return invitations;
    }

    @Override
    public boolean hasInvitation(ObjectId partyId, UUID userUUID) {
        List<PartyInvitation> invitations = userInvitationMap.get(userUUID);
        return invitations != null && invitations.stream().anyMatch(partyInvitation -> partyInvitation.partyId.equals(partyId));
    }

    @Override
    public void addInvitation(PartyInvitation partyInvitation, UUID userUUID) {
        if (userInvitationMap.containsKey(userUUID)) userInvitationMap.get(userUUID).add(partyInvitation);
        else userInvitationMap.put(userUUID, Collections.singletonList(partyInvitation));
    }

    @Override
    public boolean removeInvitation(ObjectId partyId, UUID userUUID) {
        if (!userInvitationMap.containsKey(userUUID)) return false;
        return userInvitationMap.get(userUUID).removeIf(partyInvitation -> partyInvitation.partyId.equals(partyId));
    }

    @Override
    public void clearInvitations(UUID userUUID) {
        userInvitationMap.remove(userUUID);
    }
}
