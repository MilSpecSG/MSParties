package rocks.milspecsg.msparties.model.core;



import org.mongodb.morphia.annotations.Entity;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Entity("members")
public class Member extends Dbo {

    private UUID userUUID;

    /**
     * @see Rank#getIndex()
     * 0 is default
     */
    private int rankIndex;


    public Optional<User> getUser() {
        return Sponge.getServiceManager().provide(UserStorageService.class).flatMap(s -> s.get(userUUID));
    }
}
