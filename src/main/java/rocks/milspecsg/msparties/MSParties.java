package rocks.milspecsg.msparties;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msparties.commands.HelpCommand;
import rocks.milspecsg.msparties.commands.PartyCreateCommand;
import rocks.milspecsg.msparties.commands.PartyDisbandCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Plugin(id = PluginInfo.Id, name = PluginInfo.Name, version = PluginInfo.Version, description = PluginInfo.Description, authors = PluginInfo.Authors, url = PluginInfo.Url)
public class MSParties {

    @Inject
    private Logger logger;

    public static Injector services;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }

    @Listener
    public void onServerInitialization(GameInitializationEvent event) {
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Hello!"));
        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Loading services..."));
        initServices();

        Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "Loading configuration..."));
    }


    private void initServices() {
        Guice.createInjector(new SpongeInjector());
    }

    private void initCommands() {
        Map<List<String>, CommandSpec> Subcommands = new HashMap<>();

        Subcommands.put(Arrays.asList("create, c, make"), CommandSpec.builder()
                .description(Text.of("Create party"))
                .permission(PluginPermissions.CREATE_COMMAND)
                .arguments(GenericArguments.string(Text.of("name")))
                .executor(new PartyCreateCommand())
                .build());

        Subcommands.put(Arrays.asList("disband"), CommandSpec.builder()
                .description(Text.of("Disband party"))
                .permission(PluginPermissions.DISBAND_COMMAND)
                .executor(new PartyDisbandCommand())
                .build());

        //Build all commands
        CommandSpec mainCommand = CommandSpec.builder()
                .description(Text.of("Displays all available commands"))
                .executor(new HelpCommand())
                .children(Subcommands)
                .build();

        //Register commands
        Sponge.getCommandManager().register(this, mainCommand, "msrefill", "mscr", "msr", "refill");
    }


}
