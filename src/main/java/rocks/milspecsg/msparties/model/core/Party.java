package rocks.milspecsg.msparties.model.core;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.model.Dbo;
import rocks.milspecsg.msparties.model.exceptions.InvalidMaxSizeException;
import rocks.milspecsg.msparties.model.exceptions.InvalidNameException;
import rocks.milspecsg.msparties.model.exceptions.NotInPartyException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

@Entity("parties")
public class Party extends Dbo {

    public String name;
    public String tag;
    public int maxSize;
    public UUID leaderUUID;
    public Map<ObjectId, Integer> memberRankMap;
    public List<Rank> ranks;
    public boolean inviteOnly;
    public Map<String, Integer> permissionMap;

    public Text getTitle() {
        return Text.of(TextColors.GOLD, name, " - ", TextColors.GOLD, "[", tag, TextColors.GOLD, "]");
    }
}