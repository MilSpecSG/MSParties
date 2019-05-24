package rocks.milspecsg.msparties.api.party;

import rocks.milspecsg.msparties.api.RepositoryCacheService;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.Optional;

public interface PartyCacheService extends RepositoryCacheService<Party> {

    Optional<? extends Party> getOne(String name);

}
