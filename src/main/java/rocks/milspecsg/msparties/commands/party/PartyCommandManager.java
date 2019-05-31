package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.PluginPermissions;
import rocks.milspecsg.msparties.api.party.PartyNameCacheService;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.commands.CommandManager;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.misc.TeleportationRequest;
import rocks.milspecsg.msparties.model.misc.TriConsumer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PartyCommandManager<P extends Party, M extends Member> implements CommandManager {

    protected PartyRepository partyRepository;
    protected PartyNameCacheService partyNameCacheService;

    public static Map<List<String>, CommandSpec> subCommands = new HashMap<>();


    @Inject
    protected PartyAcceptCommand<P, M> partyAcceptCommand;

    @Inject
    protected PartyCreateCommand<P> partyCreateCommand;

    @Inject
    protected PartyDisbandCommand<P> partyDisbandCommand;

    @Inject
    protected PartyInfoCommand<P, M> partyInfoCommand;

    @Inject
    protected PartyHelpCommand partyHelpCommand;

    @Inject
    protected PartyInviteCommand<P> partyInviteCommand;

    @Inject
    protected PartyJoinCommand<P> partyJoinCommand;

    @Inject
    protected PartyLeaveCommand<P> partyLeaveCommand;

    @Inject
    protected PartyListCommand<P> partyListCommand;

    @Inject
    protected PartyMembersCommand<P, M> partyMembersCommand;

    @Inject
    protected PartyPrivacyCommand<P> partyPrivacyCommand;

    @Inject
    protected PartySetRankCommand<P> partySetRankCommand;

    @Inject
    protected PartyTpaallCommand<P, M> partyTpaallCommand;


    @Inject
    public PartyCommandManager(PartyRepository<P> partyRepository, PartyNameCacheService partyNameCacheService) {
        this.partyRepository = partyRepository;
        this.partyNameCacheService = partyNameCacheService;
    }

    @Override
    public void register(Object plugin) {
        Map<List<String>, CommandSpec> subCommands = new HashMap<>();

        subCommands.put(Arrays.asList("accept", "a"), CommandSpec.builder()
                .description(Text.of("Accept a request"))
                .permission(PluginPermissions.ACCEPT_COMMAND)
                .arguments(
                        GenericArguments.optionalWeak(GenericArguments.withSuggestions(GenericArguments.string(Text.of("party")), partyNameCacheService::getSuggestions))
                )
                .executor(partyAcceptCommand)
                .build());

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
                        GenericArguments.optionalWeak(GenericArguments.withSuggestions(GenericArguments.string(Text.of("party")), partyNameCacheService::getSuggestions))
                )
                .executor(partyDisbandCommand)
                .build());

        subCommands.put(Arrays.asList("info", "i"), CommandSpec.builder()
                .description(Text.of("Show party info"))
                .permission(PluginPermissions.INFO_COMMAND)
                .arguments(
                        GenericArguments.optionalWeak(GenericArguments.withSuggestions(GenericArguments.string(Text.of("party")), partyNameCacheService::getSuggestions))
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
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
                        GenericArguments.optionalWeak(GenericArguments.withSuggestions(GenericArguments.string(Text.of("party")), partyNameCacheService::getSuggestions))
                )
                .executor(partyInviteCommand)
                .build());

        subCommands.put(Arrays.asList("join", "j"), CommandSpec.builder()
                .description(Text.of("Join a party"))
                .permission(PluginPermissions.JOIN_COMMAND)
                .arguments(
                        GenericArguments.optionalWeak(GenericArguments.withSuggestions(GenericArguments.string(Text.of("party")), partyNameCacheService::getSuggestions))
                )
                .executor(partyJoinCommand)
                .build());

        subCommands.put(Arrays.asList("leave", "l"), CommandSpec.builder()
                .description(Text.of("Leave a party"))
                .permission(PluginPermissions.LEAVE_COMMAND)
                .arguments(
                        GenericArguments.optionalWeak(GenericArguments.withSuggestions(GenericArguments.string(Text.of("party")), partyNameCacheService::getSuggestions))
                )
                .executor(partyLeaveCommand)
                .build());

        subCommands.put(Arrays.asList("list", "l"), CommandSpec.builder()
                .description(Text.of("List all parties"))
                .permission(PluginPermissions.LIST_COMMAND)
                .arguments(
                        GenericArguments.optional(GenericArguments.string(Text.of("search")))
                )
                .executor(partyListCommand)
                .build());

        subCommands.put(Arrays.asList("members", "m"), CommandSpec.builder()
                .description(Text.of("List all members of a party"))
                .permission(PluginPermissions.MEMBERS_COMMAND)
                .arguments(
                        GenericArguments.optionalWeak(GenericArguments.withSuggestions(GenericArguments.string(Text.of("party")), partyNameCacheService::getSuggestions))
                )
                .executor(partyMembersCommand)
                .build());

        subCommands.put(Arrays.asList("privacy", "p"), CommandSpec.builder()
                .description(Text.of("Get or set privacy of a party"))
                .permission(PluginPermissions.PRIVACY_COMMAND)
                .arguments(
                        GenericArguments.optionalWeak(GenericArguments.choices(Text.of("privacy"), () -> Arrays.asList("public", "private"), Text::of)),
                        GenericArguments.optionalWeak(GenericArguments.withSuggestions(GenericArguments.string(Text.of("party")), partyNameCacheService::getSuggestions))
                ).executor(partyPrivacyCommand)
                .build());

        subCommands.put(Arrays.asList("setrank", "sr"), CommandSpec.builder()
                .description(Text.of("Set rank"))
                .permission(PluginPermissions.SET_RANK_COMMAND)
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
                        GenericArguments.integer(Text.of("rankIndex")),
                        GenericArguments.optionalWeak(GenericArguments.withSuggestions(GenericArguments.string(Text.of("party")), partyNameCacheService::getSuggestions))
                )
                .executor(partySetRankCommand)
                .build());

        subCommands.put(Arrays.asList("tpaall", "here"), CommandSpec.builder()
                .description(Text.of("Send a teleport request to your clan"))
                .permission(PluginPermissions.TPAALL_COMMAND)
                .arguments(
                        GenericArguments.optionalWeak(GenericArguments.withSuggestions(GenericArguments.string(Text.of("party")), partyNameCacheService::getSuggestions))
                )
                .executor(partyTpaallCommand)
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

    static void listCurrentPartiesToPlayer(Player player, List<? extends Party> parties, String command) {
        player.sendMessage(Text.of(
                PluginInfo.PluginPrefix, TextColors.RED, "You are in the following parties:\n",
                TextColors.GOLD, parties.stream().map(party -> party.name).collect(Collectors.joining(", ")),
                TextColors.RED, "\nYou must pick one by using ", command
        ));
    }

    static void listCurrentTeleportationRequestsToPlayer(Player player, List<? extends TeleportationRequest> requests, PartyRepository<? extends Party> partyRepository) {
        CompletableFuture.runAsync(() -> {
            player.sendMessage(Text.of(
                    PluginInfo.PluginPrefix, TextColors.RED, "You have teleportation requests from the following parties:\n",
                    TextColors.GOLD, requests.stream().map(request -> partyRepository.getOne(request.targetPartyId).join().map(party -> party.name).orElse(null)).filter(Objects::nonNull).collect(Collectors.joining(", ")),
                    TextColors.RED, "\nYou must pick one by using /p accept <name>"
            ));
        });
    }

    static <T, U> void handleManyOptional(Player player, Player targetPlayer, List<T> list, Function<T, Optional<U>> toOptional, TriConsumer<U, Player, Player> ifOne, Text ifNone, Runnable ifMany) {
        if (list.size() == 0) {
            player.sendMessage(ifNone);
        } else if (list.size() == 1) {
            Optional<U> optional = toOptional.apply(list.get(0));
            if (optional.isPresent()) {
                ifOne.accept(optional.get(), player, targetPlayer);
            } else {
                player.sendMessage(Text.of(TextColors.RED, "An error occurred"));
            }
        } else {
            ifMany.run();
        }
    }

    static <T, U> void handleManyOptional(Player player, List<T> list, Function<T, Optional<U>> toOptional, BiConsumer<U, Player> ifOne, Text ifNone, Runnable ifMany) {
        if (list.size() == 0) {
            player.sendMessage(ifNone);
        } else if (list.size() == 1) {
            Optional<U> optional = toOptional.apply(list.get(0));
            if (optional.isPresent()) {
                ifOne.accept(optional.get(), player);
            } else {
                player.sendMessage(Text.of(TextColors.RED, "An error occurred"));
            }
        } else {
            ifMany.run();
        }
    }

    static <T> Optional<T> handleManyOptional(Player player, List<T> list, Text ifNone, Runnable ifMany) {
        if (list.size() == 0) {
            player.sendMessage(ifNone);
        } else if (list.size() == 1) {
            return Optional.of(list.get(0));
        } else {
            ifMany.run();
        }
        return Optional.empty();
    }

    static void handleMultiplePartyCommand(Supplier<Optional<String>> optionalNameSupplier, Player player, BiConsumer<String, Player> ifOne, PartyRepository<? extends Party> partyRepository, String command) {
        Optional<String> optionalName = optionalNameSupplier.get();
        if (optionalName.isPresent()) {
            ifOne.accept(optionalName.get(), player);
        } else {
            partyRepository.getAllForMember(player.getUniqueId()).thenAcceptAsync(parties -> handleManyOptional(player, parties, party -> Optional.of(party.name), ifOne, Text.of(TextColors.RED, "You are not currently in a ", partyRepository.getDefaultIdentifierSingularLower()), () -> listCurrentPartiesToPlayer(player, parties, command)));
        }
    }

}
