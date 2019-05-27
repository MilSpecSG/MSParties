package rocks.milspecsg.msparties.service.party;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msparties.ConfigKeys;
import rocks.milspecsg.msparties.PluginInfo;
import rocks.milspecsg.msparties.api.Repository;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.member.MemberRepository;
import rocks.milspecsg.msparties.api.party.*;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.Dbo;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.core.Rank;
import rocks.milspecsg.msparties.model.results.CreateResult;
import rocks.milspecsg.msparties.model.results.PermissibleResult;
import rocks.milspecsg.msparties.model.results.UpdateResult;
import rocks.milspecsg.msparties.service.ApiRepository;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ApiPartyRepository extends ApiRepository<Party> implements PartyRepository {

    protected ConfigurationService configurationService;
    protected NameVerificationService nameVerificationService;
    protected PermissionCacheService permissionCacheService;
    protected PartyCacheService partyCacheService;
    protected MemberRepository memberRepository;
    protected PartyInvitationCacheService partyInvitationCacheService;

    @Inject
    public ApiPartyRepository(
            MongoContext mongoContext,
            ConfigurationService configurationService,
            NameVerificationService nameVerificationService,
            PermissionCacheService permissionCacheService,
            PartyCacheService partyCacheService,
            MemberRepository memberRepository,
            PartyInvitationCacheService partyInvitationCacheService) {
        super(mongoContext);
        this.configurationService = configurationService;
        this.nameVerificationService = nameVerificationService;
        this.permissionCacheService = permissionCacheService;
        this.partyCacheService = partyCacheService;
        this.memberRepository = memberRepository;
        this.partyInvitationCacheService = partyInvitationCacheService;
    }


    @Override
    public CompletableFuture<CreateResult<? extends Party>> createParty(String name, String tag, User leader) {
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
            party.tag = tag;
            party.leaderUUID = leader.getUniqueId();
            party.inviteOnly = false;
            party.members = new HashMap<>();
            party.members.put(optionalMember.get().getId(), Integer.MAX_VALUE);

            // ==== generate default ranks ==== //
            resetRanks(party).join();

            // ==== generate default permissions ==== //
            resetPermissions(party).join();

            return new CreateResult<Party>(insertOne(party).join(), "An error occured");
        });
    }

    @Override
    public CompletableFuture<PermissibleResult> disbandParty(String name, User user) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UUID> leaderUUID = getLeaderUUID(name).join();
            if (!leaderUUID.isPresent()) {
                return PermissibleResult.fail("An error occurred");
            }

            Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "LeaderUUID: ", leaderUUID.get()));
            Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PluginPrefix, "UserUUID: ", user.getUniqueId()));


            if (!leaderUUID.get().equals(user.getUniqueId())) {
                return PermissibleResult.fail("You must be party leader to disband");
            }

