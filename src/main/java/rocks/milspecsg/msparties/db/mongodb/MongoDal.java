package rocks.milspecsg.msparties.db.mongodb;

import com.google.inject.Inject;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.db.BaseDal;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class MongoDal implements BaseDal {

    private final MongoContext mongoContext;

    @Inject
    public MongoDal(MongoContext mongoContext) {
        this.mongoContext = mongoContext;
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<Boolean> insertOneAsync(TOid item, String database, String collection) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                item.setUpdatedUtc(new Date());
//                mongoContext.mongoClient.getDatabase(database).getCollection(collection)
//                        .insertOne(item);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<List<TOid>> InsertManyAsync(List<TOid> items, String database, String collection) {
        return null;
    }

    public <TOid extends Dbo> CompletableFuture<TOid> GetOneAsync(Bson filter, String database, String collection) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document result = mongoContext.mongoClient.getDatabase(database).getCollection(collection).find(filter).first();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<TOid> GetOneAsync(Predicate<TOid> predicate, String database, String collection) {
        return null;
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<TOid> GetOneAsync(ObjectId id, String database, String collection) {
        return null;
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<List<TOid>> GetAllAsync(Predicate<TOid> predicate, String database, String collection) {
        return null;
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<List<TOid>> GetManyAsync(List<ObjectId> ids, String database, String collection) {
        return null;
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<DeleteResult> DeleteOneAsync(Predicate<TOid> predicate, String database, String collection) {
        return null;
    }
}
