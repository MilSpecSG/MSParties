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

    CompletableFuture<Optional<? extends T>> insertOne(T item);

    CompletableFuture<Optional<? extends T>> getOne(ObjectId id);

    UpdateOperations<T> createUpdateOperations();

    UpdateOperations<T> inc(String field, Number value);

    UpdateOperations<T> inc(String field);

    Query<T> asQuery();

    Query<T> asQuery(ObjectId id);

    static <U> Optional<? extends U> single(List<? extends U> list) {
        return list.size() > 0 ? Optional.ofNullable(list.get(0)) : Optional.empty();
    }

    static <U> Function<List<? extends U>, Optional<? extends U>> single() {
        return Repository::single;
    }

    <R extends RepositoryCacheService<T>> Supplier<Optional<? extends T>> ifNotPresent(Supplier<R> repositoryCacheServiceSupplier, Function<R, Optional<? extends T>> fromCache, Supplier<Optional<? extends T>> fromDB);

    Supplier<Optional<? extends T>> ifNotPresent(RepositoryCacheService<T> repositoryCacheService, ObjectId id);

}