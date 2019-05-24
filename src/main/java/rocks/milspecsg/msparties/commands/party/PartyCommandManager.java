package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msparties.PluginPermissions;
import rocks.milspecsg.msparties.api.party.PartyNameCacheService;
import rocks.milspecsg.msparties.commands.CommandManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartyCommandManager implements CommandManager {

    protected PartyNameCacheService partyNameCacheService;

    public static Map<List<String>, CommandSpec> subCommands = new HashMap<>();

    @Inject
    protected PartyCreateCommand partyCreateCommand;

    @Inject
    protected PartyDisbandCommand partyDisbandCommand;

    @Inject
    protected PartyInfoCommand partyInfoCommand;

    @Inject
    protected PartyHelpCommand partyHelpCommand;

    @Inject
    protected PartyInviteCommand partyInviteCommand;

    @Inject
    protected PartyJoinCommand partyJoinCommand;

    @Inject
    protected PartyListCommand partyListCommand;

    @Inject
    protected PartyMembersCommand partyMembersCommand;

    @Inject
    protected PartySetRankCommand partySetRankCommand;


    @Inject
    public PartyCommandManager(PartyNameCacheService partyNameCacheService) {
        this.partyNameCacheService = partyNameCacheService;
    }

    @Override
    public void register(Object plugin) {
        Map<List<String>, CommandSpec> subCommands = new HashMap<>();

        subCommands.put(Arrays.asList("create", "c"), CommandSpec.builder()
                .description(Text.of("Create a party"))
                .permission(PluginPermissions.CREATE_COMMAND)
                .arguments(
                        GenericArguments.string(Text.of("name")),
                        GenericArguments.string(Text.of("tag"))
                )
                .executor(partyCreateCommand)
                .build());

        subCommands.put(Arrays.asList("disband", "remove", "delete", "begone"), CommandSpec.builder()
                .description(Text.of("Disband party"))
                .permission(PluginPermissions.DISBAND_COMMAND)
                .arguments(
                        GenericArguments.optional(GenericArguments.string(Text.of("name")))
                )
                .executor(partyDisbandCommand)
                .build());

        subCommands.put(Arrays.asList("info", "i"), CommandSpec.builder()
                .description(Text.of("Show party info"))
                .permission(PluginPermissions.INFO_COMMAND)
                .arguments(
                        GenericArguments.optional(GenericArguments.withSuggestions(GenericArguments.string(Text.of("name")), partyNameCacheService::getSuggestions))
                )
                .executor(partyInfoCommand)
                .build());

        subCommands.put(Arrays.asList("help", "h"), CommandSpec.builder()
                .description(Text.of("Displays all available party subcommands"))
                .permission(PluginPermissions.HELP_COMMAND)
                .executor(partyHelpCommand)
                .build());

        subCommands.put(Arrays.asList("invite", "inv"), CommandSpec.builder()
                .description(Text.of("Create a party"))
                .permission(PluginPermissions.INVITE_COMMAND)
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("name")))
                )
                .executor(partyInviteCommand)
                .build());

        subCommands.put(Arrays.asList("join", "j"), CommandSpec.builder()
                .description(Text.of("Join a party"))
                .permission(PluginPermissions.JOIN_COMMAND)
                .arguments(
                        GenericArguments.string(Text.of("name"))
                )
                .executor(partyJoinCommand)
                .build());

        subCommands.put(Arrays.asList("list", "l"), CommandSpec.builder()
                .description(Text.of("List all parties"))
                .permission(PluginPermissions.LIST_COMMAND)
                .arguments(
                        GenericArguments.string(Text.of("name"))
                )
                .executor(partyListCommand)
                .build());

        subCommands.put(Arrays.asList("members", "m"), CommandSpec.builder()
                .description(Text.of("List all members of a party"))
                .permission(PluginPermissions.MEMBERS_COMMAND)
                .arguments(
                        GenericArguments.optional(GenericArguments.withSuggestions(GenericArguments.string(Text.of("name")), partyNameCacheService::getSuggestions))
                )
                .executor(partyMembersCommand)
                .build());

        subCommands.put(Arrays.asList("setrank", "sr"), CommandSpec.builder()
                .description(Text.of("List all parties"))
                .permission(PluginPermissions.JOIN_COMMAND)
                .arguments(
                        GenericArguments.string(Text.of("name"))
                )
                .executor(partySetRankCommand)
                .build());


        //Build all commands
        CommandSpec mainCommand = CommandSpec.builder()
                .description(Text.of("Displays all available party subcommands"))
                .executor(partyHelpCommand)
                .children(subCommands)
                .build();

        //Register commands
        Sponge.getCommandManager().register(plugin, mainCommand, "parties", "p", "party", "f");
        PartyCommandManager.subCommands = subCommands;
    }
}
