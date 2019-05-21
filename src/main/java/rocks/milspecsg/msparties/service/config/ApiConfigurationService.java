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
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Service to load, save and store data from a config file
 *
 * @author Cableguy20
 */
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

    protected Map<Integer, Map<Predicate<Boolean>, Function<Boolean, Boolean>>> booleanVerificationMap;
    protected Map<Integer, Map<Predicate<Double>, Function<Double, Double>>> doubleVerificationMap;
    protected Map<Integer, Map<Predicate<Integer>, Function<Integer, Integer>>> integerVerificationMap;
    protected Map<Integer, Map<Predicate<String>, Function<String, String>>> stringVerificationMap;
    protected Map<Integer, Map<Predicate<List<String>>, Function<List<String>, List<String>>>> stringListVerificationMap;

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
        initVerificationMaps();
        initDefaultMaps();
        initNodeNameMap();
        initNodeDescriptionMap();
        initConfigMaps();
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Config done"));
    }

    protected void initNodeTypeMap() {
        nodeTypeMap = new HashMap<>();

        nodeTypeMap.put(ConfigKeys.CACHE_INVALIDATION_TIMOUT_SECONDS_INT, Integer.class);
        nodeTypeMap.put(ConfigKeys.CACHE_INVALIDATION_INTERVAL_SECONDS_INT, Integer.class);

        nodeTypeMap.put(ConfigKeys.PARTY_MIN_NAME_LENGTH_INT, Integer.class);
        nodeTypeMap.put(ConfigKeys.PARTY_MAX_NAME_LENGTH_INT, Integer.class);

        nodeTypeMap.put(ConfigKeys.PARTY_MAX_SIZE_INT, Integer.class);
        nodeTypeMap.put(ConfigKeys.PARTY_FILTER_NAMES_STRLIST, List.class);

        nodeTypeMap.put(ConfigKeys.PARTY_CREATE_COST_DOUBLE, Double.class);
        nodeTypeMap.put(ConfigKeys.PARTY_DISBAND_COST_DOUBLE, Double.class);
        nodeTypeMap.put(ConfigKeys.PARTY_INVITE_COST_DOUBLBE, Double.class);
        nodeTypeMap.put(ConfigKeys.PARTY_JOIN_COST_DOUBLE, Double.class);
        nodeTypeMap.put(ConfigKeys.PLAYER_MAX_PARTIES_INT, Integer.class);
    }

    protected void initVerificationMaps() {
        booleanVerificationMap = new HashMap<>();
        doubleVerificationMap = new HashMap<>();
        integerVerificationMap = new HashMap<>();

        integerVerificationMap.put(
                ConfigKeys.PLAYER_MAX_PARTIES_INT,
                new HashMap<Predicate<Integer>, Function<Integer, Integer>>() {{
                    // if value < -1, set value to 1
                    put(value -> value < -1, value -> 1);
                }}
        );

        integerVerificationMap.put(
                ConfigKeys.PARTY_MAX_SIZE_INT,
                new HashMap<Predicate<Integer>, Function<Integer, Integer>>() {{
                    // if value < -1, set value to -1
                    put(value -> value < -1, value -> -1);
                }}
        );

        stringVerificationMap = new HashMap<>();
        stringListVerificationMap = new HashMap<>();
    }

    protected void initDefaultMaps() {
        defaultBooleanMap = new HashMap<>();

        defaultDoubleMap = new HashMap<>();
        defaultDoubleMap.put(ConfigKeys.PARTY_CREATE_COST_DOUBLE, 250.00);
        defaultDoubleMap.put(ConfigKeys.PARTY_DISBAND_COST_DOUBLE, 50.00);
        defaultDoubleMap.put(ConfigKeys.PARTY_INVITE_COST_DOUBLBE, 20.00);
        defaultDoubleMap.put(ConfigKeys.PARTY_JOIN_COST_DOUBLE, 20.00);

        defaultIntegerMap = new HashMap<>();
        defaultIntegerMap.put(ConfigKeys.CACHE_INVALIDATION_INTERVAL_SECONDS_INT, 5);
        defaultIntegerMap.put(ConfigKeys.CACHE_INVALIDATION_TIMOUT_SECONDS_INT, 30);
        defaultIntegerMap.put(ConfigKeys.PARTY_MAX_SIZE_INT, -1);
        defaultIntegerMap.put(ConfigKeys.PARTY_MIN_NAME_LENGTH_INT, 5);
        defaultIntegerMap.put(ConfigKeys.PARTY_MAX_NAME_LENGTH_INT, 15);
        defaultIntegerMap.put(ConfigKeys.PLAYER_MAX_PARTIES_INT, 1);

        defaultStringMap = new HashMap<>();
        defaultStringListMap = new HashMap<>();
        defaultStringListMap.put(ConfigKeys.PARTY_FILTER_NAMES_STRLIST, Arrays.asList("fuck", "shit", "bitch", "nigg", "cunt"));
    }

    protected void initNodeNameMap() {
        nodeNameMap = new HashMap<>();

        nodeNameMap.put(ConfigKeys.PARTY_CREATE_COST_DOUBLE, "partyCreateCost");
        nodeNameMap.put(ConfigKeys.PARTY_DISBAND_COST_DOUBLE, "partyDisbandCost");
        nodeNameMap.put(ConfigKeys.PARTY_INVITE_COST_DOUBLBE, "partyInviteCost");
        nodeNameMap.put(ConfigKeys.PARTY_JOIN_COST_DOUBLE, "partyJoinCost");

        nodeNameMap.put(ConfigKeys.CACHE_INVALIDATION_TIMOUT_SECONDS_INT, "cacheInvalidationTimoutSeconds");
        nodeNameMap.put(ConfigKeys.CACHE_INVALIDATION_INTERVAL_SECONDS_INT, "cacheInvalidationIntervalSeconds");
        nodeNameMap.put(ConfigKeys.PARTY_MAX_SIZE_INT, "partyMaxSize");

        nodeNameMap.put(ConfigKeys.PARTY_MIN_NAME_LENGTH_INT, "partyMinNameLength");
        nodeNameMap.put(ConfigKeys.PARTY_MAX_NAME_LENGTH_INT, "partyMaxNameLength");
        nodeNameMap.put(ConfigKeys.PLAYER_MAX_PARTIES_INT, "playerMaxParties");

        nodeNameMap.put(ConfigKeys.PARTY_FILTER_NAMES_STRLIST, "partyFilterNames");
    }

    protected void initNodeDescriptionMap() {
        nodeDescriptionMap = new HashMap<>();

        nodeDescriptionMap.put(ConfigKeys.PARTY_CREATE_COST_DOUBLE, "\nCost to create a party");
        nodeDescriptionMap.put(ConfigKeys.PARTY_DISBAND_COST_DOUBLE, "\nCost to disband a party");
        nodeDescriptionMap.put(ConfigKeys.PARTY_INVITE_COST_DOUBLBE, "\nCost to invite a player to a party");
        nodeDescriptionMap.put(ConfigKeys.PARTY_JOIN_COST_DOUBLE, "\nCost to join a party");

        nodeDescriptionMap.put(ConfigKeys.CACHE_INVALIDATION_TIMOUT_SECONDS_INT, "\nMaximum time in seconds for a party to stay in cache,\nShould be a multiple of cacheInvalidationIntervalSeconds (default 30)");
        nodeDescriptionMap.put(ConfigKeys.CACHE_INVALIDATION_INTERVAL_SECONDS_INT, "\nInterval in seconds for the party cache invalidation task to run (default 5)");
        nodeDescriptionMap.put(ConfigKeys.PARTY_MAX_SIZE_INT, "\nMaximum party size (-1 for unlimited)\nnote: values less than -1 will be replaced with -1");
        nodeDescriptionMap.put(ConfigKeys.PARTY_MIN_NAME_LENGTH_INT, "\nMinimum length for a party's name, values less than 1 will be reset to 1\nto avoid breaking things (default 5)");
        nodeDescriptionMap.put(ConfigKeys.PARTY_MAX_NAME_LENGTH_INT, "\nMaximum length for a party's name, values less than partyMinNameLength will be reset to partyMinNameLength\nto avoid breaking things (default 15)");
        nodeDescriptionMap.put(ConfigKeys.PLAYER_MAX_PARTIES_INT, "\nMaximum number of parties a player can be in.\nSet to -1 for unlimited (default 1)\nnote: values less than -1 will be replaced with 1");
        nodeDescriptionMap.put(ConfigKeys.PARTY_FILTER_NAMES_STRLIST, "\nList of strings which a party name may not contain\nParty name will additionally be required to contain alpha-numeric characters only");
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

        int updatedCount = 0;
        for (Integer nodeKey : nodeNameMap.keySet()) {
            CommentedConfigurationNode node = rootConfigurationNode.getNode(nodeNameMap.get(nodeKey));
            Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Loading node " + nodeKey));
            if (node.isVirtual()) {
                saveDefaultValue(nodeKey, node, nodeTypeMap.get(nodeKey));
                updatedCount++;
            } else {
                if (initConfigValue(nodeKey, node, nodeTypeMap.get(nodeKey))) {
                    updatedCount++;
                }
            }

            if (node.isVirtual() || !node.getComment().isPresent()) {
                node.setComment(nodeDescriptionMap.get(nodeKey));
                updatedCount++;
            }
        }
        if (updatedCount > 0) {
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
                Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Saving config value (" + clazz.getName() + ")" + def.get()));
                node.setValue(def.get());
            } else {
                throw new Exception("Casting error while generating configuration: This should not happen, please report this incident on the plugin page");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param nodeKey
     * @param node
     * @param clazz
     * @return {@code true} if values were modified from config value, {@code false} otherwise
     */
    protected boolean initConfigValue(Integer nodeKey, CommentedConfigurationNode node, Class<?> clazz) {
        boolean[] modified = new boolean[]{false};
        try {
            if (clazz.isAssignableFrom(Boolean.class)) {
                Boolean value = node.getBoolean();
                configBooleanMap.put(nodeKey, verify(booleanVerificationMap.get(nodeKey), value, node, modified));
                //Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Loading config value (Boolean): " + value));
            } else if (clazz.isAssignableFrom(Double.class)) {
                Double value = node.getDouble();
                configDoubleMap.put(nodeKey, verify(doubleVerificationMap.get(nodeKey), value, node, modified));
                //Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Loading config value (Double): " + value));
            } else if (clazz.isAssignableFrom(Integer.class)) {
                Integer value = node.getInt();
                configIntegerMap.put(nodeKey, verify(integerVerificationMap.get(nodeKey), value, node, modified));
                //Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Loading config value (Integer): " + value));
            } else if (clazz.isAssignableFrom(String.class)) {
                String value = node.getString();
                configStringMap.put(nodeKey, verify(stringVerificationMap.get(nodeKey), value, node, modified));
                //Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Loading config value (String): " + value));
            } else if (clazz.isAssignableFrom(List.class)) {
                List<String> value = node.getList(TypeToken.of(String.class));
                configStringListMap.put(nodeKey, verify(stringListVerificationMap.get(nodeKey), value, node, modified));
                //Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Loading config value (List<String>): " + value));
            } else {
                throw new Exception("Class did not match any values");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modified[0];
    }

    private <T> T verify(Map<Predicate<T>, Function<T, T>> verificationMap, T value, CommentedConfigurationNode node, boolean[] modified) {
        if (verificationMap == null) return value; // if there is no verification function defined
        for (Map.Entry<Predicate<T>, Function<T, T>> entry : verificationMap.entrySet())
            if (entry.getKey().test(value)) {
                modified[0] = true;
                T result = entry.getValue().apply(value);
                // Sponge.getServer().getConsole().sendMessage(Text.of("Changing ", value, " to ", result));
                node.setValue(result);
                return result;
            }
        return value;
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

    /**
     * @param configMap  Map of values to check if present
     * @param defaultMap Fallback map
     * @param key        configuration node key to use
     * @param <T>        Value type of map
     * @return value at key from configMap if present and non-null, otherwise value at key from defaultMap
     */
    protected <T> T getValue(Map<Integer, T> configMap, Map<Integer, T> defaultMap, int key) {
        if (configMap.containsKey(key)) {
            T result = configMap.get(key);
            return result != null ? result : defaultMap.get(key);
        } else return defaultMap.get(key);
    }
}
