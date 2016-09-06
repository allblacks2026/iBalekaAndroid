package RetroFitModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 9/2/2016.
 */
public class ResponseBody {

    @SerializedName("didError")
    private boolean didError;
    @SerializedName("errorMessage")
    private String errorMessage;

    public ResponseBody(boolean didError, String errorMessage) {
        this.didError = didError;
        this.errorMessage = errorMessage;
    }

    public boolean isDidError() {
        return didError;
    }

    public void setDidError(boolean didError) {
        this.didError = didError;
    }
}
