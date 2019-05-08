package rocks.milspecsg.msparties.service.user;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import rocks.milspecsg.msparties.api.user.MemberRepository;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.service.ApiRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class ApiMemberRepository extends ApiRepository<Member> implements MemberRepository {

    @Inject
    public ApiMemberRepository(MongoContext mongoContext) {
        super(mongoContext);
    }

    @Override
    public CompletableFuture<Optional<User>> getUser(Predicate<Member> member) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<Member>> getMember(UUID uuid) {
        return null;
    }

    @Override
    public CompletableFuture<Member> getOneAsync(ObjectId id) {
        return null;
    }

    @Override
    public Query<Member> asQuery() {
        return mongoContext.datastore.createQuery(Member.class);
    }
}
