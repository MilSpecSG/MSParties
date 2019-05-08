package rocks.milspecsg.msparties.service.party;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.Dbo;
import rocks.milspecsg.msparties.model.ObjectWithId;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.exceptions.*;
import rocks.milspecsg.msparties.model.results.UpdateResult;
import rocks.milspecsg.msparties.service.ApiRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ApiPartyRepository extends ApiRepository<Party> implements PartyRepository {

    @Inject
    public ApiPartyRepository(MongoContext mongoContext) {
        super(mongoContext);
    }

    @Override
    public CompletableFuture<Optional<Party>> createParty(String name, User leader) throws InvalidNameException {
        // todo: name verification
        Party party = new Party();
        party.name = name;
        party.leaderUUID = leader.getUniqueId();
        return insertOneAsync(party);
    }

    @Override
    public CompletableFuture<UpdateResult> disbandParty() {
        return null;
    }

    @Override
    public CompletableFuture<UpdateResult> joinParty(User user, Party party) throws PartyFullException, BannedFromPartyException {
        return null;
    }

    @Override
    public CompletableFuture<UpdateResult> leaveParty(User user, Party party) throws CannotLeavePartyAsLeaderException, NotInPartyException {
        return null;
    }

    @Override
    public CompletableFuture<UpdateResult> renameParty(String name, Party party) throws IllegalNameException, NotInPartyException {
        return null;
    }

    @Override
    public CompletableFuture<UpdateResult> invitePlayerToParty(User user) throws NotInPartyException {
        return null;
    }

    @Override
    public CompletableFuture<Optional<Party>> getOneAsync(String name) throws PartyNotFoundException {
        return null;
    }

    @Override
    public CompletableFuture<Optional<Party>> getOneAsync(User leader) throws PartyNotFoundException {
        return null;
    }

    @Override
    public CompletableFuture<List<Party>> getParties(User user) {
        return null;
    }

    @Override
    public CompletableFuture<Party> getOneAsync(ObjectId id) {
        return null;
    }

    @Override
    public Query<Party> asQuery() {
        return mongoContext.datastore.createQuery(Party.class);
    }
}
