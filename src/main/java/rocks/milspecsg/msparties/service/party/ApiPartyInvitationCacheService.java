package rocks.milspecsg.msparties.service.party;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.party.PartyInvitationCacheService;
import rocks.milspecsg.msparties.model.misc.PartyInvitation;
import rocks.milspecsg.msparties.service.ApiCacheInvalidationService;

import java.util.*;

@Singleton
public class ApiPartyInvitationCacheService extends ApiCacheInvalidationService<PartyInvitation> implements PartyInvitationCacheService {


    @Inject
    public ApiPartyInvitationCacheService(ConfigurationService configurationService) {
        super(configurationService);
    }


    @Override
    public List<? extends PartyInvitation> getAll(UUID userUUID) {
        return getAll(partyInvitation -> partyInvitation.targetPlayer.equals(userUUID));
    }

    @Override
    public boolean hasInvitation(ObjectId partyId, UUID userUUID) {
        return getAll(userUUID).stream().anyMatch(partyInvitation -> partyInvitation.partyId.equals(partyId));
    }

    @Override
    public void put(ObjectId partyId, UUID targetPlayer) {
        PartyInvitation partyInvitation = new PartyInvitation();
        partyInvitation.partyId = partyId;
        partyInvitation.targetPlayer = targetPlayer;
        partyInvitation.message = "";
        put(partyInvitation);
    }

    @Override
    public void remove(ObjectId partyId, UUID targetPlayer) {
        remove(partyInvitation -> partyInvitation.partyId.equals(partyId) && partyInvitation.targetPlayer.equals(targetPlayer));
    }

    @Override
    public void clearInvitations(UUID targetPlayer) {
        remove(partyInvitation -> partyInvitation.targetPlayer.equals(targetPlayer));
    }
}
