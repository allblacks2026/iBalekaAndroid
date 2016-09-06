package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Okuhle on 8/29/2016.
 */
public class Route implements Serializable{

    @SerializedName("routeId")
    @Expose
    private int routeID;

    @SerializedName("userID")
    @Expose
    private String userID;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("dateModified")
    @Expose
    private String dateModified;

    @SerializedName("dateRecorded")
    @Expose
    private String dateRecorded;

    @SerializedName("deleted")
    @Expose
    private boolean deleted;

    @SerializedName("distance")
    @Expose
    private double distance;

    @SerializedName("checkpoint")
    @Expose
    private List<Checkpoint> checkpoint = new ArrayList<>();

    public Route(int routeID, String userID, String title, String dateModified, String dateRecorded, boolean deleted, double distance, List<Checkpoint> checkpoint) {
        this.routeID = routeID;
        this.userID = userID;
        this.title = title;
        this.dateModified = dateModified;
        this.dateRecorded = dateRecorded;
        this.deleted = deleted;
        this.distance = distance;
        this.checkpoint = checkpoint;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(String dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<Checkpoint> getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(List<Checkpoint> checkpoint) {
        this.checkpoint = checkpoint;
    }
}
