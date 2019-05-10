package rocks.milspecsg.msparties.api;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msparties.model.ObjectWithId;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Repository<T extends ObjectWithId<ObjectId>> {

    // <T extends ObjectWithId<ObjectId, Date>>
    CompletableFuture<Optional<? extends T>> insertOneAsync(T item);

    CompletableFuture<Optional<? extends T>> getOneAsync(ObjectId id);

    UpdateOperations<T> createUpdateOperations();

    UpdateOperations<T> inc(String field, Number value);

    UpdateOperations<T> inc(String field);

    Query<T> asQuery();

}