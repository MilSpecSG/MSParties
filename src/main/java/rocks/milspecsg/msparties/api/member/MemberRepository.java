package rocks.milspecsg.msparties.api.member;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.spongepowered.api.entity.living.player.User;
import rocks.milspecsg.msparties.api.Repository;
import rocks.milspecsg.msparties.model.core.Member;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MemberRepository extends Repository<Member> {

    /**
     *
     * Gets the corresponding {@code Member} from the database.
     * If not present, creates a new one and saves it to the database
     *
     * @param userUUID Mojang issued {@code uuid} of {@code User} to getRequiredRankIndex corresponding {@code Member}
     * @return a ready-to-use {@code Member} that corresponds with the given {@code uuid}
     */
    CompletableFuture<Optional<? extends Member>> getOneOrGenerate(UUID userUUID);

    CompletableFuture<Optional<? extends Member>> getOne(UUID userUUID);


    Optional<User> getUser(UUID uuid);

    Optional<User> getUser(String lastKnownName);

    CompletableFuture<Optional<ObjectId>> getId(UUID uuid);

    CompletableFuture<Optional<UUID>> getUUID(ObjectId id);


    Query<Member> asQuery(UUID userUUID);

}
