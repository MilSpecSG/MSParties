package rocks.milspecsg.msparties.model.core;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity("parties")
public class Party extends Dbo {

    public String name;
    public String tag;
    public int maxSize;
    public UUID leaderUUID;
    public Map<ObjectId, Integer> members;
    public List<Rank> ranks;
    public String privacy;
    public Map<String, Integer> permissions;

    public Text getTitle() {
        return Text.of(TextColors.GOLD, name, " - ", TextColors.GOLD, "[", tag, TextColors.GOLD, "]");
    }
}