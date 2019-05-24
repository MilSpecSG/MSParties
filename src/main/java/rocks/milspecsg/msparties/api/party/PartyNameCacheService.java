package rocks.milspecsg.msparties.api.party;

import java.util.List;
import java.util.function.Function;

import org.spongepowered.api.command.CommandSource;
import rocks.milspecsg.msparties.model.core.Party;


public interface PartyNameCacheService {

    /**
     * @return All {@link Party} names in the cache
     */
    List<String> getPartyNames();

    /**
     * Add a {@link Party} name to the cache
     * @param name {@link Party} name to put
     */
    void addPartyName(String name);

    /**
     * If {@code oldName} is not present in the cache, {@code newName} will be added to the cache.
     * Otherwise, rename
     *
     * Same as calling {@link #removePartyName(String)} and then {@link #addPartyName(String)}
     *
     * @param oldName {@link Party} name to remove
     * @param newName {@link Party} mame to put
     */
    void setPartyName(String oldName, String newName);

    /**
     * Removes a {@link Party} name from the cache
     * @param name Party name to remove
     */
    void removePartyName(String name);


    Iterable<String> getSuggestions(CommandSource commandSource);
}
