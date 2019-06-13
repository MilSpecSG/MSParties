package rocks.milspecsg.msparties.service.config.implementation;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msparties.ConfigKeys;
import rocks.milspecsg.msparties.service.config.ApiConfigurationService;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class MSConfigurationService extends ApiConfigurationService {

    @Inject
    public MSConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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
}
