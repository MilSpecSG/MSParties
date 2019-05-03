package rocks.milspecsg.msparties.service;

import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.api.BaseRepository;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class Repository implements BaseRepository {
    @Override
    public <TOid extends Dbo> CompletableFuture<TOid> InsertOneAsync(TOid item) {
        return null;
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<List<TOid>> InsertManyAsync(List<TOid> items) {
        return null;
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<TOid> GetOneAsync(Predicate<TOid> predicate) {
        return null;
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<TOid> GetOneAsync(ObjectId id) {
        return null;
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<List<TOid>> GetAllAsync(Predicate<TOid> predicate) {
        return null;
    }

    @Override
    public <TOid extends Dbo> CompletableFuture<List<TOid>> GetManyAsync(List<ObjectId> ids) {
        return null;
    }
}
