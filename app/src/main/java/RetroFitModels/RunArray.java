package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Okuhle on 9/4/2016.
 */
public class RunArray {
    @SerializedName("didError")
    @Expose
    private boolean didError;
    @SerializedName("model")
    @Expose
    private List<Run> model = new ArrayList<>();

    public RunArray(boolean didError, List<Run> model) {
        this.didError = didError;
        this.model = model;
    }

    public boolean isDidError() {
        return didError;
    }

    public void setDidError(boolean didError) {
        this.didError = didError;
    }

    public List<Run> getModel() {
        return model;
    }

    public void setModel(List<Run> model) {
        this.model = model;
    }
}
