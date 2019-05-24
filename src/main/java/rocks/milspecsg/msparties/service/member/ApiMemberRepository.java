package rocks.milspecsg.msparties.service.member;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.member.MemberCacheService;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.Dbo;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.service.ApiRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ApiMemberRepository extends ApiRepository<Member> implements MemberRepository {

    protected MemberCacheService memberCacheService;

    @Inject
    public ApiMemberRepository(MongoContext mongoContext, MemberCacheService memberCacheService) {
        super(mongoContext);
        this.memberCacheService = memberCacheService;
    }

    @Override
    public CompletableFuture<Optional<? extends Member>> getOneOrGenerate(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<? extends Member> optionalMember = getOne(userUUID).join();
            if (optionalMember.isPresent()) return optionalMember;
            // if there isn't one already, create a new one
            Member member = new Member();
            member.userUUID = userUUID;
            return insertOne(member).join();
        });
    }

    @Override
    public CompletableFuture<Optional<? extends Member>> getOne(ObjectId id) {
        return CompletableFuture.supplyAsync(ifNotPresent(memberCacheService, id));
    }

    @Override
    public CompletableFuture<Optional<? extends Member>> getOne(UUID userUUID) {
        return CompletableFuture.supplyAsync(ifNotPresent(() -> memberCacheService, service -> service.getOne(userUUID), () -> Optional.ofNullable(asQuery(userUUID).get())));
    }

    @Override
    public UpdateOperations<Member> createUpdateOperations() {
        return mongoContext.datastore.createUpdateOperations(Member.class);
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
        //Optional.ofNullable(asQuery(id).project("userUUID", true).get().userUUID);
    }

    @Override
    public Query<Member> asQuery() {
        return mongoContext.datastore.createQuery(Member.class);
    }

    @Override
    public Query<Member> asQuery(UUID userUUID) {
        return asQuery().field("userUUID").equal(userUUID);
    }
}
