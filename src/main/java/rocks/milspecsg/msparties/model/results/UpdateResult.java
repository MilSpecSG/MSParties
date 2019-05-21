package rocks.milspecsg.msparties.model.results;

import org.mongodb.morphia.query.UpdateResults;

import java.util.function.Function;

public class UpdateResult extends Result {

    private UpdateResults updateResults;

    public UpdateResults getUpdateResults() {
        return updateResults;
    }

    public UpdateResult(UpdateResults updateResults, boolean success, String successMessage, String errorMessage) {
        this.updateResults = updateResults;
        this.success = success;
        this.successMessage = successMessage;
        this.errorMessage = errorMessage;
    }

    public UpdateResult(UpdateResults updateResults, Function<UpdateResults, Boolean> successCondition, String successMessage, String errorMessage) {
        this(updateResults, successCondition.apply(updateResults), successMessage, errorMessage);
    }

    public UpdateResult(UpdateResults updateResults, Function<UpdateResults, Boolean> successCondition) {
        this.updateResults = updateResults;
        this.success = successCondition.apply(updateResults);
    }

    public UpdateResult(UpdateResults updateResults, String successMessage, String errorMessage) {
        this(updateResults, updateResults.getUpdatedCount() > 0, successMessage, errorMessage);
    }

    public UpdateResult(UpdateResults updateResults, boolean success) {
        this.updateResults = updateResults;
        this.success = success;
    }

    public UpdateResult(UpdateResults updateResults) {
        this(updateResults, u -> u.getUpdatedCount() > 0);
    }

    public static UpdateResult success() {
        return new UpdateResult(null, true);
    }

    public static UpdateResult fail() {
        return new UpdateResult(null, false);
    }

}
