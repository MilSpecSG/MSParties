package rocks.milspecsg.msparties.api.party;

import org.bson.types.ObjectId;
import org.spongepowered.api.entity.living.player.User;
import rocks.milspecsg.msparties.api.Repository;
import rocks.milspecsg.msparties.model.core.Rank;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.results.CreateResult;
import rocks.milspecsg.msparties.model.results.UpdateResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PartyRepository extends Repository<Party> {

    /**
     * Represents the default singular identifier for a group
     * <p>
     * Should be overridden by other plugins who change the name of party.
     * Examples: "Clan", "Faction", "Guild" ... etc
     * </p>
     * <p>
     * Used in text sent to the player
     * </p>
     *
     * @return {@code "Party"} by default, otherwise the identifier specified by a subclass
     */
    default String getDefaultIdentifierSingularUpper() {
        return "Party";
    }

    /**
     * Represents the default plural identifier for a group
     * <p>
     * Should be overridden by other plugins who change the name of party.
     * Examples: "Clans", "Factions", "Guilds" ... etc
     * </p>
     * <p>
     * Used in text sent to the player
     * </p>
     *
     * @return {@code "Parties"} by default, otherwise the identifier specified by a subclass
     */
    default String getDefaultIdentifierPluralUpper() {
        return "Parties";
    }

    /**
     * Represents the default singular identifier for a group
     * <p>
     * Should be overridden by other plugins who change the name of party.
     * Examples: "clan", "faction", "guild" ... etc
     * </p>
     * <p>
     * Used in text sent to the player
     * </p>
     *
     * @return {@code "party"} by default, otherwise the identifier specified by a subclass
     *
     * <p>
     * note: this will be used as the base command
     * </p>
     */
    default String getDefaultIdentifierSingularLower() {
        return "party";
    }

    /**
     * Represents the default plural identifier for a group
     * <p>
     * Should be overridden by other plugins who change the name of party.
     * Examples: "clans", "factions", "guilds" ... etc
     * </p>
     * <p>
     * Used in text sent to the player
     * </p>
     *
     * @return {@code "parties"} by default, otherwise the identifier specified by a subclass
     */
    default String getDefaultIdentifierPluralLower() {
        return "parties";
    }

    /**
     * @param name   {@link String} Name of new party
     * @param leader {@link User} that is to be the leader of the new party
     * @return {@code Optional} wrapped {@code Party}
     */
    CompletableFuture<CreateResult<? extends Party>> createParty(String name, User leader);

    /**
     * @param party {@link Party} to be modified
     * @param user  {@link User} that is attempting to disband the party
     * @return {@link UpdateResult} with information about the modification
     */
    CompletableFuture<UpdateResult> disbandParty(Party party, User user);

    /**
     * @param party {@link Party} to be modified
     * @param user  {@link User} to add to party
     * @return {@link UpdateResult} with information about the modification
     */
    CompletableFuture<UpdateResult> joinParty(Party party, User user);

    /**
     * @param party {@link Party} to be modified
     * @param user  {@link User} to remove from party
     * @return {@link UpdateResult} with information about the modification
     */
    CompletableFuture<UpdateResult> leaveParty(Party party, User user);

    /**
     * @param party   {@link Party} to be modified
     * @param newName {@link String} Name to set
     * @return {@link UpdateResult} with information about the modification
     */
    CompletableFuture<UpdateResult> renameParty(Party party, String newName);

    /**
     * @param party  {@link Party} to be modified
     * @param newTag {@link String} Tag to set
     * @return {@link UpdateResult} with information about the modification
     */
    CompletableFuture<UpdateResult> retagParty(Party party, String newTag);

    /**
     * @param party {@link Party} to invite {@link User} to
     * @param user  {@link User} to invite to party
     * @return {@link UpdateResult} with information about the modificatio
     */
    CompletableFuture<UpdateResult> inviteUser(Party party, User user);

    /**
     * @param name {@link String} Value to check
     * @return All parties that contain {@code name} in their name
     */
    CompletableFuture<List<? extends Party>> getAllContains(String name);

    /**
     * @param name {@link String} Value to check
     * @return First party that contains {@code name} in its name
     */
    CompletableFuture<Optional<? extends Party>> getOneContains(String name);

    /**
     * @param name {@link String} Value to check
     * @return All parties that contain {@code name} in their name
     */
    CompletableFuture<List<? extends Party>> getAll(String name);

    /**
     * @param name {@link String} Value to check
     * @return First {@link Party} that contains {@code name} in its name
     */
    CompletableFuture<Optional<? extends Party>> getOne(String name);

    /**
     * @param userUUID {@link UUID} to check
     * @return a list of {@link Party} that a player with the provided {@link UUID} is in, empty if none
     */
    CompletableFuture<List<? extends Party>> getAllForMember(UUID userUUID);

    /**
     * @param userUUID {@link UUID} to check
     * @return a the first found {@link Party} that a player with the provided {@link UUID} is in
     */
    CompletableFuture<Optional<? extends Party>> getOneForMember(UUID userUUID);

    /**
     * @param id {@link ObjectId} to check
     * @return a list of {@link Party} that a player with the provided {@link ObjectId} is in, empty if none
     */
    CompletableFuture<List<? extends Party>> getAllForMember(ObjectId id);

    /**
     * @param id {@link ObjectId} to check
     * @return a the first found {@link Party} that a player with the provided {@link ObjectId} is in
     */
    CompletableFuture<Optional<? extends Party>> getOneForMember(ObjectId id);

    /**
     * @param party {@link Party} to check
     * @return A {@link Map} mapping player names with their rank
     */
    CompletableFuture<Map<String, Rank>> getUserNameRankMap(Party party);

    /**
     * @param party {@link Party} to reset ranks
     * @return A {@link Party} with the ranks reset to default TODO: as defined in the config
     */
    CompletableFuture<Party> resetRanks(Party party);

    /**
     * @param id {@link ObjectId} of a {@link Party} to reset ranks
     * @return A {@link Party} with the ranks reset to default TODO: as defined in the config
     */
    CompletableFuture<UpdateResult> resetRanks(ObjectId id);

    /**
     * @param name {@link String} of a {@link Party} to reset ranks.
     *
     * {@link String#equalsIgnoreCase(String)} must be {@code true} for {@link Party#name} and the provided {@code name}
     *
     * @return A {@link Party} with the ranks reset to default TODO: as defined in the config
     */
    CompletableFuture<UpdateResult> resetRanks(String name);

    /**
     * @param party {@link Party} to reset permissions
     * @return A {@link Party} with the permissions reset to default TODO: as defined in the config
     */
    CompletableFuture<Party> resetPermissions(Party party);

    /**
     * @param id {@link ObjectId} of a {@link Party} to reset permissions
     * @return A {@link Party} with the permissions reset to default TODO: as defined in the config
     */
    CompletableFuture<UpdateResult> resetPermissions(ObjectId id);

    /**
     * @param name {@link String} of a {@link Party} to reset permissions.
     *
     * {@link String#equalsIgnoreCase(String)} must be {@code true} for {@link Party#name} and the provided {@code name}
     *
     * @return A {@link Party} with the permissions reset to default TODO: as defined in the config
     */
    CompletableFuture<UpdateResult> resetPermissions(String name);

}
