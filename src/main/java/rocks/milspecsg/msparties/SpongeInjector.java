package rocks.milspecsg.msparties;

import com.google.inject.AbstractModule;
import rocks.milspecsg.msparties.api.party.BasePartyRepository;
import rocks.milspecsg.msparties.service.party.PartyRepository;

public class SpongeInjector extends AbstractModule {
    @Override
    protected void configure() {
        //        bind(new TypeLiteral<BasePartyRepository<User>>(){}).to(new TypeLiteral<PartyRepository<User>>(){});
        bind(BasePartyRepository.class).to(PartyRepository.class);
    }
}
