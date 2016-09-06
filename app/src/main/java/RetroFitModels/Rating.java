package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 8/29/2016.
 */
public class Rating {

    @SerializedName("ratingId")
    @Expose
    private int ratingId;

    @SerializedName("comment")
    private String comment;
    private String dateAdded;
    private String deleted;
    private String routeId;
    private String runId;
    private String value;
    private Route route;
    private Run run;


}
