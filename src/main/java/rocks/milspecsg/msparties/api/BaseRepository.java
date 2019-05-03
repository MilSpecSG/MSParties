package rocks.milspecsg.msparties.api;

import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface BaseRepository {

    // <TOid extends ObjectWithId<ObjectId, Date>>
    <TOid extends Dbo> CompletableFuture<TOid> InsertOneAsync(TOid item);

    <TOid extends Dbo> CompletableFuture<List<TOid>> InsertManyAsync(List<TOid> items);

    <TOid extends Dbo> CompletableFuture<TOid> GetOneAsync(Predicate<TOid> predicate);

    <TOid extends Dbo> CompletableFuture<TOid> GetOneAsync(ObjectId id);

    <TOid extends Dbo> CompletableFuture<List<TOid>> GetAllAsync(Predicate<TOid> predicate);

    <TOid extends Dbo> CompletableFuture<List<TOid>> GetManyAsync(List<ObjectId> ids);

}