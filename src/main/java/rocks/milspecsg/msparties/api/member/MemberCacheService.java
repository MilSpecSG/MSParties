package rocks.milspecsg.msparties.api.member;

import rocks.milspecsg.msparties.api.RepositoryCacheService;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.Optional;
import java.util.UUID;

public interface MemberCacheService extends RepositoryCacheService<Member> {

    Optional<? extends Member> getOne(UUID userUUID);

}
