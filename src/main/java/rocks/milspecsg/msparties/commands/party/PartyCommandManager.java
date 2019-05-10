package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msparties.PluginPermissions;
import rocks.milspecsg.msparties.commands.CommandRegisterer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartyCommandManager implements CommandRegisterer {

    public static Map<List<String>, CommandSpec> subCommands = new HashMap<>();

    private PartyHelpCommand partyHelpCommand;
    private PartyCreateCommand partyCreateCommand;
    private PartyFindCommand partyFindCommand;
    private PartyDisbandCommand partyDisbandCommand;

    @Inject
    public PartyCommandManager(PartyHelpCommand partyHelpCommand, PartyCreateCommand partyCreateCommand, PartyFindCommand partyFindCommand, PartyDisbandCommand partyDisbandCommand) {
        this.partyHelpCommand = partyHelpCommand;
        this.partyCreateCommand = partyCreateCommand;
        this.partyFindCommand = partyFindCommand;
        this.partyDisbandCommand = partyDisbandCommand;
    }

    @Override
    public void register(Object plugin) {
        Map<List<String>, CommandSpec> subCommands = new HashMap<>();

        subCommands.put(Arrays.asList("create", "c"), CommandSpec.builder()
                .description(Text.of("Create a party"))
                .permission(PluginPermissions.CREATE_COMMAND)
                .arguments(
                        GenericArguments.string(Text.of("name"))
                )
                .executor(partyCreateCommand)
                .build());

        subCommands.put(Arrays.asList("join", "j"), CommandSpec.builder()
                .description(Text.of("Create a party"))
                .permission(PluginPermissions.JOIN_COMMAND)
                .arguments(
                        GenericArguments.string(Text.of("name"))
                )
                .executor(partyFindCommand)
                .build());

        subCommands.put(Arrays.asList("find", "f", "info", "i"), CommandSpec.builder()
                .description(Text.of("Show party info"))
                .permission(PluginPermissions.INFO_COMMAND)
                .arguments(
                        GenericArguments.optional(GenericArguments.string(Text.of("name")))
                )
                .executor(partyFindCommand)
                .build());

        subCommands.put(Arrays.asList("disband", "remove", "delete", "begone"), CommandSpec.builder()
                .description(Text.of("Disband party"))
                .permission(PluginPermissions.DISBAND_COMMAND)
                .executor(partyDisbandCommand)
                .build());

        //Build all commands
        CommandSpec mainCommand = CommandSpec.builder()
                .description(Text.of("Displays all available party subcommands"))
                .executor(partyHelpCommand)
                .children(subCommands)
                .build();

        //Register commands
        Sponge.getCommandManager().register(plugin, mainCommand, "msparties", "msp", "parties", "p", "party");
        PartyCommandManager.subCommands = subCommands;
    }
}
