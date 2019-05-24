package rocks.milspecsg.msparties.service;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msparties.ConfigKeys;
import rocks.milspecsg.msparties.MSParties;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.Repository;
import rocks.milspecsg.msparties.api.RepositoryCacheService;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.model.Dbo;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ApiRepositoryCacheService<T extends Dbo> implements RepositoryCacheService<T> {

    protected ConfigurationService configurationService;

    Map<T, Long> cache;

    @Inject
    public ApiRepositoryCacheService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
        cache = new HashMap<>();
        Integer interval = configurationService.getConfigInteger(ConfigKeys.CACHE_INVALIDATION_INTERVAL_SECONDS_INT);
        Task.builder().interval(interval, TimeUnit.SECONDS).execute(getCacheInvalidationTask()).submit(MSParties.plugin);
    }

    /**
     * Cache invalidation task
     */
    @Override
    public Runnable getCacheInvalidationTask() {
        return () -> {
            Integer timeoutSeconds = configurationService.getConfigInteger(ConfigKeys.CACHE_INVALIDATION_TIMOUT_SECONDS_INT);

            List<T> toRemove = new ArrayList<>();
            for (T t : getAll()) {
                if (System.currentTimeMillis() - cache.get(t) > timeoutSeconds * 1000L) {
                    // if time has gone over limit
                    toRemove.add(t);
                    Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Removing ", t));
                }
            }
            // remove from map
            toRemove.forEach(this::removeParty);
        };
    }

    @Override
    public Map<? extends T, Long> getMap() {
        return null;
    }

    @Override
    public Set<? extends T> getAll() {
        return cache.keySet();
    }

    @Override
    public Optional<? extends T> put(T t) {
        if (t == null) return Optional.empty();
        cache.put(t, System.currentTimeMillis());
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Saved ", t));
        return Optional.of(t);
    }

    @Override
    public void removeParty(T t) {
        cache.remove(t);
    }

    @Override
    public List<? extends T> getAll(Predicate<? super T> predicate) {
        return getAll().stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public Optional<? extends T> getOne(Predicate<? super T> predicate) {
        return Repository.single(getAll(predicate));
    }

    @Override
    public Optional<? extends T> getOne(ObjectId id) {
        return getOne(party -> party.getId().equals(id));
    }
}
