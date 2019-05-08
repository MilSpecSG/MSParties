package rocks.milspecsg.msparties.db.mongodb;

import com.google.inject.Inject;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import rocks.milspecsg.msparties.db.BaseDal;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

// probably wont need this class. May delete later
public class MongoDal implements BaseDal {

    private final MongoContext mongoContext;

    @Inject
    public MongoDal(MongoContext mongoContext) {
        this.mongoContext = mongoContext;
    }

    @Override
    public <T extends Dbo> CompletableFuture<Optional<T>> insertOneAsync(T item, String database, String collection) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                mongoContext.datastore.save(item);
                return Optional.of(item);
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }

    @Override
    public <T extends Dbo> CompletableFuture<List<T>> InsertManyAsync(List<T> items, String database, String collection) {
        return null;
    }

    public <T extends Dbo> CompletableFuture<T> GetOneAsync(Bson filter, String database, String collection) {
        return CompletableFuture.supplyAsync(() -> {
            try {


                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    @Override
    public <T extends Dbo> CompletableFuture<Optional<T>> GetOneAsync(Predicate<T> predicate, String database, String collection) {
        return CompletableFuture.supplyAsync(Optional::empty);
    }

    @Override
    public <T extends Dbo> CompletableFuture<Optional<T>> GetOneAsync(ObjectId id, String database, String collection) {
        return CompletableFuture.supplyAsync(Optional::empty);
    }

    @Override
    public <T extends Dbo> CompletableFuture<List<T>> GetAllAsync(Predicate<T> predicate, String database, String collection) {
        return null;
    }

    @Override
    public <T extends Dbo> CompletableFuture<List<T>> GetManyAsync(List<ObjectId> ids, String database, String collection) {
        return null;
    }

    @Override
    public <T extends Dbo> CompletableFuture<DeleteResult> DeleteOneAsync(Predicate<T> predicate, String database, String collection) {
        return null;
    }
}
