package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 8/29/2016.
 */
public class Checkpoint {

    @SerializedName("checkpointId")
    @Expose
    private int checkpointID;

    @SerializedName("deleted")
    @Expose
    private boolean deleted;

    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;

    @SerializedName("routeId")
    @Expose
    private int routeID;

    public Checkpoint(int checkpointID, boolean deleted, double latitude, double longitude, int routeID) {
        this.checkpointID = checkpointID;
        this.deleted = deleted;
        this.latitude = latitude;
        this.longitude = longitude;
        this.routeID = routeID;
    }

    public int getCheckpointID() {
        return checkpointID;
    }

    public void setCheckpointID(int checkpointID) {
        this.checkpointID = checkpointID;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }
}
