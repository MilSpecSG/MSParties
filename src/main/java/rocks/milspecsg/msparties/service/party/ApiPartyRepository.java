package rocks.milspecsg.msparties.service.party;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.spongepowered.api.entity.living.player.User;
import rocks.milspecsg.msparties.ConfigKeys;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.party.NameVerificationService;
import rocks.milspecsg.msparties.api.party.PartyRepository;
import rocks.milspecsg.msparties.api.party.PermissionCacheService;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.core.Rank;
import rocks.milspecsg.msparties.model.exceptions.*;
import rocks.milspecsg.msparties.model.results.CreateResult;
import rocks.milspecsg.msparties.model.results.UpdateResult;
import rocks.milspecsg.msparties.service.ApiRepository;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ApiPartyRepository extends ApiRepository<Party> implements PartyRepository {

    protected ConfigurationService configurationService;
    protected NameVerificationService nameVerificationService;
    protected MemberRepository memberRepository;

    private List<String> cachedPartyNames;

    @Inject
    public ApiPartyRepository(MongoContext mongoContext, ConfigurationService configurationService, NameVerificationService nameVerificationService, MemberRepository memberRepository) {
        super(mongoContext);
        this.configurationService = configurationService;
        this.nameVerificationService = nameVerificationService;
        this.memberRepository = memberRepository;
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


            List<? extends Party> alreadyIn = getAllForMember(leader.getUniqueId()).join();
            int maxParties = configurationService.getConfigInteger(ConfigKeys.PLAYER_MAX_PARTIES_INT);
            if (maxParties >= 0 && alreadyIn.size() >= maxParties) {
                return maxParties == 1
                        ? new CreateResult<>("You are already in a " + getDefaultIdentifierSingularLower())
                        : new CreateResult<>("You are already in " + alreadyIn.size() + " parties. Maximum: " + maxParties);
            }


            Optional<? extends Member> optionalMember = memberRepository.getOneOrGenerate(leader.getUniqueId()).join();
            if (!optionalMember.isPresent()) return new CreateResult<>("Error creating/getting member from database");

            Party party = new Party();
            party.name = name;
            party.leaderUUID = leader.getUniqueId();
            party.memberRankMap = new HashMap<>();
            party.memberRankMap.put(optionalMember.get().getId(), Integer.MAX_VALUE);

            // ==== generate default ranks ==== //
            resetRanks(party).join();

            // ==== generate default permissions ==== //
            resetPermissions(party).join();

            return new CreateResult<Party>(insertOne(party).join(), "An error occurred");
        });
    }

    @Override
    public CompletableFuture<UpdateResult> disbandParty(Party party, User user) {
        return CompletableFuture.supplyAsync(UpdateResult::fail);
    }

    @Override
    public CompletableFuture<UpdateResult> joinParty(Party party, User user) {
        return CompletableFuture.supplyAsync(UpdateResult::fail);
    }

    @Override
    public CompletableFuture<UpdateResult> leaveParty(Party party, User user)  {
        return CompletableFuture.supplyAsync(UpdateResult::fail);
    }

    @Override
    public CompletableFuture<UpdateResult> renameParty(Party party, String newName)  {
        return CompletableFuture.supplyAsync(UpdateResult::fail);
    }

    @Override
    public CompletableFuture<UpdateResult> retagParty(Party party, String newTag) {
        return CompletableFuture.supplyAsync(UpdateResult::fail);
    }

    @Override
    public CompletableFuture<UpdateResult> inviteUser(Party party, User user) {
        return CompletableFuture.supplyAsync(UpdateResult::fail);
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
    public CompletableFuture<List<? extends Party>> getAllForMember(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> memberRepository.getId(userUUID).join().map(id -> getAllForMember(id).join()).orElse(new ArrayList<>()));
    }

    @Override
    public CompletableFuture<Optional<? extends Party>> getOneForMember(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> memberRepository.getId(userUUID).join().flatMap(id -> getOneForMember(id).join()));
    }

    @Override
    public CompletableFuture<List<? extends Party>> getAllForMember(ObjectId id) {
        return CompletableFuture.supplyAsync(() -> asQuery().field("memberRankMap." + id.toString()).exists().asList());
    }

    @Override
    public CompletableFuture<Optional<? extends Party>> getOneForMember(ObjectId id) {
        return CompletableFuture.supplyAsync(() -> {
            List<? extends Party> parties = getAllForMember(id).join();
            return parties.size() > 0 ? Optional.of(parties.get(0)) : Optional.empty();
        });
    }

    //    protected static <T> CompletableFuture<List<T>> toList(List<CompletableFuture<T>> com) {
