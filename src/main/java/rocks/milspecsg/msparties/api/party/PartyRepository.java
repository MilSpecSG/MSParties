package rocks.milspecsg.msparties.api.party;

import org.spongepowered.api.entity.living.player.User;
import rocks.milspecsg.msparties.api.Repository;
import rocks.milspecsg.msparties.model.exceptions.*;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.results.UpdateResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PartyRepository extends Repository<Party> {

    /**
     *
     * @param name Of party
     * @param leader of party
     * @return {@code Optional} wrapped {@code Party}
     * @throws InvalidNameException When name does not successfully pass language filter
     */
    CompletableFuture<Optional<? extends Party>> createParty(String name, User leader) throws InvalidNameException;

    CompletableFuture<UpdateResult> disbandParty();

    CompletableFuture<UpdateResult> joinParty(User user, Party party) throws PartyFullException, BannedFromPartyException;

    CompletableFuture<UpdateResult> leaveParty(User user, Party party) throws CannotLeavePartyAsLeaderException, NotInPartyException;

    CompletableFuture<UpdateResult> renameParty(String name, Party party) throws IllegalNameException, NotInPartyException;

    CompletableFuture<UpdateResult> inviteUser(User user) throws NotInPartyException;

    CompletableFuture<List<? extends Party>> getAll(String name);
    CompletableFuture<Optional<? extends Party>> getOne(String name);

    /**
     * @param userUUID to check
     * @return a list with users's parties. Empty if none
     */
    CompletableFuture<List<? extends Party>> getAll(UUID userUUID);

    CompletableFuture<Optional<? extends Party>> getOne(UUID userUUID);



   // CompletableFuture<List<Party>> getParties(User user);
}
