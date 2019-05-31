package rocks.milspecsg.msparties.api.member;

import rocks.milspecsg.msparties.api.RepositoryCacheService;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.Optional;
import java.util.UUID;

public interface MemberCacheService<M extends Member> extends RepositoryCacheService<M> {

    Optional<M> getOne(UUID userUUID);

}
