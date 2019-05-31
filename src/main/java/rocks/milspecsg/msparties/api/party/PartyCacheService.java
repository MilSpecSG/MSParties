package rocks.milspecsg.msparties.api.party;

import rocks.milspecsg.msparties.api.RepositoryCacheService;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.Optional;

public interface PartyCacheService<T extends Party> extends RepositoryCacheService<T> {

    Optional<T> getOne(String name);

    void remove(String name);

}
