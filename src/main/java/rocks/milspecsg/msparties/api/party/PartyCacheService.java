package rocks.milspecsg.msparties.api.party;

import rocks.milspecsg.msparties.model.core.Party;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PartyCacheService {

    /**
     * @return All a map of parties and the time in millis since last update in the cache
     */
    Map<Party, Long> getPartiesMap();

    /**
     * @return A set containing all parties in the cache
     */
    Set<Party> getParties();

    /**
     * Add a {@link Party} to the cache
     * @param party {@link Party} to add to cache
     */
    void addParty(Party party);

    /**
     * Removes a {@link Party} from the cache
     * @param party {@link Party} to remove from cache
     */
    void removeParty(Party party);
}
