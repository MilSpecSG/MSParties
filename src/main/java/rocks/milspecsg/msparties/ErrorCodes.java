package rocks.milspecsg.msparties;

public abstract class ErrorCodes {

    public static final int ERROR_GETTING_MEMBER_ID = 480;
    public static final int ERROR_GETTING_LEADER_ID = 481;

    public static final int ERROR_INSERTING_INTO_DB = 500;
    public static final int ERROR_GETTING_FROM_DB = 501;
    public static final int ERROR_DELETING_FROM_DB = 502;




    public static String getMessage(int errorCode) {
        return "Error " + errorCode;
    }
}
