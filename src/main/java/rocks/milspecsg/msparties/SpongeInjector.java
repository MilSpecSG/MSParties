package rocks.milspecsg.msparties;

import com.google.inject.AbstractModule;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.commands.party.PartyCreateCommand;
import rocks.milspecsg.msparties.commands.party.PartyDisbandCommand;
import rocks.milspecsg.msparties.commands.party.PartyFindCommand;
import rocks.milspecsg.msparties.service.party.ApiPartyRepository;

public class SpongeInjector extends AbstractModule {
    @Override
    protected void configure() {
        //        bind(new TypeLiteral<PartyRepository<User>>(){}).to(new TypeLiteral<PartyRepository<User>>(){});
        bind(PartyRepository.class).to(ApiPartyRepository.class);
        bind(PartyCreateCommand.class);
        bind(PartyDisbandCommand.class);
        bind(PartyFindCommand.class);
    }
}
