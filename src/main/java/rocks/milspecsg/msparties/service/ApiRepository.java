package rocks.milspecsg.msparties.service;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msparties.api.Repository;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.ObjectWithId;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class ApiRepository<T extends ObjectWithId<ObjectId>> implements Repository<T> {

    protected MongoContext mongoContext;

    @Inject
    public ApiRepository(MongoContext mongoContext) {
        this.mongoContext = mongoContext;
    }

    @Override
    public CompletableFuture<Optional<? extends T>> insertOne(T item) {
        return CompletableFuture.supplyAsync(() -> {
            Key<T> key = mongoContext.datastore.save(item);
            item.setId((ObjectId) key.getId());
            return Optional.of(item);
        });
    }

    @Override
    public CompletableFuture<Optional<? extends T>> getOne(ObjectId id) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(asQuery(id).get()));
    }

    @Override
    public UpdateOperations<T> inc(String field, Number value) {
        return createUpdateOperations().inc(field, value);
    }

    @Override
    public UpdateOperations<T> inc(String field) {
        return inc(field, 1);
    }

    @Override
    public Query<T> asQuery(ObjectId id) {
        return asQuery().field("id").equal(id);
    }
}