//            if (!newLeader.isPresent() && party.members.keySet().size() > 1) {
//                return PermissibleResult.fail("You must specify a new leader, or the party must be empty");
//            }
            return mongoContext.datastore.delete(asQuery(name)).getN() > 0
                    ? PermissibleResult.success(Text.of(TextColors.GREEN, "Successfully abandoned ", TextColors.YELLOW, name))
                    : PermissibleResult.fail("An error occurred");
        });
    }

    @Override
    public CompletableFuture<PermissibleResult> joinParty(String name, User user) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Boolean> hasInvitation = getId(name).join().map(id -> partyInvitationCacheService.hasInvitation(id, user.getUniqueId()));
            if (!getOne(name).join().map(p -> p.inviteOnly).orElse(true) || hasInvitation.isPresent() && hasInvitation.get()) {

                Optional<ObjectId> optionalMemberId = memberRepository.getId(user.getUniqueId()).join();

                if (!optionalMemberId.isPresent()) {
                    return PermissibleResult.fail("An error occurred");
                }

                if (isIn(name, user).join().orElse(false)) {
                    return PermissibleResult.fail("You are already in this party");
                }

                return memberRepository.getId(user.getUniqueId()).join().map(objectId -> mongoContext.datastore.update(asQuery(name), createUpdateOperations().set("members." + objectId, 0)).getUpdatedCount() > 0
                        ? PermissibleResult.success(Text.of(TextColors.GREEN, "Successfully joined ", TextColors.YELLOW, name))
                        : PermissibleResult.fail("An error occurred")).orElse(PermissibleResult.fail());
            } else {
                return PermissibleResult.fail(Text.of(TextColors.RED, "You do not have an invitation for ", TextColors.YELLOW, name));
            }
        });
    }

    @Override
    public CompletableFuture<PermissibleResult> leaveParty(String name, User user, Optional<User> newLeader) {
        return CompletableFuture.supplyAsync(PermissibleResult::fail);
    }

    @Override
    public CompletableFuture<PermissibleResult> nameParty(String name, String newName, User user) {
        return CompletableFuture.supplyAsync(PermissibleResult::fail);
    }

    @Override
    public CompletableFuture<PermissibleResult> tagParty(String name, String newTag, User user) {
        return CompletableFuture.supplyAsync(PermissibleResult::fail);
    }

    @Override
    public CompletableFuture<PermissibleResult> inviteUser(String name, User user, Player targetPlayer) {
        return CompletableFuture.supplyAsync(() -> getOne(name).join().map(party -> {
            if (check(party.getId(), PermissionCacheService.INVITE, user).join()) {

                partyInvitationCacheService.put(party.getId(), targetPlayer.getUniqueId());

                return PermissibleResult.success(Text.of(TextColors.GREEN, "Successfully invited ", TextColors.YELLOW, targetPlayer.getName(), TextColors.GREEN, " to ", party.name));
            } else {
                return PermissibleResult.fail();
            }
        }).orElse(PermissibleResult.fail()));
    }

    @Override
    public CompletableFuture<Optional<? extends Party>> getOne(ObjectId id) {
        return CompletableFuture.supplyAsync(ifNotPresent(partyCacheService, id));
    }

    @Override
    public CompletableFuture<List<? extends Party>> getAllContains(String name) {
        return CompletableFuture.supplyAsync(() -> asQuery().field("name").containsIgnoreCase(name).asList());
    }

    @Override
    public CompletableFuture<Optional<? extends Party>> getOneContains(String name) {
        return CompletableFuture.supplyAsync(ifNotPresent(partyCacheService, service -> service.getOne(party -> party.name.toLowerCase().contains(name.toLowerCase())), () -> getAllContains(name).thenApplyAsync(Repository.single()).join()));
    }

    @Override
    public CompletableFuture<List<? extends Party>> getAll(String name) {
        return CompletableFuture.supplyAsync(() -> asQuery().field("name").equalIgnoreCase(name).asList());
    }

    @Override
    public CompletableFuture<Optional<? extends Party>> getOne(String name) {
        return CompletableFuture.supplyAsync(ifNotPresent(partyCacheService, service -> service.getOne(party -> party.name.equals(name)), () -> getAllContains(name).thenApplyAsync(Repository.single()).join()));
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
        return CompletableFuture.supplyAsync(saveToCache(partyCacheService, () -> asQuery().field("members." + id.toString()).exists().asList()));
    }

    @Override
    public CompletableFuture<Optional<? extends Party>> getOneForMember(ObjectId id) {
        return getAllForMember(id).thenApplyAsync(Repository.single());
    }

    //    protected static <T> CompletableFuture<List<T>> toList(List<CompletableFuture<T>> com) {
