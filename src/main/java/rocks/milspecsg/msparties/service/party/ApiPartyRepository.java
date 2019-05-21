package rocks.milspecsg.msparties.service.party;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.ConfigKeys;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.party.NameVerificationService;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.exceptions.*;
import rocks.milspecsg.msparties.model.results.CreateResult;
import rocks.milspecsg.msparties.model.results.UpdateResult;
import rocks.milspecsg.msparties.service.ApiRepository;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ApiPartyRepository extends ApiRepository<Party> implements PartyRepository {

    protected ConfigurationService configurationService;
    protected NameVerificationService nameVerificationService;

    private List<String> cachedPartyNames;

    @Inject
    public ApiPartyRepository(MongoContext mongoContext, ConfigurationService configurationService, NameVerificationService nameVerificationService) {
        super(mongoContext);
        this.configurationService = configurationService;
        this.nameVerificationService = nameVerificationService;
        cachedPartyNames = new ArrayList<>();
    }

    @Override
    public CompletableFuture<CreateResult<? extends Party>> createParty(String name, User leader) {
        // todo: name verification

        return CompletableFuture.supplyAsync(() -> {

            // make sure name passes filters

            // check if name already exists
            List<? extends Party> existing = getAll(name).join();
            if (existing.size() > 0) {
                return new CreateResult<>("Party with name \'" + name + "\' already exists");
            }

            List<? extends Party> alreadyIn = getAll(leader.getUniqueId()).join();
            int maxParties = configurationService.getConfigInteger(ConfigKeys.PLAYER_MAX_PARTIES_INT);
            if (maxParties >= 0 && alreadyIn.size() >= maxParties) {
                return maxParties == 1
                        ? new CreateResult<>("You are already in a party")
                        : new CreateResult<>("You are already in " + alreadyIn.size() + " parties. Maximum: " + maxParties);
            }


            Party party = new Party();
            party.name = name;
            party.leaderUUID = leader.getUniqueId();
            party.memberUUIDs = Collections.singletonList(party.leaderUUID);
            return new CreateResult<Party>(insertOneAsync(party).join(), "An error occurred");
        });

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
    public CompletableFuture<UpdateResult> inviteUser(User user) throws NotInPartyException {
        return null;
    }

    @Override
    public CompletableFuture<List<? extends Party>> getAllContains(String name) {
        return CompletableFuture.supplyAsync(() -> asQuery().field("name").containsIgnoreCase(name).asList());
    }

    @Override
    public CompletableFuture<Optional<? extends Party>> getOneContains(String name) {
        return CompletableFuture.supplyAsync(() -> {
            List<? extends Party> parties = getAllContains(name).join();
            return parties.size() > 0 ? Optional.of(parties.get(0)) : Optional.empty();
        });
    }

    @Override
    public CompletableFuture<List<? extends Party>> getAll(String name) {
        return CompletableFuture.supplyAsync(() -> asQuery().field("name").equalIgnoreCase(name).asList());

    }

    @Override
    public CompletableFuture<Optional<? extends Party>> getOne(String name) {
        return CompletableFuture.supplyAsync(() -> {
            List<? extends Party> parties = getAll(name).join();
            return parties.size() > 0 ? Optional.of(parties.get(0)) : Optional.empty();
        });
    }

    @Override
    public CompletableFuture<List<? extends Party>> getAll(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> asQuery().field("memberUUIDs").hasThisOne(userUUID).asList());
    }

    @Override
    public CompletableFuture<Optional<? extends Party>> getOne(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> {
            List<? extends Party> parties = getAll(userUUID).join();
            return parties.size() > 0 ? Optional.of(parties.get(0)) : Optional.empty();
        });
    }

    @Override
    public CompletableFuture<Optional<? extends Party>> getOneAsync(ObjectId id) {
        return CompletableFuture.supplyAsync(Optional::empty);
    }

    @Override
    public UpdateOperations<Party> createUpdateOperations() {
        return mongoContext.datastore.createUpdateOperations(Party.class);
    }

    @Override
    public Query<Party> asQuery() {
        return mongoContext.datastore.createQuery(Party.class);
    }
}
