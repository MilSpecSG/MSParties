package rocks.milspecsg.msparties.api.party;

import org.bson.types.ObjectId;

public interface PermissionCacheService {

    String INVITE = "invite";
    String EDIT_PRIVACY = "edit_privacy";
    String EDIT_NAME = "edit_name";
    String EDIT_TAG = "edit_tag";
    String EDIT_MAX_SIZE = "edit_max_size";
    String EDIT_RANKS = "edit_ranks";
    String EDIT_PERMISSIONS = "edit_permissions";
    String ASSIGN_RANKS = "assign_ranks";
    String KICK = "kick";
    String BAN = "ban";

    void setPermission(ObjectId id, String permission, Integer rank);

    boolean checkSet(ObjectId id);

    boolean checkSet(ObjectId id, String permission);

    boolean check(ObjectId id, String permission, Integer rank);

}