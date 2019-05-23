package rocks.milspecsg.msparties.service.party;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bson.types.ObjectId;
import rocks.milspecsg.msparties.api.party.PermissionCacheService;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class ApiPermissionCacheService implements PermissionCacheService {


    Map<ObjectId, Map<String, Integer>> permissionCache;

    @Inject
    public ApiPermissionCacheService() {
        this.permissionCache = new HashMap<>();
    }

    @Override
    public void setPermission(ObjectId id, String permission, Integer rank) {
        
    }

    @Override
    public boolean checkSet(ObjectId id) {
        return false;
    }

    @Override
    public boolean checkSet(ObjectId id, String permission) {
        return false;
    }

    @Override
    public boolean check(ObjectId id, String permission, Integer rank) {
        return false;
    }
}
