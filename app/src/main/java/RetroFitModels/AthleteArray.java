package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 9/4/2016.
 */
public class AthleteArray {

    @SerializedName("didError")
    @Expose
    private boolean didError;

    @SerializedName("model")
    @Expose
    private Athlete model;

    public AthleteArray(boolean didError, Athlete model) {
        this.didError = didError;
        this.model = model;
    }

    public boolean isDidError() {
        return didError;
    }

    public void setDidError(boolean didError) {
        this.didError = didError;
    }

    public Athlete getModel() {
        return model;
    }

    public void setModel(Athlete model) {
        this.model = model;
    }
}
