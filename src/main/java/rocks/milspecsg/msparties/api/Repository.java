package rocks.milspecsg.msparties.api;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import rocks.milspecsg.msparties.model.Dbo;
import rocks.milspecsg.msparties.model.ObjectWithId;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface Repository<T extends ObjectWithId<ObjectId>> {

    // <T extends ObjectWithId<ObjectId, Date>>
     CompletableFuture<Optional<T>> insertOneAsync(T item);

     CompletableFuture<T> getOneAsync(ObjectId id);

    Query<T> asQuery();

}