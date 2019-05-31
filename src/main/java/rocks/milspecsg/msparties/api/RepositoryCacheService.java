package rocks.milspecsg.msparties.api;

import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.model.Dbo;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public interface RepositoryCacheService<T extends Dbo> extends CacheInvalidationService<T> {

    Optional<T> getOne(ObjectId id);

}
