package rocks.milspecsg.msparties.service.party;

import rocks.milspecsg.msparties.model.exceptions.*;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.api.party.BasePartyRepository;

import java.util.List;
import java.util.UUID;

public class PartyRepository<T> implements BasePartyRepository<T> {

    @Override
    public Party createParty(String name, T leader) {
        return null;
    }

    @Override
    public Party disbandParty() {
        return null;
    }

    @Override
    public void joinParty(T user, Party party) throws PartyFullException, BannedFromPartyException {

    }

    @Override
    public void leaveParty(T user, Party party) throws CannotLeavePartyAsLeaderException, NotInPartyException {

    }

    @Override
    public void renameParty(String name, Party party) throws IllegalNameException, NotInPartyException {

    }

    @Override
    public void invitePlayerToParty(T user) throws NotInPartyException {

    }

    @Override
    public Party getParty(String name) throws PartyNotFoundException {
        return null;
    }

    @Override
    public Party getParty(UUID uuid) throws PartyNotFoundException {
        return null;
    }

    @Override
    public Party getParty(T leader) throws PartyNotFoundException {
        return null;
    }

    @Override
    public List<Party> getParties(T user) {
        return null;
    }
}
