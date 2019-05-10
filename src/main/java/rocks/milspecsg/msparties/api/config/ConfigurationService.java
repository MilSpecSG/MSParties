package rocks.milspecsg.msparties.api.config;

import com.google.inject.Singleton;
import rocks.milspecsg.msparties.ConfigKeys;

public interface ConfigurationService {

    /**
     * @param key corresponds to {@link ConfigKeys} final ints
     * @return default value for this key
     */
    Double getDefaultDouble(int key);

    /**
     * @param key corresponds to {@link ConfigKeys} final ints
     * @return default value for this key
     */
    Integer getDefaultInteger(int key);

    /**
     * @param key corresponds to {@link ConfigKeys} final ints
     * @return default value for this key
     */
    String getDefaultString(int key);

    /**
     * Will provide default values if the ones from the config are not present
     *
     * @param key corresponds to {@link ConfigKeys} final ints
     * @return config value for this key
     */
    Double getConfigDouble(int key);

    /**
     * Will provide default values if the ones from the config are not present
     *
     * @param key corresponds to {@link ConfigKeys} final ints
     * @return config value for this key
     */
    Integer getConfigInteger(int key);

    /**
     * Will provide default values if the ones from the config are not present
     *
     * @param key corresponds to {@link ConfigKeys} final ints
     * @return config value for this key
     */
    String getConfigString(int key);

}
