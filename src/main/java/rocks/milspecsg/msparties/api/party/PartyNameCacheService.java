package rocks.milspecsg.msparties.api.party;

import java.util.List;

public interface PartyNameCacheService {

    /**
     * @return All party names in the cache
     */
    List<String> getPartyNames();

    /**
     * Add a party name to the cache
     * @param name Party name to add
     */
    void addPartyName(String name);

    /**
     * If {@code oldName} is not present in the cache, {@code newName} will be added to the cache.
     * Otherwise, rename
     *
     * @param oldName Party name to remove
     * @param newName Party mame to add
     */
    void setPartyName(String oldName, String newName);

    /**
     * Removes a party name from the cache
     * @param name Party name to remove
     */
    void removePartyName(String name);
}
