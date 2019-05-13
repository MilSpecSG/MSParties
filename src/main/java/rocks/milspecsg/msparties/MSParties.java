package rocks.milspecsg.msparties;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.party.PartyCacheService;
import rocks.milspecsg.msparties.commands.party.*;

@Plugin(id = PluginInfo.Id, name = PluginInfo.Name, version = PluginInfo.Version, description = PluginInfo.Description, authors = PluginInfo.Authors, url = PluginInfo.Url)
public class MSParties {

    @Inject
    private Logger logger;

    @Inject
    public Injector spongeRootInjector;

    public static MSParties plugin = null;
    public static Injector injector = null;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }

    @Listener
    public void onServerInitialization(GameInitializationEvent event) {
        plugin = this;
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Hello!"));
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Loading..."));
        initServices();
        initSingletonServices();
        initCommands();

        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Finished"));
    }

    private void initServices() {
        injector = spongeRootInjector.createChildInjector(new SpongeInjector());
        //injector = Guice.createInjector();
    }

    private void initSingletonServices() {
        injector.getInstance(ConfigurationService.class);
        injector.getInstance(PartyCacheService.class);
    }

    private void initCommands() {
        injector.getInstance(PartyCommandManager.class).register(this);
    }


}
