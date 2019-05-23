package rocks.milspecsg.msparties.service.party;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandSource;
import rocks.milspecsg.msparties.api.party.PartyNameCacheService;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ApiPartyNameCacheService implements PartyNameCacheService {

    protected List<String> partyNames;

    @Inject
    public ApiPartyNameCacheService() {
        this.partyNames = new ArrayList<>();
    }

    @Override
    public List<String> getPartyNames() {
        return this.partyNames;
    }

    @Override
    public void addPartyName(String name) {
        this.partyNames.add(name);
    }

    @Override
    public void setPartyName(String oldName, String newName) {
        removePartyName(oldName);
        addPartyName(newName);
    }

    @Override
    public void removePartyName(String name) {
        this.partyNames.remove(name);
    }

    @Override
    public Iterable<String> getSuggestions(CommandSource commandSource) {
        return partyNames;
    }
}
