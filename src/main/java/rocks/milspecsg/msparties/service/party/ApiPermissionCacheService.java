package rocks.milspecsg.msparties.service.party;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.api.party.PermissionCacheService;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class ApiPermissionCacheService implements PermissionCacheService {


    protected Map<ObjectId, Map<String, Integer>> permissionCache;

    @Inject
    public ApiPermissionCacheService() {
        this.permissionCache = new HashMap<>();
    }

    @Override
    public void setPermission(ObjectId id, String permission, Integer rankIndex) {
        if (checkSet(id)) {
            permissionCache.get(id).put(permission, rankIndex);
        } else {
            permissionCache.put(id, new HashMap<String, Integer>() {{
                put(permission, rankIndex);
            }});
        }
    }

    @Override
    public void clear(ObjectId id) {
        permissionCache.remove(id);
    }

    @Override
    public boolean checkSet(ObjectId id) {
        return permissionCache.containsKey(id) && permissionCache.get(id) != null;
    }

    @Override
    public boolean checkSet(ObjectId id, String permission) {
        return checkSet(id) && permissionCache.get(id).containsKey(permission);
    }

    @Override
    public boolean check(ObjectId id, String permission, Integer rankIndex) {
        return checkSet(id, permission) && getRankIndex(id, permission) <= rankIndex;
    }

    @Override
    public Integer getRankIndex(ObjectId id, String permission) {
        return permissionCache.get(id).get(permission);
    }
}
