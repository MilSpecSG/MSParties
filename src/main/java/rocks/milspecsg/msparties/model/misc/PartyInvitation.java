package rocks.milspecsg.msparties.model.misc;

import org.bson.types.ObjectId;

import java.util.UUID;

public class PartyInvitation {

    public ObjectId partyId;
    public UUID targetPlayer;
    public String message;

}
