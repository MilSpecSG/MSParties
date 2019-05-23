package rocks.milspecsg.msparties.service.member;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.service.ApiRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ApiMemberRepository extends ApiRepository<Member> implements MemberRepository {

    @Inject
    public ApiMemberRepository(MongoContext mongoContext) {
        super(mongoContext);
    }

    @Override
    public CompletableFuture<Optional<? extends Member>> getOneOrGenerate(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> {
            // try to find one in the database
            List<Member> members = asQuery(userUUID).asList();
            if (members != null && members.size() > 0 && members.get(0) != null) return Optional.of(members.get(0));

            // if there isn't one already, create a new one
            Member member = new Member();
            member.userUUID = userUUID;
            return insertOne(member).join();
        });
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
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(asQuery(uuid).project("_id", true).get().getId()));
    }

    @Override
    public CompletableFuture<Optional<UUID>> getUUID(ObjectId id) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(asQuery(id).project("userUUID", true).get().userUUID));
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
