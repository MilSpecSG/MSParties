package rocks.milspecsg.msparties;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import rocks.milspecsg.msparties.commands.party.*;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.service.member.ApiMemberCacheService;
import rocks.milspecsg.msparties.service.member.ApiMemberRepository;
import rocks.milspecsg.msparties.service.member.implementation.MSMemberRepository;
import rocks.milspecsg.msparties.service.party.*;
import rocks.milspecsg.msparties.service.party.implementation.MSPartyRepository;

class MSPartiesModule extends APIModule<Party, Member> {
    @Override
    protected void configure() {
        super.configure();

        bind(new TypeLiteral<ApiPartyRepository<Party, Member>>() {}).to(new TypeLiteral<MSPartyRepository>() {});
        bind(new TypeLiteral<ApiMemberRepository<Member>>() {}).to(new TypeLiteral<MSMemberRepository>() {});
    }
}
