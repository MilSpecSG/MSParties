package rocks.milspecsg.msparties.api.model;



import java.util.Calendar;

public interface Member<TUser> {

    TUser getUser();

    Rank getRank();

    Calendar getJoinDate();




}
