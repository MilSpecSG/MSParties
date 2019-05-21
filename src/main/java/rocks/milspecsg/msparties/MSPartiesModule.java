package rocks.milspecsg.msparties;

import com.google.inject.AbstractModule;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.party.NameVerificationService;
import rocks.milspecsg.msparties.api.party.PartyCacheService;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.commands.party.PartyCreateCommand;
import rocks.milspecsg.msparties.commands.party.PartyDisbandCommand;
import rocks.milspecsg.msparties.commands.party.PartyInfoCommand;
import rocks.milspecsg.msparties.service.config.ApiConfigurationService;
import rocks.milspecsg.msparties.service.member.ApiMemberRepository;
import rocks.milspecsg.msparties.service.party.ApiNameVerificationService;
import rocks.milspecsg.msparties.service.party.ApiPartyCacheService;
import rocks.milspecsg.msparties.service.party.ApiPartyRepository;

public class MSPartiesModule extends AbstractModule {
    @Override
    protected void configure() {
        //        bind(new TypeLiteral<PartyRepository<User>>(){}).to(new TypeLiteral<PartyRepository<User>>(){});
        bind(PartyRepository.class).to(ApiPartyRepository.class);
        bind(ConfigurationService.class).to(ApiConfigurationService.class);
        bind(NameVerificationService.class).to(ApiNameVerificationService.class);
        bind(PartyCacheService.class).to(ApiPartyCacheService.class);
        bind(MemberRepository.class).to(ApiMemberRepository.class);
        bind(PartyCreateCommand.class);
        bind(PartyDisbandCommand.class);
        bind(PartyInfoCommand.class);
    }
}
