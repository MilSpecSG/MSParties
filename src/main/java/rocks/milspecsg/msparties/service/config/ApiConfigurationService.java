package rocks.milspecsg.msparties.service.config;

import rocks.milspecsg.msparties.ConfigKeys;
import rocks.milspecsg.msparties.api.config.ConfigurationService;

import java.util.HashMap;
import java.util.Map;

public class ApiConfigurationService implements ConfigurationService {

    Map<Integer, Double> defaultDoubleValues;
    Map<Integer, Integer> defaultIntegerValues;
    Map<Integer, String> defaultStringValues;

    public ApiConfigurationService() {
        defaultDoubleValues = new HashMap<>();



        defaultIntegerValues = new HashMap<>();
        defaultIntegerValues.put(ConfigKeys.CACHE_INVALIDATION_INTERVAL_SECONDS, 5);
        defaultIntegerValues.put(ConfigKeys.CACHE_INVALIDATION_TIMOUT_SECONDS, 30);



        defaultStringValues = new HashMap<>();



    }

    @Override
    public Double getDefaultDouble(int key) {
        return defaultDoubleValues.get(key);
    }

    @Override
    public Integer getDefaultInteger(int key) {
        return defaultIntegerValues.get(key);
    }

    @Override
    public String getDefaultString(int key) {
        return defaultStringValues.get(key);
    }

    @Override
    public Double getConfigDouble(int key) {
        return null;
    }

    @Override
    public Integer getConfigInteger(int key) {
        return null;
    }

    @Override
    public String getConfigString(int key) {
        return null;
    }
}
