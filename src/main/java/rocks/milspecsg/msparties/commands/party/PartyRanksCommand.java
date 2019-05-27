package rocks.milspecsg.msparties.commands.party;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import rocks.milspecsg.msparties.api.party.PartyRepository;

public class PartyRanksCommand implements CommandExecutor {

    protected PartyRepository partyRepository;

    @Inject
    public PartyRanksCommand(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return null;
    }
}
