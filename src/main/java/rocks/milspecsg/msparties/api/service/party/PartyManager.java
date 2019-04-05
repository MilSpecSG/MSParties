package rocks.milspecsg.msparties.api.service.party;

import rocks.milspecsg.msparties.api.exception.*;
import rocks.milspecsg.msparties.api.model.Party;

import java.util.List;
import java.util.UUID;

public interface PartyManager<TUser> {

    Party createParty(String name, TUser leader);

    Party disbandParty();

    void joinParty(TUser user, Party party) throws PartyFullException, BannedFromPartyException;

    void leaveParty(TUser user, Party party) throws CannotLeavePartyAsLeaderException, NotInPartyException;

    void renameParty(String name, Party party) throws IllegalNameException, NotInPartyException;

    void invitePlayerToParty(TUser user) throws NotInPartyException;

    Party getParty(String name) throws PartyNotFoundException;

    Party getParty(UUID uuid) throws PartyNotFoundException;

    Party getParty(TUser leader) throws PartyNotFoundException;

    List<Party> getParties(TUser user);
}
