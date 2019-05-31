package rocks.milspecsg.msparties.service.party;

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
import rocks.milspecsg.msparties.api.party.PartyInvitationCacheService;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.misc.PartyInvitation;
import rocks.milspecsg.msparties.service.ApiCacheInvalidationService;

import java.util.*;

@Singleton
public class ApiPartyInvitationCacheService<P extends Party, M extends Member> extends ApiCacheInvalidationService<PartyInvitation> implements PartyInvitationCacheService {

    protected PartyRepository<P> partyRepository;
    protected MemberRepository<M> memberRepository;

    @Inject
    public ApiPartyInvitationCacheService(ConfigurationService configurationService, PartyRepository<P> partyRepository, MemberRepository<M> memberRepository) {
        super(configurationService);
        this.partyRepository = partyRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<PartyInvitation> getAll(UUID userUUID) {
        return getAll(partyInvitation -> partyInvitation.targetPlayer.equals(userUUID));
    }

    @Override
    public boolean hasInvitation(ObjectId partyId, UUID userUUID) {
        return getAll(userUUID).stream().anyMatch(partyInvitation -> partyInvitation.partyId.equals(partyId));
    }

    @Override
    public void remove(ObjectId partyId, UUID targetPlayer) {
        remove(partyInvitation -> partyInvitation.partyId.equals(partyId) && partyInvitation.targetPlayer.equals(targetPlayer));
    }

    @Override
    public void accept(PartyInvitation invitation) {
        memberRepository.getUser(invitation.targetPlayer).ifPresent(targetUser -> targetUser.getPlayer().ifPresent(target -> {
                target.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.GRAY, "You have successfully accepted the invitation for", TextColors.YELLOW, target.getName()));
        }));
        remove(invitation);
    }

    @Override
    public Optional<? extends PartyInvitation> sendRequest(User inviter, Player invitee, Party party) {
        invitee.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.GRAY, "You have received an invitation from\n", TextColors.YELLOW, inviter.getName(), TextColors.GRAY, " for the ", partyRepository.getDefaultIdentifierSingularLower(), " ", TextColors.YELLOW, party.name,
                TextColors.GRAY, "\nType ", TextColors.GREEN, "/p join ", party.name, TextColors.GRAY, " to join"));
        PartyInvitation partyInvitation = new PartyInvitation();
        partyInvitation.partyId = party.getId();
        partyInvitation.targetPlayer = invitee.getUniqueId();
        partyInvitation.message = "";
        return put(partyInvitation);
    }

    @Override
    public void clearInvitations(UUID targetPlayer) {
        remove(partyInvitation -> partyInvitation.targetPlayer.equals(targetPlayer));
    }
}
