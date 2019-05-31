package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.core.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PartyListCommand<P extends Party> implements CommandExecutor {

    protected PartyRepository<P> partyRepository;


    @Inject
    public PartyListCommand(PartyRepository<P> partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalSearch = context.getOne(Text.of("search"));


        if (optionalSearch.isPresent()) {
            partyRepository.getAllContains(optionalSearch.get()).thenAcceptAsync(parties -> handle(parties, source));
        } else {
            partyRepository.getAll().thenAcceptAsync(parties -> handle(parties, source));
        }

        return CommandResult.success();
    }

    private void handle(List<? extends Party> parties, CommandSource source) {
        List<Text> textList = new ArrayList<>();

        for (Party party : parties) {
            textList.add(party.getTitle());
        }
        textList.sort(Text::compareTo);

        Optional<PaginationService> paginationService = Sponge.getServiceManager().provide(PaginationService.class);
        if (!paginationService.isPresent()) return;
        PaginationList.Builder paginationBuilder = paginationService.get().builder().title(PluginInfo.PluginPrefix.concat(Text.of("List parties"))).padding(Text.of(TextColors.GRAY, "=")).contents(textList).linesPerPage(10);
        paginationBuilder.build().sendTo(source);
    }
}
