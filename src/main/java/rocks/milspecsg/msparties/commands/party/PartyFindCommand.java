package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.exceptions.CommandExceptionFactory;
import rocks.milspecsg.msparties.model.exceptions.InvalidNameException;

import java.util.Optional;
import java.util.UUID;

public class PartyFindCommand implements CommandExecutor {

    private PartyRepository partyRepository;
    private MemberRepository memberRepository;

    @Inject
    public PartyFindCommand(PartyRepository partyRepository, MemberRepository memberRepository) {
        this.partyRepository = partyRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalName = context.getOne(Text.of("name"));
        if (source instanceof Player) {
            Player player = (Player) source;

            if (optionalName.isPresent()) {
                handleName(optionalName.get(), player);
            } else {
                handleUUID(player);
            }

            return CommandResult.success();
        } else if (source instanceof ConsoleSource) {
            if (optionalName.isPresent()) {
                handleName(optionalName.get(), source);
                return CommandResult.success();
            } else {
                throw new CommandException(Text.of(TextColors.RED, "Missing name"));
            }
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Command can only be run as player or console"));
        }
    }

    private void handleName(String name, CommandSource source) {
        partyRepository.getOne(name).thenAcceptAsync(party -> {
            if (party.isPresent()) {
                printParty(party.get(), source);
            } else {
                source.sendMessage(Text.of(TextColors.RED, "That party does not exist"));
            }
        });
    }

    private void handleUUID(Player player) {
        partyRepository.getOne(player.getUniqueId()).thenAcceptAsync(party -> {
            if (party.isPresent()) {
                printParty(party.get(), player);
            } else {
                player.sendMessage(Text.of(TextColors.RED, "You are not currently in a party"));
            }
        });
    }

    private void printParty(Party party, CommandSource player) {
        player.sendMessage(Text.of(TextColors.GREEN, "Party: ", party.name));
        player.sendMessage(Text.of(TextColors.DARK_GRAY, "Leader: ", memberRepository.getUser(party.leaderUUID).flatMap(u -> Optional.of(u.getName())).orElse("N/A")));


//        Optional<PaginationService> paginationService = Sponge.getServiceManager().provide(PaginationService.class);
//        if (!paginationService.isPresent()) return;
//        PaginationList.Builder paginationBuilder = paginationService.get().builder().title(Text.of(TextColors.GOLD, party.name)).padding(Text.of(TextColors.DARK_GREEN, "-")).contents(helpList).linesPerPage(10);
//        paginationBuilder.build().sendTo(player);
    }


}
