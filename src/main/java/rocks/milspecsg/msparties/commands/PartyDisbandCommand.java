package rocks.milspecsg.msparties.commands;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import rocks.milspecsg.msparties.api.party.PartyRepository;

public class PartyDisbandCommand implements CommandExecutor {

    private PartyRepository partyRepository;

    @Inject
    public PartyDisbandCommand(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public CommandResult execute(@Root CommandSource src, CommandContext args) throws CommandException {
        return null;
    }
}
