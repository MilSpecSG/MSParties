package rocks.milspecsg.msparties.api.party;

import rocks.milspecsg.msparties.model.core.Party;

import java.util.Map;
import java.util.Set;

public interface PartyCacheService {

    Map<Party, Long> getPartiesMap();

    Set<Party> getParties();

    void addParty(Party party);

    void removeParty(Party party);

}
