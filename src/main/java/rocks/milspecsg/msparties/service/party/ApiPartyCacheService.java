package rocks.milspecsg.msparties.service.party;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.scheduler.Task;
import rocks.milspecsg.msparties.ConfigKeys;
import rocks.milspecsg.msparties.MSParties;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.party.PartyCacheService;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
public class ApiPartyCacheService implements PartyCacheService {

    private ConfigurationService configurationService;

    private Map<Party, Long> partyCache;

    @Inject
    public ApiPartyCacheService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
        partyCache = new HashMap<>();
        Integer interval = configurationService.getConfigInteger(ConfigKeys.CACHE_INVALIDATION_INTERVAL_SECONDS_INT);
        Task.builder().interval(interval, TimeUnit.SECONDS).execute(task).submit(MSParties.plugin);
    }

    /**
     * Cache invalidation task
     */
    private Runnable task = () -> {
        Integer timeoutSeconds = configurationService.getConfigInteger(ConfigKeys.CACHE_INVALIDATION_TIMOUT_SECONDS_INT);

        List<Party> toRemove = new ArrayList<>();
        for (Party party : getParties()) {
            if (System.currentTimeMillis() - partyCache.get(party) > timeoutSeconds * 1000L) {
                // if time has gone over limit
                toRemove.add(party);
            }
        }
        // remove from map
        toRemove.forEach(this::removeParty);
    };

    @Override
    public Map<Party, Long> getPartiesMap() {
        return this.partyCache;
    }

    @Override
    public Set<Party> getParties() {
        return this.partyCache.keySet();
    }

    @Override
    public void addParty(Party party) {
        this.partyCache.put(party, System.currentTimeMillis());
    }

    @Override
    public void removeParty(Party party) {
        this.partyCache.remove(party);
    }
}
