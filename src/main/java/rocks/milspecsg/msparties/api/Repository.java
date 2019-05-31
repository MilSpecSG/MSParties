package rocks.milspecsg.msparties.api;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Repository<T extends Dbo> {

    /**
     * @return An empty {@link T}
     */
    T generateDefault();

    CompletableFuture<Optional<T>> insertOne(T item);

    CompletableFuture<Optional<T>> getOne(ObjectId id);

    UpdateOperations<T> createUpdateOperations();

    UpdateOperations<T> inc(String field, Number value);

    UpdateOperations<T> inc(String field);

    Query<T> asQuery();

    Query<T> asQuery(ObjectId id);

    static <U> Optional<U> single(List<U> list) {
        return list.size() > 0 ? Optional.ofNullable(list.get(0)) : Optional.empty();
    }

    static <U> Function<List<U>, Optional<U>> single() {
        return Repository::single;
    }

    <R extends RepositoryCacheService<T>> Supplier<List<T>> saveToCache(R repositoryCacheService, Supplier<List<T>> fromDB);

    <R extends RepositoryCacheService<T>> Supplier<Optional<T>> ifNotPresent(R repositoryCacheService, Function<R, Optional<T>> fromCache, Supplier<Optional<T>> fromDB);

    Supplier<Optional<T>> ifNotPresent(RepositoryCacheService<T> repositoryCacheService, ObjectId id);

}