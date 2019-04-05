package rocks.milspecsg.msparties.api.model;


import java.util.List;

public interface Party {

    String getName();

    Member getLeader();

    Member getNewestMember();

    Member getOldestMember();

    /**
     *
     * @return All members of the clan (includes leader)
     */
    List<Member> getMembers();

    List<Rank> getRanks();







}
