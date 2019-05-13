package rocks.milspecsg.msparties.service.config;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msparties.ConfigKeys;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.config.ConfigurationService;

import java.io.IOException;
import java.util.*;

@Singleton
public class ApiConfigurationService implements ConfigurationService {

    protected ConfigurationLoader<CommentedConfigurationNode> configLoader;
    protected CommentedConfigurationNode rootConfigurationNode;

    protected Map<Integer, Boolean> defaultBooleanMap;
    protected Map<Integer, Double> defaultDoubleMap;
    protected Map<Integer, Integer> defaultIntegerMap;
    protected Map<Integer, String> defaultStringMap;
    protected Map<Integer, List<String>> defaultStringListMap;

    protected Map<Integer, Boolean> configBooleanMap;
    protected Map<Integer, Double> configDoubleMap;
    protected Map<Integer, Integer> configIntegerMap;
    protected Map<Integer, String> configStringMap;
    protected Map<Integer, List<String>> configStringListMap;

    /**
     * Maps {@link ConfigKeys} to configuration node names
     */
    protected Map<Integer, String> nodeNameMap;

    /**
     * Maps {@link ConfigKeys} to configuration node descriptions
     */
    protected Map<Integer, String> nodeDescriptionMap;

    /**
     * Maps {@link ConfigKeys} to configuration node types
     */
    protected Map<Integer, Class<?>> nodeTypeMap;

    @Inject
    public ApiConfigurationService(@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        this.configLoader = configLoader;
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Loading config"));
        initNodeTypeMap();
        initDefaultMaps();
        initNodeNameMap();
        initNodeDescriptionMap();
        initConfigMaps();
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Config done"));
    }

    protected void initDefaultMaps() {
        defaultBooleanMap = new HashMap<>();

        defaultDoubleMap = new HashMap<>();

        defaultIntegerMap = new HashMap<>();
        defaultIntegerMap.put(ConfigKeys.CACHE_INVALIDATION_INTERVAL_SECONDS_INT, 5);
        defaultIntegerMap.put(ConfigKeys.CACHE_INVALIDATION_TIMOUT_SECONDS_INT, 30);
        defaultIntegerMap.put(ConfigKeys.PARTY_MAX_SIZE_INT, -1);

        defaultStringMap = new HashMap<>();
        defaultStringListMap = new HashMap<>();
        defaultStringListMap.put(ConfigKeys.PARTY_FILTER_NAMES_STRLIST, Arrays.asList("fuck", "shit", "bitch", "nigg", "cunt"));
    }

    protected void initNodeNameMap() {
        nodeNameMap = new HashMap<>();

        nodeNameMap.put(ConfigKeys.CACHE_INVALIDATION_TIMOUT_SECONDS_INT, "cacheInvalidationTimoutSeconds");
        nodeNameMap.put(ConfigKeys.CACHE_INVALIDATION_INTERVAL_SECONDS_INT, "cacheInvalidationIntervalSeconds");
        nodeNameMap.put(ConfigKeys.PARTY_MAX_SIZE_INT, "partyMaxSize");
        nodeNameMap.put(ConfigKeys.PARTY_FILTER_NAMES_STRLIST, "partyFilterNames");
    }

    protected void initNodeDescriptionMap() {
        nodeDescriptionMap = new HashMap<>();

        nodeDescriptionMap.put(ConfigKeys.CACHE_INVALIDATION_TIMOUT_SECONDS_INT, "Maximum time in seconds for a party to stay in cache (default 30)");
        nodeDescriptionMap.put(ConfigKeys.CACHE_INVALIDATION_INTERVAL_SECONDS_INT, "Interval in seconds for the party cache invalidation task to run (default 5)");
        nodeDescriptionMap.put(ConfigKeys.PARTY_MAX_SIZE_INT, "Maximum party size (-1 for unlimited)");
        nodeDescriptionMap.put(ConfigKeys.PARTY_FILTER_NAMES_STRLIST, "List of strings which a party name may not contain");
    }

    protected void initNodeTypeMap() {
        nodeTypeMap = new HashMap<>();

        nodeTypeMap.put(ConfigKeys.CACHE_INVALIDATION_TIMOUT_SECONDS_INT, Integer.class);
        nodeTypeMap.put(ConfigKeys.CACHE_INVALIDATION_INTERVAL_SECONDS_INT, Integer.class);
        nodeTypeMap.put(ConfigKeys.PARTY_MAX_SIZE_INT, Integer.class);
        nodeTypeMap.put(ConfigKeys.PARTY_FILTER_NAMES_STRLIST, List.class);
    }

