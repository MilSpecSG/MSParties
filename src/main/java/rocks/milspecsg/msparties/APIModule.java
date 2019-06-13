package rocks.milspecsg.msparties;

import com.google.common.reflect.TypeToken;
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
public class APIModule<P extends Party, M extends Member> extends AbstractModule {



    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    @Override
    protected void configure() {
        bind(
                (TypeLiteral<ConfigurationService>) TypeLiteral.get(new TypeToken<ConfigurationService>(getClass()) {}.getType())
        ).to(
                (TypeLiteral<ApiConfigurationService>) TypeLiteral.get(new TypeToken<ApiConfigurationService>(getClass()) {}.getType())
        );

        bind(
                (TypeLiteral<PartyRepository<P>>) TypeLiteral.get(new TypeToken<PartyRepository<P>>(getClass()) {}.getType())
        ).to(
                (TypeLiteral<ApiPartyRepository<P, M>>) TypeLiteral.get(new TypeToken<ApiPartyRepository<P, M>>(getClass()) {}.getType())
        );

        bind(
                (TypeLiteral<PartyCacheService<P>>) TypeLiteral.get(new TypeToken<PartyCacheService<P>>(getClass()) {}.getType())
        ).to(
                (TypeLiteral<ApiPartyCacheService<P>>) TypeLiteral.get(new TypeToken<ApiPartyCacheService<P>>(getClass()) {}.getType())
        );

        bind(
                (TypeLiteral<PartyNameCacheService>) TypeLiteral.get(new TypeToken<PartyNameCacheService>(getClass()) {}.getType())
        ).to(
                (TypeLiteral<ApiPartyNameCacheService>) TypeLiteral.get(new TypeToken<ApiPartyNameCacheService>(getClass()) {}.getType())
        );

        bind(
                (TypeLiteral<NameVerificationService>) TypeLiteral.get(new TypeToken<NameVerificationService>(getClass()) {}.getType())
        ).to(
                (TypeLiteral<ApiNameVerificationService>) TypeLiteral.get(new TypeToken<ApiNameVerificationService>(getClass()) {}.getType())
        );

        bind(
                (TypeLiteral<MemberRepository<M>>) TypeLiteral.get(new TypeToken<MemberRepository<M>>(getClass()) {}.getType())
        ).to(
                (TypeLiteral<ApiMemberRepository<M>>) TypeLiteral.get(new TypeToken<ApiMemberRepository<M>>(getClass()) {}.getType())
        );

        bind(
                (TypeLiteral<MemberCacheService<M>>) TypeLiteral.get(new TypeToken<MemberCacheService<M>>(getClass()) {}.getType())
        ).to(
                (TypeLiteral<ApiMemberCacheService<M>>) TypeLiteral.get(new TypeToken<ApiMemberCacheService<M>>(getClass()) {}.getType())
        );

        bind(
                (TypeLiteral<TeleportationCacheService>) TypeLiteral.get(new TypeToken<TeleportationCacheService>(getClass()) {}.getType())
        ).to(
                (TypeLiteral<ApiTeleportationCacheService<Member>>) TypeLiteral.get(new TypeToken<ApiTeleportationCacheService<M>>(getClass()) {}.getType())
        );

        bind(
                (TypeLiteral<PermissionCacheService>) TypeLiteral.get(new TypeToken<PermissionCacheService>(getClass()) {}.getType())
        ).to(
                (TypeLiteral<ApiPermissionCacheService>) TypeLiteral.get(new TypeToken<ApiPermissionCacheService>(getClass()) {}.getType())
        );

        bind(
                (TypeLiteral<PartyInvitationCacheService>) TypeLiteral.get(new TypeToken<PartyInvitationCacheService>(getClass()) {}.getType())
        ).to(
                (TypeLiteral<ApiPartyInvitationCacheService<P, M>>) TypeLiteral.get(new TypeToken<ApiPartyInvitationCacheService<P, M>>(getClass()) {}.getType())
        );
    }
}
