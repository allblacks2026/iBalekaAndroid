package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 9/4/2016.
 */
public class TotalCaloriesResponseBody {

    @SerializedName("didError")
    @Expose
    private boolean didError;

    @SerializedName("model")
    @Expose
    private double model;

    public TotalCaloriesResponseBody(boolean didError, double model) {
        this.didError = didError;
        this.model = model;
    }

    public boolean isDidError() {
        return didError;
    }

    public void setDidError(boolean didError) {
        this.didError = didError;
    }

    public double getModel() {
        return model;
    }

    public void setModel(double model) {
        this.model = model;
    }
}