//        return CompletableFuture.allOf(com.toArray(new CompletableFuture[0]))
//                .thenApplyAsync(v -> com.stream()
//                        .map(CompletableFuture::join)
//                        .collect(Collectors.toList())
//                );
//    }


    @Override
    public CompletableFuture<Optional<Boolean>> isIn(String name, User user) {
        return CompletableFuture.supplyAsync(() -> memberRepository.getOneOrGenerate(user.getUniqueId()).join().map(member -> getAll(name).join().stream().anyMatch(party -> party.members.containsKey(member.getId()))));
    }

    @Override
    public CompletableFuture<Map<String, Rank>> getUserNameRankMap(Party party) {
        //CompletableFuture<List<Optional<? extends Member>>> completableFuture = toList(party.memberRankMap.keySet().stream().map(u -> memberRepository.getOne(u)).collect(Collectors.toList()));

        return CompletableFuture.supplyAsync(() -> {
//            Map<Integer, Rank> rankIndexNameMap = new HashMap<>();
//            party.ranks.forEach(rank -> rankIndexNameMap.put(rank.index, rank));
            Map<String, Rank> userRankMap = new HashMap<>();
            party.members.keySet().forEach(id -> memberRepository.getUUID(id).join().ifPresent(uuid -> memberRepository.getUser(uuid)
                    .ifPresent(user -> party.ranks.stream().filter(rank -> rank.index == party.members.get(id)).findFirst().ifPresent(rank -> userRankMap.put(user.getName(), rank)))));

            //list.forEach(m -> m.ifPresent(member -> memberRepository.getUser(m.getRequiredRankIndex().userUUID).ifPresent(user -> userRankMap.put(user.getName(), rankIndexNameMap.getRequiredRankIndex(member.rankIndex)))));
            return userRankMap;
        });
    }

    @Override
    public CompletableFuture<Optional<UUID>> getLeaderUUID(String name) {
        return CompletableFuture.supplyAsync(() -> getOne(name).join().map(p -> p.leaderUUID));

    }

    @Override
    public CompletableFuture<Optional<User>> getLeader(String name) {
        return CompletableFuture.supplyAsync(() -> getLeaderUUID(name).join().flatMap(memberRepository::getUser));
    }

    @Override
    public UpdateOperations<Party> createUpdateOperations() {
        return mongoContext.datastore.createUpdateOperations(Party.class);
    }

    @Override
    public Query<Party> asQuery() {
        return mongoContext.datastore.createQuery(Party.class);
    }

    private List<Rank> getDefaultRanks() {
        List<Rank> result = new ArrayList<>();
        Rank leaderRank = new Rank();
        leaderRank.color = "gold";
        leaderRank.index = Integer.MAX_VALUE;
        leaderRank.name = "Leader";
        result.add(leaderRank);

        Rank coLeaderRank = new Rank();
        coLeaderRank.color = "red";
        coLeaderRank.index = 10;
        coLeaderRank.name = "Co-Leader";
        result.add(coLeaderRank);

        Rank elderRank = new Rank();
        elderRank.color = "blue";
        elderRank.index = 5;
        elderRank.name = "Elder";
        result.add(elderRank);
        return result;
    }

    @Override
    public CompletableFuture<Party> resetRanks(Party party) {
        //TODO: load from config
        return CompletableFuture.supplyAsync(() -> {
            party.ranks = getDefaultRanks();
            return party;
        });
    }

    @Override
    public CompletableFuture<UpdateResult> resetRanks(ObjectId id) {
        return CompletableFuture.supplyAsync(() -> {
            // party in cache
            partyCacheService.getOne(id).ifPresent(this::resetRanks);

            // party in db
            return new UpdateResult(mongoContext.datastore.update(asQuery(id), createUpdateOperations().set("ranks", getDefaultRanks())));
        });
    }

    @Override
    public CompletableFuture<UpdateResult> resetRanks(String name) {
        return CompletableFuture.supplyAsync(() -> {
            // party in cache
            partyCacheService.getOne(name).ifPresent(this::resetRanks);

            // party in db
            return new UpdateResult(mongoContext.datastore.update(asQuery(name), createUpdateOperations().set("ranks", getDefaultRanks())));
        });
    }

    private Map<String, Integer> getDefaultPermissions() {
        Map<String, Integer> result = new HashMap<>();
        result.put(PermissionCacheService.INVITE, 5);
        result.put(PermissionCacheService.EDIT_PRIVACY, 10);
        result.put(PermissionCacheService.EDIT_NAME, 20);
        result.put(PermissionCacheService.EDIT_TAG, 20);
        result.put(PermissionCacheService.EDIT_MAX_SIZE, 20);
        result.put(PermissionCacheService.EDIT_RANKS, 20);
        result.put(PermissionCacheService.EDIT_PERMISSIONS, 100);
        result.put(PermissionCacheService.ASSIGN_RANKS, 10);
        result.put(PermissionCacheService.KICK, 5);
        result.put(PermissionCacheService.BAN, 10);
        return result;
    }

    @Override
    public CompletableFuture<Party> resetPermissions(Party party) {
        //TODO: load from config
        return CompletableFuture.supplyAsync(() -> {
            party.permissions = getDefaultPermissions();
            return party;
        });
    }

    @Override
    public CompletableFuture<UpdateResult> resetPermissions(ObjectId id) {
        return CompletableFuture.supplyAsync(() -> {
            // party in cache
            partyCacheService.getOne(id).ifPresent(this::resetPermissions);

            // party in db
            return new UpdateResult(mongoContext.datastore.update(asQuery(id), createUpdateOperations().set("permissions", getDefaultPermissions())));
        });
    }

    @Override
    public CompletableFuture<UpdateResult> resetPermissions(String name) {
        return CompletableFuture.supplyAsync(() -> {
            // party in cache
            partyCacheService.getOne(name).ifPresent(this::resetPermissions);

            // party in db
            return new UpdateResult(mongoContext.datastore.update(asQuery(name), createUpdateOperations().set("permissions", getDefaultPermissions())));
        });
    }

    @Override
    public CompletableFuture<Optional<Integer>> getRankIndex(ObjectId id, User user) {
        return CompletableFuture.supplyAsync(() -> getOne(id).join().flatMap(party -> memberRepository.getId(user.getUniqueId()).join().map(memberId -> party.members.get(memberId))));
    }

    @Override
    public CompletableFuture<Boolean> check(ObjectId id, String permission, Integer rankIndex) {
        return CompletableFuture.supplyAsync(() -> (permissionCacheService.checkSet(id, permission) ? permissionCacheService.getRankIndex(id, permission) : getRequiredRankIndex(id, permission).join().orElse(Integer.MAX_VALUE)) <= rankIndex);
    }

    @Override
    public CompletableFuture<Boolean> check(ObjectId id, String permission, User user) {
        return CompletableFuture.supplyAsync(() -> check(id, permission, getRankIndex(id, user).join().orElse(0)).join());
    }

    @Override
    public CompletableFuture<Optional<ObjectId>> getId(String name) {
        return CompletableFuture.supplyAsync(() -> getOne(name).join().map(Dbo::getId));
//        return CompletableFuture.supplyAsync(() -> {
//            return partyCacheService.getOne(name).map(party -> Optional.of(party.getId())).orElseGet(() -> Optional.ofNullable(asQuery(name).project("_id", true).get().getId()));
//        });
    }

    @Override
    public Query<Party> asQuery(String name) {
        return asQuery().field("name").equalIgnoreCase(name);
    }

    public CompletableFuture<Optional<Integer>> getRequiredRankIndex(ObjectId id, String permission) {
        return CompletableFuture.supplyAsync(() -> partyCacheService.getOne(id).map(party -> Optional.of((party.permissions.get(permission))).orElseGet(() -> getOne(id).join().map(p -> p.permissions.get(permission)).orElse(Integer.MAX_VALUE))));
    }
}
