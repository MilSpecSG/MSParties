package rocks.milspecsg.msparties.db;

import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface BaseDal {
    <TOid extends Dbo> CompletableFuture<Boolean> insertOneAsync(TOid item, String database, String collection);

    <TOid extends Dbo> CompletableFuture<List<TOid>> InsertManyAsync(List<TOid> items, String database, String collection);

    <TOid extends Dbo> CompletableFuture<TOid> GetOneAsync(Predicate<TOid> predicate, String database, String collection);

    <TOid extends Dbo> CompletableFuture<TOid> GetOneAsync(ObjectId id, String database, String collection);

    <TOid extends Dbo> CompletableFuture<List<TOid>> GetAllAsync(Predicate<TOid> predicate, String database, String collection);

    <TOid extends Dbo> CompletableFuture<List<TOid>> GetManyAsync(List<ObjectId> ids, String database, String collection);

    <TOid extends Dbo> CompletableFuture<DeleteResult> DeleteOneAsync(Predicate<TOid> predicate, String database, String collection);
}
