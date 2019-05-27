package rocks.milspecsg.msparties.api;

import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public interface CacheInvalidationService<T> {


    Runnable getCacheInvalidationTask();

    /**
     * Add a {@link T} to the cache
     *
     * @param t {@link T} to put to cache
     * @return {@link Optional} with inserted {@link T} if successfully inserted.
     * Otherwise {@link Optional#empty()}
     */
    Optional<? extends T> put(T t);

    List<? extends T> put(List<? extends T> list);

    /**
     * @return A set containing all parties in the cache
     */
    Set<? extends T> getAll();


    /**
     * Removes a {@link T} from the cache
     *
     * @param t {@link T} to remove from cache
     */
    void remove(T t);

    void remove(Predicate<? super T> predicate);

    List<? extends T> getAll(Predicate<? super T> predicate);

    Optional<? extends T> getOne(Predicate<? super T> predicate);
}
