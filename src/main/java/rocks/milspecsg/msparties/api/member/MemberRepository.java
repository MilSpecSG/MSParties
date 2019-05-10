package rocks.milspecsg.msparties.api.member;

import org.mongodb.morphia.query.Query;
import org.spongepowered.api.entity.living.player.User;
import rocks.milspecsg.msparties.api.Repository;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.results.UpdateResult;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MemberRepository extends Repository<Member> {

//    /**
//     * @param kd to get corresponding {@code User}
//     * @return a {@code User} that corresponds to this kd
//     */
    //CompletableFuture<Optional<User>> getUser(Predicate<Member> kd);

    /**
     *
     * Gets the corresponding {@code Member} from the database.
     * If not present, creates a new one and saves it to the database
     *
     * @param userUUID Mojang issued {@code uuid} of {@code User} to get corresponding {@code Member}
     * @return a ready-to-use {@code Member} that corresponds with the given {@code uuid}
     */
    CompletableFuture<Optional<? extends Member>> getMember(UUID userUUID);

    Optional<User> getUser(UUID uuid);

    Optional<User> getUser(String lastKnownName);



    Query<Member> asQuery(UUID userUUID);

}
