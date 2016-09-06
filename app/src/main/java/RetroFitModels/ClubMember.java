package RetroFitModels;

import org.json.JSONObject;

/**
 * Created by Okuhle on 8/29/2016.
 */
public class ClubMember {

    private int memberId;
    private int athleteId;
    private int clubId;
    private String dateJoined;
    private String dateLeft;
    private String status;

    public ClubMember(JSONObject object) {
        try {
            memberId = object.getInt("memberId");
            athleteId = object.getInt("athleteId");
            clubId = object.getInt("clubId");
            dateJoined = object.getString("dateJoined");
            dateLeft = object.getString("dateLeft");
            status = object.getString("status");
        } catch (Exception error) {

        }
    }

    public ClubMember(int memberId, int athleteId, int clubId, String dateJoined, String dateLeft, String status) {
        this.memberId = memberId;
        this.athleteId = athleteId;
        this.clubId = clubId;
        this.dateJoined = dateJoined;
        this.dateLeft = dateLeft;
        this.status = status;
    }

    public ClubMember(int athleteId, int clubId, String dateJoined, String dateLeft, String status) {
        this.athleteId = athleteId;
        this.clubId = clubId;
        this.dateJoined = dateJoined;
        this.dateLeft = dateLeft;
        this.status = status;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getDateLeft() {
        return dateLeft;
    }

    public void setDateLeft(String dateLeft) {
        this.dateLeft = dateLeft;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