//        return CompletableFuture.allOf(com.toArray(new CompletableFuture[0]))
//                .thenApplyAsync(v -> com.stream()
//                        .map(CompletableFuture::join)
//                        .collect(Collectors.toList())
//                );
//    }

    @Override
    public CompletableFuture<Map<String, Rank>> getUserNameRankMap(Party party) {
        CompletableFuture<Map<Integer, Rank>> rankIndexMapFuture = CompletableFuture.supplyAsync(() -> {
            Map<Integer, Rank> result = new HashMap<>();
            party.ranks.forEach(rank -> result.put(rank.index, rank));
            return result;
        });

        //CompletableFuture<List<Optional<? extends Member>>> completableFuture = toList(party.memberRankMap.keySet().stream().map(u -> memberRepository.getOne(u)).collect(Collectors.toList()));

        return CompletableFuture.supplyAsync(() -> {
            Map<String, Rank> userRankMap = new HashMap<>();
            Map<Integer, Rank> rankIndexNameMap = rankIndexMapFuture.join();
            party.memberRankMap.keySet().forEach(id -> memberRepository.getUUID(id).join().ifPresent(uuid -> memberRepository.getUser(uuid).ifPresent(user -> userRankMap.put(user.getName(), rankIndexNameMap.get(party.memberRankMap.get(id))))));

            //list.forEach(m -> m.ifPresent(member -> memberRepository.getUser(m.get().userUUID).ifPresent(user -> userRankMap.put(user.getName(), rankIndexNameMap.get(member.rankIndex)))));
            return userRankMap;
        });
    }

    @Override
    public UpdateOperations<Party> createUpdateOperations() {
        return mongoContext.datastore.createUpdateOperations(Party.class);
    }

    @Override
    public Query<Party> asQuery() {
        return mongoContext.datastore.createQuery(Party.class);
    }

    @Override
    public CompletableFuture<Party> resetRanks(Party party) {
        //TODO: load from config
        return CompletableFuture.supplyAsync(() -> {
            party.ranks = new ArrayList<>();
            Rank leaderRank = new Rank();
            leaderRank.color = "gold";
            leaderRank.index = Integer.MAX_VALUE;
            leaderRank.name = "Leader";
            party.ranks.add(leaderRank);

            Rank coLeaderRank = new Rank();
            coLeaderRank.color = "red";
            coLeaderRank.index = 10;
            coLeaderRank.name = "Co-Leader";
            party.ranks.add(coLeaderRank);

            Rank elderRank = new Rank();
            elderRank.color = "blue";
            elderRank.index = 5;
            elderRank.name = "Elder";
            party.ranks.add(elderRank);
            return party;
        });
    }

    @Override
    public CompletableFuture<UpdateResult> resetRanks(ObjectId id) {
        return CompletableFuture.supplyAsync(UpdateResult::fail);
    }

    @Override
    public CompletableFuture<UpdateResult> resetRanks(String name) {
        return CompletableFuture.supplyAsync(UpdateResult::fail);
    }

    @Override
    public CompletableFuture<Party> resetPermissions(Party party) {
        //TODO: load from config
        return CompletableFuture.supplyAsync(() -> {
            party.permissionMap = new HashMap<>();
            party.permissionMap.put(PermissionCacheService.INVITE, 5);
            party.permissionMap.put(PermissionCacheService.EDIT_PRIVACY, 10);
            party.permissionMap.put(PermissionCacheService.EDIT_NAME, 20);
            party.permissionMap.put(PermissionCacheService.EDIT_TAG, 20);
            party.permissionMap.put(PermissionCacheService.EDIT_MAX_SIZE, 20);
            party.permissionMap.put(PermissionCacheService.EDIT_RANKS, 20);
            party.permissionMap.put(PermissionCacheService.EDIT_PERMISSIONS, 100);
            party.permissionMap.put(PermissionCacheService.ASSIGN_RANKS, 10);
            party.permissionMap.put(PermissionCacheService.KICK, 5);
            party.permissionMap.put(PermissionCacheService.BAN, 10);
            return party;
        });
    }

    @Override
    public CompletableFuture<UpdateResult> resetPermissions(ObjectId id) {
        return CompletableFuture.supplyAsync(UpdateResult::fail);
    }

    @Override
    public CompletableFuture<UpdateResult> resetPermissions(String name) {
        return CompletableFuture.supplyAsync(UpdateResult::fail);
    }
}
