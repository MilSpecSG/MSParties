package rocks.milspecsg.msparties.service;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.Repository;
import rocks.milspecsg.msparties.api.RepositoryCacheService;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ApiRepository<T extends Dbo> implements Repository<T> {

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

    @Override
    public <R extends RepositoryCacheService<T>> Supplier<Optional<? extends T>> ifNotPresent(Supplier<R> repositoryCacheServiceSupplier, Function<R, Optional<? extends T>> fromCache, Supplier<Optional<? extends T>> fromDB) {
        R repositoryCacheService = repositoryCacheServiceSupplier.get();
        Optional<? extends T> main = fromCache.apply(repositoryCacheService);
        if (main.isPresent()) {
            Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Found in cache"));
            return () -> main;
        } else {
            Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Not present in cache, saving"));
            return () -> fromDB.get().flatMap(repositoryCacheService::put);
        }
    }

    @Override
    public Supplier<Optional<? extends T>> ifNotPresent(RepositoryCacheService<T> repositoryCacheService, ObjectId id) {
        return ifNotPresent(() -> repositoryCacheService, service -> service.getOne(id), () -> Optional.ofNullable(asQuery(id).get()));
    }

}
