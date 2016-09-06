package RetroFitModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Okuhle on 8/28/2016.
 */
public class EventRoute implements Parcelable{

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("eventRouteID")
    @Expose
    private int eventRouteId;

    @SerializedName("dateAdded")
    @Expose
    private String dateAdded;

    @SerializedName("deleted")
    @Expose
    private boolean deleted;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("eventID")
    @Expose
    private int eventId;

    @SerializedName("routeID")
    private int routeId;

    public EventRoute(String title, int eventRouteId, String dateAdded, boolean deleted, String description, int eventId, int routeId) {
        this.title = title;
        this.eventRouteId = eventRouteId;
        this.dateAdded = dateAdded;
        this.deleted = deleted;
        this.description = description;
        this.eventId = eventId;
        this.routeId = routeId;
    }

    protected EventRoute(Parcel in) {
        title = in.readString();
        eventRouteId = in.readInt();
        dateAdded = in.readString();
        deleted = in.readByte() != 0;
        description = in.readString();
        eventId = in.readInt();
        routeId = in.readInt();
    }

    public static final Creator<EventRoute> CREATOR = new Creator<EventRoute>() {
        @Override
        public EventRoute createFromParcel(Parcel in) {
            return new EventRoute(in);
        }

        @Override
        public EventRoute[] newArray(int size) {
            return new EventRoute[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEventRouteId() {
        return eventRouteId;
    }

    public void setEventRouteId(int eventRouteId) {
        this.eventRouteId = eventRouteId;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(eventRouteId);
        dest.writeString(dateAdded);
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeString(description);
        dest.writeInt(eventId);
        dest.writeInt(routeId);
    }
}
