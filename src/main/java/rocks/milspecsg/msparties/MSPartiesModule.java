package rocks.milspecsg.msparties;

import com.google.inject.AbstractModule;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.member.MemberCacheService;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.member.TeleportationCacheService;
import rocks.milspecsg.msparties.api.party.*;
import rocks.milspecsg.msparties.commands.party.*;
import rocks.milspecsg.msparties.service.config.ApiConfigurationService;
import rocks.milspecsg.msparties.service.member.ApiMemberCacheService;
import rocks.milspecsg.msparties.service.member.ApiMemberRepository;
import rocks.milspecsg.msparties.service.member.ApiTeleportationCacheService;
import rocks.milspecsg.msparties.service.party.*;

public class MSPartiesModule extends AbstractModule {
    @Override
    protected void configure() {
        //        bind(new TypeLiteral<PartyRepository<User>>(){}).to(new TypeLiteral<PartyRepository<User>>(){});
        bind(PartyRepository.class).to(ApiPartyRepository.class);
        bind(ConfigurationService.class).to(ApiConfigurationService.class);
        bind(NameVerificationService.class).to(ApiNameVerificationService.class);
        bind(PartyCacheService.class).to(ApiPartyCacheService.class);
        bind(PartyNameCacheService.class).to(ApiPartyNameCacheService.class);
        bind(MemberRepository.class).to(ApiMemberRepository.class);
        bind(MemberCacheService.class).to(ApiMemberCacheService.class);
        bind(TeleportationCacheService.class).to(ApiTeleportationCacheService.class);
        bind(PermissionCacheService.class).to(ApiPermissionCacheService.class);
        bind(PartyInvitationCacheService.class).to(ApiPartyInvitationCacheService.class);
        bind(PartyCommandManager.class);
        bind(PartyAcceptCommand.class);
        bind(PartyCreateCommand.class);
        bind(PartyDisbandCommand.class);
        bind(PartyPrivacyCommand.class);
        bind(PartyInfoCommand.class);
        bind(PartyTpaallCommand.class);
    }
}
