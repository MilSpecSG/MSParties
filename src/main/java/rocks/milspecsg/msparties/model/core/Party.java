package rocks.milspecsg.msparties.model.core;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import rocks.milspecsg.msparties.model.Dbo;
import rocks.milspecsg.msparties.model.exceptions.InvalidMaxSizeException;
import rocks.milspecsg.msparties.model.exceptions.InvalidNameException;
import rocks.milspecsg.msparties.model.exceptions.NotInPartyException;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Entity("parties")
public class Party extends Dbo {

    public String name;
    public int maxSize;
    public UUID leaderUUID;
    public List<UUID> memberUUIDs; //todo: check if array is better
    public List<Rank> ranks;
    public boolean inviteOnly;
}