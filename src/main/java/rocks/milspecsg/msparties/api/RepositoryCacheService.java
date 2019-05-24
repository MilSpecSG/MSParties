package rocks.milspecsg.msparties.api;

import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.model.Dbo;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public interface RepositoryCacheService<T extends Dbo> {


    Runnable getCacheInvalidationTask();

    /**
     * @return All a map of parties and the time in millis since last update in the cache
     */
    Map<? extends T, Long> getMap();

    /**
     * @return A set containing all parties in the cache
     */
    Set<? extends T> getAll();

    /**
     * Add a {@link T} to the cache
     *
     * @param t {@link T} to put to cache
     * @return {@link Optional} with inserted {@link Party} if successfully inserted.
     * Otherwise {@link Optional#empty()}
     */
    Optional<? extends T> put(T t);

    /**
     * Removes a {@link T} from the cache
     *
     * @param t {@link T} to remove from cache
     */
    void removeParty(T t);

    List<? extends T> getAll(Predicate<? super T> predicate);

    Optional<? extends T> getOne(Predicate<? super T> predicate);

    Optional<? extends T> getOne(ObjectId id);
}
