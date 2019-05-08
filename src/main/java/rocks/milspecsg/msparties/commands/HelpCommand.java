package rocks.milspecsg.msparties.commands;

import com.google.common.collect.Lists;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import rocks.milspecsg.msparties.MSParties;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HelpCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Map<List<String>, CommandSpec> commands = MSParties.subCommands;

        List<Text> helpList = Lists.newArrayList();

        for(List<String> aliases : commands.keySet()){
            CommandSpec commandSpec = commands.get(aliases);

            if (!commandSpec.getShortDescription(source).isPresent()) continue;;

            String replace = aliases.toString().replace("[", "").replace("]", "");
            Text commandHelp = Text.builder()
                    .append(Text.builder()
                            .append(Text.of(TextColors.GREEN, "/party " + replace))
                            .build())
                    .append(Text.builder()
                            .append(Text.of(TextColors.GOLD, " - " + commandSpec.getShortDescription(source).get().toPlain() + "\n"))
                            .build())
                    .append(Text.builder()
                            .append(Text.of(TextColors.GRAY, "Usage: /party " + replace + " " + commandSpec.getUsage(source).toPlain()))
                            .build())
                    .build();

            helpList.add(commandHelp);
        }

        helpList.sort(Text::compareTo);



        Optional<PaginationService> paginationService = Sponge.getServiceManager().provide(PaginationService.class);
        if (!paginationService.isPresent()) return null;
        PaginationList.Builder paginationBuilder = paginationService.get().builder().title(Text.of(TextColors.GOLD, "MSParties - MilspecSG")).padding(Text.of(TextColors.DARK_GREEN, "-")).contents(helpList).linesPerPage(10);
        paginationBuilder.sendTo(source);
        return CommandResult.success();
    }
}
