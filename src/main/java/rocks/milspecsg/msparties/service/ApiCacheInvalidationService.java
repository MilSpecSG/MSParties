package rocks.milspecsg.msparties.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msparties.ConfigKeys;
import rocks.milspecsg.msparties.MSParties;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.CacheInvalidationService;
import rocks.milspecsg.msparties.api.Repository;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.model.Dbo;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Singleton
public abstract class ApiCacheInvalidationService<T> implements CacheInvalidationService<T> {

    protected ConfigurationService configurationService;

    protected Map<T, Long> cache;

    @Inject
    public ApiCacheInvalidationService(ConfigurationService configurationService) {
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
            toRemove.forEach(this::remove);
        };
    }

    @Override
    public Set<T> getAll() {
        return cache.keySet();
    }

    @Override
    public Optional<T> put(T t) {
        if (t == null) return Optional.empty();
        cache.put(t, System.currentTimeMillis());
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Saved ", t));
        return Optional.of(t);
    }

    @Override
    public List<T> put(List<T> list) {
        return list.stream().map(t -> put(t).orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void remove(T t) {
        cache.remove(t);
    }

    @Override
    public void remove(Predicate<? super T> predicate) {
        getOne(predicate).ifPresent(this::remove);
    }

    @Override
    public List<T> getAll(Predicate<? super T> predicate) {
        return getAll().stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public Optional<T> getOne(Predicate<? super T> predicate) {
        return Repository.single(getAll(predicate));
    }
}
