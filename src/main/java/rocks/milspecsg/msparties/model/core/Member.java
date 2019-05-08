package rocks.milspecsg.msparties.model.core;

import org.mongodb.morphia.annotations.Entity;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.UUID;

@Entity("members")
public class Member extends Dbo {

    public UUID userUUID;

    /**
     * @see Rank#getIndex()
     * 0 is default
     */
    public int rankIndex;


    public int playerKills;
    public int playerDeaths;
    public int mobKills;
    public int mobDeaths;
    public int totalKills;
    public int totalDeaths;



}
