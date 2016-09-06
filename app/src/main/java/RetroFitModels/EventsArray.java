package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Okuhle on 9/2/2016.
 */
public class EventsArray {

    @SerializedName("didError")
    @Expose
    private boolean didError;

    @SerializedName("model")
    @Expose
    private List<Event> model;

    public EventsArray(boolean didError, List<Event> model) {
        this.didError = didError;
        this.model = model;
    }

    public boolean isDidError() {
        return didError;
    }

    public void setDidError(boolean didError) {
        this.didError = didError;
    }

    public List<Event> getModel() {
        return model;
    }

    public void setModel(List<Event> model) {
        this.model = model;
    }
}
