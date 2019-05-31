package rocks.milspecsg.msparties;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.member.MemberCacheService;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.member.TeleportationCacheService;
import rocks.milspecsg.msparties.api.party.*;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.service.config.ApiConfigurationService;
import rocks.milspecsg.msparties.service.member.ApiMemberCacheService;
import rocks.milspecsg.msparties.service.member.ApiMemberRepository;
import rocks.milspecsg.msparties.service.member.ApiTeleportationCacheService;
import rocks.milspecsg.msparties.service.party.*;

/**
 * Default bindings used for the MSParties API
 *
 * @author Cableguy20
 */
public abstract class APIModule<P extends Party, M extends Member> extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<ConfigurationService>() {}).to(new TypeLiteral<ApiConfigurationService>() {});
        bind(new TypeLiteral<PartyRepository<P>>() {}).to(new TypeLiteral<ApiPartyRepository<P, M>>() {});
        bind(new TypeLiteral<PartyCacheService<P>>() {}).to(new TypeLiteral<ApiPartyCacheService<P>>() {});
        bind(new TypeLiteral<PartyNameCacheService>() {}).to(new TypeLiteral<ApiPartyNameCacheService>() {});
        bind(new TypeLiteral<NameVerificationService>() {}).to(new TypeLiteral<ApiNameVerificationService>() {});

        bind(new TypeLiteral<MemberRepository<M>>() {}).to(new TypeLiteral<ApiMemberRepository<M>>() {});
        bind(new TypeLiteral<MemberCacheService<M>>() {}).to(new TypeLiteral<ApiMemberCacheService<M>>() {});
        bind(new TypeLiteral<TeleportationCacheService>() {}).to(new TypeLiteral<ApiTeleportationCacheService<M>>() {});
        bind(new TypeLiteral<PermissionCacheService>() {}).to(new TypeLiteral<ApiPermissionCacheService>() {});
        bind(new TypeLiteral<PartyInvitationCacheService>() {}).to(new TypeLiteral<ApiPartyInvitationCacheService<P, M>>() {});
    }
}