    protected void initConfigMaps() {
        configBooleanMap = new HashMap<>();
        configDoubleMap = new HashMap<>();
        configIntegerMap = new HashMap<>();
        configStringMap = new HashMap<>();
        configStringListMap = new HashMap<>();

        try {
            rootConfigurationNode = configLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int createdCount = 0;
        for (Integer nodeKey : nodeNameMap.keySet()) {
            CommentedConfigurationNode node = rootConfigurationNode.getNode(nodeNameMap.get(nodeKey));
            if (node.isVirtual()) {
                saveDefaultValue(nodeKey, node, nodeTypeMap.get(nodeKey));
                createdCount++;
            } else {
                initConfigValue(nodeKey, node, nodeTypeMap.get(nodeKey));
            }
        }
        if (createdCount > 0) {
            Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Saving config"));
            try {
                configLoader.save(rootConfigurationNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected <T> void saveDefaultValue(Integer nodeKey, CommentedConfigurationNode node, Class<T> clazz) {
        Optional<T> def = getDefault(nodeKey, clazz);
        try {
            if (def.isPresent()) {
                // lists need to be done another way, this is not supported by configuration api
                if (List.class.isAssignableFrom(clazz)) return;
                node.setValue(TypeToken.of(clazz), def.get());
            } else {
                throw new Exception("Casting error while generating configuration: This should not happen, please report this incident on the plugin page");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initConfigValue(Integer nodeKey, CommentedConfigurationNode node, Class<?> clazz) {
        try {
            if (clazz.isAssignableFrom(Boolean.class)) {
                configBooleanMap.put(nodeKey, node.getBoolean());
            } else if (clazz.isAssignableFrom(Double.class)) {
                configDoubleMap.put(nodeKey, node.getDouble());
            } else if (clazz.isAssignableFrom(Integer.class)) {
                configIntegerMap.put(nodeKey, node.getInt());
            } else if (clazz.isAssignableFrom(String.class)) {
                configStringMap.put(nodeKey, node.getString());
            } else if (clazz.isAssignableFrom(List.class)) {
                configStringListMap.put(nodeKey, node.getList(TypeToken.of(String.class)));
            } else {
                throw new Exception("Class did not match any values");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<?> getDefault(int key) {
        Class<?> clazz = nodeTypeMap.get(key);
        Optional<?> optionalObject = getDefault(key, clazz);
        if (optionalObject.isPresent() && clazz.isInstance(optionalObject.get())) {
            return Optional.of(clazz.cast(optionalObject.get()));
        }
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> getDefault(int key, Class<T> clazz) {
        try {
            if (clazz.isAssignableFrom(Boolean.class)) {
                return Optional.of(clazz.cast(getDefaultBoolean(key)));
            } else if (clazz.isAssignableFrom(Double.class)) {
                return Optional.of(clazz.cast(getDefaultDouble(key)));
            } else if (clazz.isAssignableFrom(Integer.class)) {
                return Optional.of(clazz.cast(getDefaultInteger(key)));
            } else if (clazz.isAssignableFrom(String.class)) {
                return Optional.of(clazz.cast(getDefaultString(key)));
            } else if (clazz.isAssignableFrom(List.class)) {
                return Optional.of(clazz.cast(getDefaultStringList(key)));
            } else {
                throw new Exception("Class did not match any values");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Boolean getDefaultBoolean(int key) {
        return defaultBooleanMap.get(key);
    }

    @Override
    public Double getDefaultDouble(int key) {
        return defaultDoubleMap.get(key);
    }

    @Override
    public Integer getDefaultInteger(int key) {
        return defaultIntegerMap.get(key);
    }

    @Override
    public String getDefaultString(int key) {
        return defaultStringMap.get(key);
    }

    @Override
    public List<String> getDefaultStringList(int key) {
        return defaultStringListMap.get(key);
    }

    @Override
    public <T> T getConfig(int key) {
        return null;
    }

    @Override
    public Boolean getConfigBoolean(int key) {
        return getValue(configBooleanMap, defaultBooleanMap, key);
    }

    @Override
    public Double getConfigDouble(int key) {
        return getValue(configDoubleMap, defaultDoubleMap, key);
    }

    @Override
    public Integer getConfigInteger(int key) {
        return getValue(configIntegerMap, defaultIntegerMap, key);
    }

    @Override
    public String getConfigString(int key) {
        return getValue(configStringMap, defaultStringMap, key);
    }

    @Override
    public List<String> getConfigStringList(int key) {
        return getValue(configStringListMap, defaultStringListMap, key);
    }

    protected <T> T getValue(Map<Integer, T> configMap, Map<Integer, T> defaultMap, int key) {
        if (configMap.containsKey(key)) {
            T result = configMap.get(key);
            return result != null ? result : defaultMap.get(key);
        } else return defaultMap.get(key);
    }
}
