package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;

import java.util.Optional;

public class PartyInfoCommand<P extends Party, M extends Member> implements CommandExecutor {

    private PartyRepository<P> partyRepository;
    private MemberRepository<M> memberRepository;

    @Inject
    public PartyInfoCommand(PartyRepository<P> partyRepository, MemberRepository<M> memberRepository) {
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
        } else {
            if (optionalName.isPresent()) {
                handleName(optionalName.get(), source);
                return CommandResult.success();
            } else {
                throw new CommandException(Text.of(TextColors.RED, "Missing name"));
            }
        }
    }

    private void handleName(String name, CommandSource source) {
        partyRepository.getOneContains(name).thenAcceptAsync(party -> {
            if (party.isPresent()) {
                printParty(party.get(), source);
            } else {
                source.sendMessage(Text.of(TextColors.RED, "That party does not exist"));
            }
        });
    }

    private void handleUUID(Player player) {
        partyRepository.getOneForMember(player.getUniqueId()).thenAcceptAsync(party -> {
            if (party.isPresent()) {
                printParty(party.get(), player);
            } else {
                player.sendMessage(Text.of(TextColors.RED, "You are not currently in a party, to see information of another party use:\n/party info <name>"));
            }
        });
    }

    protected void printParty(Party party, CommandSource player) {
        player.sendMessage(Text.of(
                party.getTitle(), "\n",
                TextColors.GRAY, "Leader: ", TextColors.GOLD, memberRepository.getUser(party.leaderUUID).flatMap(u -> Optional.of(u.getName())).orElse("N/A")
        ));


//        Optional<PaginationService> paginationService = Sponge.getServiceManager().provide(PaginationService.class);
//        if (!paginationService.isPresent()) return;
//        PaginationList.Builder paginationBuilder = paginationService.getRequiredRankIndex().builder().title(Text.of(TextColors.GOLD, party.name)).padding(Text.of(TextColors.DARK_GREEN, "-")).contents(helpList).linesPerPage(10);
//        paginationBuilder.build().sendTo(player);
    }


}
