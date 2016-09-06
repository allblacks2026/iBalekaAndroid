package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 9/2/2016.
 */
public class RouteArray {

    @SerializedName("didError")
    @Expose
    private boolean didError;

    @SerializedName("model")
    @Expose
    private Route model;

    public RouteArray(boolean didError, Route model) {
        this.didError = didError;
        this.model = model;
    }

    public boolean isDidError() {
        return didError;
    }

    public void setDidError(boolean didError) {
        this.didError = didError;
    }

    public Route getModel() {
        return model;
    }

    public void setModel(Route model) {
        this.model = model;
    }
}
