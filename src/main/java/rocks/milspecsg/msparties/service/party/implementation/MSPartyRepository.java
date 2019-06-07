package rocks.milspecsg.msparties.service.party.implementation;

import com.google.inject.Inject;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.member.TeleportationCacheService;
import rocks.milspecsg.msparties.api.party.NameVerificationService;
import rocks.milspecsg.msparties.api.party.PartyCacheService;
import rocks.milspecsg.msparties.api.party.PartyInvitationCacheService;
import rocks.milspecsg.msparties.api.party.PermissionCacheService;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.service.party.ApiPartyRepository;

public class MSPartyRepository extends ApiPartyRepository<Party, Member> {

    @Inject
    public MSPartyRepository(
            MongoContext mongoContext,
            ConfigurationService configurationService,
            NameVerificationService nameVerificationService,
            PermissionCacheService permissionCacheService,
            PartyCacheService<Party> partyCacheService,
            MemberRepository<Member> memberRepository,
            PartyInvitationCacheService partyInvitationCacheService,
            TeleportationCacheService teleportationCacheService
    ) {
        super(
                mongoContext,
                configurationService,
                nameVerificationService,
                permissionCacheService,
                partyCacheService,
                memberRepository,
                partyInvitationCacheService,
                teleportationCacheService
        );
    }

    @Override
    public Party generateEmpty() {
        return new Party();
    }

    @Override
    public UpdateOperations<Party> createUpdateOperations() {
        return mongoContext.datastore.createUpdateOperations(Party.class);
    }

    @Override
    public Query<Party> asQuery() {
        return mongoContext.datastore.createQuery(Party.class);
    }
}
