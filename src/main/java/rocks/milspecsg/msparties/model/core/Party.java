package rocks.milspecsg.msparties.model.core;


import org.mongodb.morphia.annotations.Entity;
import rocks.milspecsg.msparties.model.Dbo;
import rocks.milspecsg.msparties.model.exceptions.InvalidMaxSizeException;
import rocks.milspecsg.msparties.model.exceptions.InvalidNameException;
import rocks.milspecsg.msparties.model.exceptions.NotInPartyException;

import java.util.List;
import java.util.function.Predicate;

@Entity("parties")
public class Party extends Dbo {

    private String name;
    private int maxSize;
    private List<Member> members;
    private List<Rank> ranks;

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidNameException {
        // TODO: check for profanity
        this.name = name;
    }

    
    public int getMaxSize() {
        return 0;
    }

    public void setMaxSize(int maxSize) throws InvalidMaxSizeException {
        if (maxSize < 1)
            throw new InvalidMaxSizeException();
        this.maxSize = maxSize;
    }

    
    public Member getLeader() {
        return members.get(0);
    }

    
    public void setLeader(Member leader) throws NotInPartyException {
        Member oldLeader = getLeader();
    }

    public void setLeader(Predicate<Member> leader) throws NotInPartyException {
        Member newLeader = getMember(leader);

    }

    public Member getMember(Predicate<Member> member) throws NotInPartyException {
        for (Member m : members) {
            if (member.test(m)) return m;
        }
        throw new NotInPartyException();
    }

    public Member getNewestMember() {
        return null;
    }

    public Member getOldestMember() {
        return null;
    }

    public List<Member> getMembers() {
        return null;
    }

    public List<Member> getMembersAtRank(int index) {
        return null;
    }

    public List<Rank> getRanks() {
        return null;
    }
}
