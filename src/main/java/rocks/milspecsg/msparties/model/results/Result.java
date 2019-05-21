package rocks.milspecsg.msparties.model.results;

public class Result {

    protected boolean success;
    protected String successMessage = "success", errorMessage = "error";

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return success ? successMessage : errorMessage;
    }

}
