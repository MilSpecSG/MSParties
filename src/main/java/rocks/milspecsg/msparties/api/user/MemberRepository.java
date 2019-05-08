package rocks.milspecsg.msparties.api.user;

import org.spongepowered.api.entity.living.player.User;
import rocks.milspecsg.msparties.api.Repository;
import rocks.milspecsg.msparties.model.core.Member;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface MemberRepository extends Repository<Member> {

    /**
     * @param member to get corresponding {@code User}
     * @return a {@code User} that corresponds to this member
     */
    CompletableFuture<Optional<User>> getUser(Predicate<Member> member);


    /**
     *
     * Gets the corresponding {@code Member} from the database.
     * If not present, creates a new one and saves it to the database
     *
     * @param uuid of user to get corresponding {@code Member}
     * @return a ready-to-use {@code Member} that corresponds with the given {@code uuid}
     */
    CompletableFuture<Optional<Member>> getMember(UUID uuid);

}
