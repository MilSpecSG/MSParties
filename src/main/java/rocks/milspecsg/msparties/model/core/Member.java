package rocks.milspecsg.msparties.model.core;

import org.mongodb.morphia.annotations.Entity;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.UUID;

@Entity("members")
public class Member extends Dbo {

    public UUID userUUID;

}
