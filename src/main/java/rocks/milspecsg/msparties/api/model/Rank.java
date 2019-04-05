package rocks.milspecsg.msparties.api.model;

import java.util.List;

public interface Rank {


    int getIndex(); // TODO: find better name

    String getName();

    /**
     *
     * @return Members that have this rank (only in this party)
     */
    List<Member> getMembers();

}
