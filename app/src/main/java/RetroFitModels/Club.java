package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Okuhle on 8/30/2016.
 */
public class Club implements Serializable{

    @SerializedName("clubId")
    @Expose
    private int clubId;

    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;

    @SerializedName("deleted")
    @Expose
    private boolean deleted;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("userId")
    @Expose
    private int userId;

    @SerializedName("event")
    @Expose
    private List<Event> event;

    @SerializedName("clubMember")
    @Expose
    private List<ClubMember> clubMember;

    @SerializedName("applicationUser")
    @Expose
    private String applicationUser;

    public Club(JSONObject object) {
        try {
            clubId = object.getInt("clubId");
            dateCreated = object.getString("dateCreated");
            deleted = object.getBoolean("deleted");
            description = object.getString("description");
            location = object.getString("location");
            userId = object.getInt("userId");
            name = object.getString("name");
        } catch (Exception error) {

        }
    }

    public Club(String applicationUser, int userId, String location, String name, String description, boolean deleted, String dateCreated, int clubId) {
        this.applicationUser = applicationUser;
        this.userId = userId;
        this.location = location;
        this.description = description;
        this.deleted = deleted;
        this.dateCreated = dateCreated;
        this.clubId = clubId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Event> getEvent() {
        return event;
    }

    public void setEvent(List<Event> event) {
        this.event = event;
    }

    public String getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(String applicationUser) {
        this.applicationUser = applicationUser;
    }

    public List<ClubMember> getClubMember() {
        return clubMember;
    }

    public void setClubMember(List<ClubMember> clubMember) {
        this.clubMember = clubMember;
    }
}
