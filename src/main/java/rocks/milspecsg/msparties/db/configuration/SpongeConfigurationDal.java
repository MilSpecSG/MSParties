package rocks.milspecsg.msparties.db.configuration;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.config.DefaultConfig;
import rocks.milspecsg.msparties.db.configuration.ConfigurationDal;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

public class SpongeConfigurationDal implements ConfigurationDal {

    private ConfigurationNode baseNode;

    public SpongeConfigurationDal() {
        this.baseNode = null;
    }

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Override
    public ConfigurationNode getBaseNode() {
        if (baseNode==null) throw new NotImplementedException();
        return baseNode;
    }

    @Override
    public void load() {
        try {
            baseNode = configManager.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
