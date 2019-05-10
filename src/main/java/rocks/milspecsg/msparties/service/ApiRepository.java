package rocks.milspecsg.msparties.service;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msparties.api.Repository;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.ObjectWithId;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @param <T>
 * @since 0.1.0
 */
public abstract class ApiRepository<T extends ObjectWithId<ObjectId>> implements Repository<T> {

    protected MongoContext mongoContext;

    @Inject
    public ApiRepository(MongoContext mongoContext) {
        this.mongoContext = mongoContext;
    }

    @Override
    public CompletableFuture<Optional<? extends T>> insertOneAsync(T item) {
        return CompletableFuture.supplyAsync(() -> {
            Key<T> key = mongoContext.datastore.save(item);
            item.setId((ObjectId) key.getId());
            return Optional.of(item);
        });
    }

    @Override
    public UpdateOperations<T> inc(String field, Number value) {
        return createUpdateOperations().inc(field, value);
    }

    @Override
    public UpdateOperations<T> inc(String field) {
        return inc(field, 1);
    }
}
