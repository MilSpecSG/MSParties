package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.core.Rank;

import java.util.*;

public class PartyMembersCommand<P extends Party, M extends Member> implements CommandExecutor {

    protected PartyRepository<P> partyRepository;
    protected MemberRepository<M> memberRepository;

    @Inject
    public PartyMembersCommand(PartyRepository<P> partyRepository, MemberRepository<M> memberRepository) {
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
                player.sendMessage(Text.of(TextColors.RED, "You are not currently in a " + partyRepository.getDefaultIdentifierSingularLower() + ", to see members of another " + partyRepository.getDefaultIdentifierSingularLower() + " use:\n/" + partyRepository.getDefaultIdentifierSingularLower() + " members <name>"));
            }
        });
    }

    protected void printParty(Party party, CommandSource source) {
        partyRepository.getUserNameRankMap(party).thenAcceptAsync(map -> {
            List<Text> textList = new ArrayList<>();

            for (String name : map.keySet()) {
                Rank rank = map.get(name);
                textList.add(Text.of(rank.getColor(), name, " - ", rank.index ," - (", rank.name, ")"));
            }
            textList.sort(Text::compareTo);

            Optional<PaginationService> paginationService = Sponge.getServiceManager().provide(PaginationService.class);
            if (!paginationService.isPresent()) return;
            PaginationList.Builder paginationBuilder = paginationService.get().builder().title(PluginInfo.PluginPrefix.concat(party.getTitle())).padding(Text.of(TextColors.GRAY, "=")).contents(textList).linesPerPage(10);
            paginationBuilder.build().sendTo(source);
        });
    }
}
