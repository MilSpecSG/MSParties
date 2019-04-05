package rocks.milspecsg.msparties.sponge;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msparties.api.service.party.PartyManager;

@Plugin(id = SpongePluginInfo.Id, name = SpongePluginInfo.Name, version = SpongePluginInfo.Version, description = SpongePluginInfo.Description, authors = SpongePluginInfo.Authors, url = SpongePluginInfo.Url)
public class MSParties {


    @Inject private PartyManager partyManager;

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }

    @Listener
    public void onServerInitialization(GameInitializationEvent event) {
        Sponge.getServer().getConsole().sendMessage(Text.of(SpongePluginInfo.PluginPrefix, "Hello!"));
        Sponge.getServer().getConsole().sendMessage(Text.of(SpongePluginInfo.PluginPrefix, "Loading services..."));
        initServices();

        Sponge.getServer().getConsole().sendMessage(Text.of(SpongePluginInfo.PluginPrefix, "Loading configuration..."));
    }


    private void initServices() {
    }


}
