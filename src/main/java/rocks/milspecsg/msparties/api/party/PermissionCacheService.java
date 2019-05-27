package rocks.milspecsg.msparties.api.party;

import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.model.core.*;

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
    String TPAALL = "tpaall";

    /**
     * @param id         {@link ObjectId} id of {@link Party} to set
     * @param permission {@link String} permission value to set
     * @param rankIndex  {@link Integer} corresponds to {@link Rank#index}.
     *                   All ranks at or above this index will have the permission
     */
    void setPermission(ObjectId id, String permission, Integer rankIndex);

    /**
     * Will remove the matching {@link Party}'s permissions from the cache
     *
     * @param id {@link ObjectId} id of {@link Party} to clear
     */
    void clear(ObjectId id);

    /**
     * @param id {@link ObjectId} id of {@link Party} to check
     * @return Whether this permissions of this {@link Party} have been saved in the cache
     */
    boolean checkSet(ObjectId id);

    /**
     * @param id         {@link ObjectId} id of {@link Party} to check
     * @param permission {@link String} permission value to check
     * @return Whether this permission node has been set in the cache
     */
    boolean checkSet(ObjectId id, String permission);

    /**
     * @param id         {@link ObjectId} id of {@link Party} to check
     * @param permission {@link String} permission value to check
     * @param rankIndex  {@link Integer} rank to check
     * @return Whether the given rank has access to the given permission.
     * <p>
     * All ranks at or above the saved permission index will have the permission
     * </p>
     */
    boolean check(ObjectId id, String permission, Integer rankIndex);

    /**
     * @param id         {@link ObjectId} id of {@link Party} to check
     * @param permission {@link String} permission value to check
     * @return {@link Integer} rankIndex of the specified permission and party
     *
     * <p>
     * Precondition: id and permission must exist in the cache,
     * </p>
     *
     * @see #checkSet(ObjectId, String)
     */
    Integer getRankIndex(ObjectId id, String permission);

}