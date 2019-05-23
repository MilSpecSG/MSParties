package rocks.milspecsg.msparties.api;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msparties.model.ObjectWithId;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Repository<T extends ObjectWithId<ObjectId>> {

    CompletableFuture<Optional<? extends T>> insertOne(T item);

    CompletableFuture<Optional<? extends T>> getOne(ObjectId id);

    UpdateOperations<T> createUpdateOperations();

    UpdateOperations<T> inc(String field, Number value);

    UpdateOperations<T> inc(String field);

    Query<T> asQuery();

    Query<T> asQuery(ObjectId id);
}