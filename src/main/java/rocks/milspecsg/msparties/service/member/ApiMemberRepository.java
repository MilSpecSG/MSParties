package rocks.milspecsg.msparties.service.member;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import rocks.milspecsg.msparties.api.member.MemberCacheService;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.Dbo;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.service.ApiRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class ApiMemberRepository<M extends Member> extends ApiRepository<M> implements MemberRepository<M> {

    protected MemberCacheService<M> memberCacheService;

    @Inject
    public ApiMemberRepository(MongoContext mongoContext, MemberCacheService<M> memberCacheService) {
        super(mongoContext);
        this.memberCacheService = memberCacheService;
    }


    @Override
    public CompletableFuture<Optional<M>> getOneOrGenerate(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<M> optionalMember = getOne(userUUID).join();
            if (optionalMember.isPresent()) return optionalMember;
            // if there isn't one already, create a new one
            M member = generateEmpty();
            member.userUUID = userUUID;
            return insertOne(member).join();
        });
    }

    @Override
    public CompletableFuture<Optional<M>> getOne(ObjectId id) {
        return CompletableFuture.supplyAsync(ifNotPresent(memberCacheService, id));
    }

    @Override
    public CompletableFuture<Optional<M>> getOne(UUID userUUID) {
        return CompletableFuture.supplyAsync(ifNotPresent(memberCacheService, service -> service.getOne(userUUID), () -> Optional.ofNullable(asQuery(userUUID).get())));
    }

    @Override
    public Optional<User> getUser(UUID uuid) {
        return Sponge.getServiceManager().provide(UserStorageService.class).flatMap(u -> u.get(uuid));
    }

    @Override
    public Optional<User> getUser(String lastKnownName) {
        return Sponge.getServiceManager().provide(UserStorageService.class).flatMap(u -> u.get(lastKnownName));
    }

    @Override
    public CompletableFuture<Optional<ObjectId>> getId(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getOneOrGenerate(uuid).join().map(Dbo::getId));
    }

    @Override
    public CompletableFuture<Optional<UUID>> getUUID(ObjectId id) {
        return CompletableFuture.supplyAsync(() -> getOne(id).join().map(member -> member.userUUID));
    }

    @Override
    public CompletableFuture<Optional<User>> getUser(ObjectId id) {
        return CompletableFuture.supplyAsync(() -> getUUID(id).join().flatMap(this::getUser));
    }

    @Override
    public Query<M> asQuery(UUID userUUID) {
        return asQuery().field("userUUID").equal(userUUID);
    }
}
