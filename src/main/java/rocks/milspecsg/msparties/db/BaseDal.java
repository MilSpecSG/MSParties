package rocks.milspecsg.msparties.db;

import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface BaseDal {
    <T extends Dbo> CompletableFuture<Optional<T>> insertOneAsync(T item, String database, String collection);

    <T extends Dbo> CompletableFuture<List<T>> InsertManyAsync(List<T> items, String database, String collection);

    <T extends Dbo> CompletableFuture<Optional<T>> GetOneAsync(Predicate<T> predicate, String database, String collection);

    <T extends Dbo> CompletableFuture<Optional<T>> GetOneAsync(ObjectId id, String database, String collection);

    <T extends Dbo> CompletableFuture<List<T>> GetAllAsync(Predicate<T> predicate, String database, String collection);

    <T extends Dbo> CompletableFuture<List<T>> GetManyAsync(List<ObjectId> ids, String database, String collection);

    <T extends Dbo> CompletableFuture<DeleteResult> DeleteOneAsync(Predicate<T> predicate, String database, String collection);
}
