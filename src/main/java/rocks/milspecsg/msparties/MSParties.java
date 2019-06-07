package rocks.milspecsg.msparties;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.commands.party.*;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.service.member.ApiMemberRepository;
import rocks.milspecsg.msparties.service.member.implementation.MSMemberRepository;
import rocks.milspecsg.msparties.service.party.ApiPartyRepository;
import rocks.milspecsg.msparties.service.party.implementation.MSPartyRepository;

@Plugin(id = PluginInfo.Id, name = PluginInfo.Name, version = PluginInfo.Version, description = PluginInfo.Description, authors = PluginInfo.Authors, url = PluginInfo.Url)
public class MSParties {

    @Inject
    private Logger logger;

    @Inject
    public Injector spongeRootInjector;

    public static MSParties plugin = null;
    private Injector injector = null;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }

    @Listener
    public void onServerInitialization(GameInitializationEvent event) {
        plugin = this;
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Loading..."));
        initServices();
        initSingletonServices();
        initCommands();
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Finished"));
    }

    private void initServices() {
        injector = spongeRootInjector.createChildInjector(new MSPartiesModule());
    }

    private void initSingletonServices() {
        injector.getInstance(ConfigurationService.class);
    }

    private void initCommands() {
        injector.getInstance(Key.get(new TypeLiteral<PartyCommandManager<Party, Member>>() {})).register(this);
    }

    private class MSPartiesModule extends APIModule<Party, Member> {
        @Override
        protected void configure() {
            super.configure();

            bind(new TypeLiteral<ApiPartyRepository<Party, Member>>() {}).to(new TypeLiteral<MSPartyRepository>() {});
            bind(new TypeLiteral<ApiMemberRepository<Member>>() {}).to(new TypeLiteral<MSMemberRepository>() {});
        }
    }
}
