package rocks.milspecsg.msparties.model.misc;

import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.UUID;

public class TeleportationRequest {

    public UUID teleportingPlayer;
    public UUID targetPlayer;
    public ObjectId targetPartyId;
    public String message;

}
