package rocks.milspecsg.msparties.model.results;

import org.mongodb.morphia.query.UpdateResults;

import java.util.function.Consumer;
import java.util.function.Function;

public class UpdateResult {

    private UpdateResults updateResults;
    private boolean success;
    private String successMessage = "success", failMessage = "fail";

    public UpdateResults getUpdateResults() {
        return updateResults;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return success ? successMessage : failMessage;
    }

    public UpdateResult(UpdateResults updateResults, boolean success, String successMessage, String failMessage) {
        this.updateResults = updateResults;
        this.success = success;
        this.successMessage = successMessage;
        this.failMessage = failMessage;
    }

    public UpdateResult(UpdateResults updateResults, Function<UpdateResults, Boolean> successCondition, String successMessage, String failMessage) {
        this(updateResults, successCondition.apply(updateResults), successMessage, failMessage);
    }

    public UpdateResult(UpdateResults updateResults, Function<UpdateResults, Boolean> successCondition) {
        this.updateResults = updateResults;
        this.success = successCondition.apply(updateResults);
    }

    public UpdateResult(UpdateResults updateResults, String successMessage, String failMessage) {
        this(updateResults, updateResults.getUpdatedCount() > 0, successMessage, failMessage);
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
