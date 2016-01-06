package ly.generalassemb.drewmahrt.project_03.model;

import android.content.ContentValues;

import ly.generalassemb.drewmahrt.project_03.ItinerarySQLiteOpenHelper;

/**
 * Created by Drew on 12/2/15.
 */
public class ItineraryEvent {
    private int mEventId;
    private int mItineraryId;
    private String mTitle;
    private String mLocation;
    private String mDate;
    private String mDescription;

    public ItineraryEvent(){

    }

    public ItineraryEvent(int eventId,ContentValues values){
        mEventId = eventId;
        mItineraryId = values.getAsInteger(ItinerarySQLiteOpenHelper.COL_LIST_ID);
        mTitle = values.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_TITLE);
        mLocation = values.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_LOCATION);
        mDate = values.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_DATE);
        mDescription = values.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_DESCRIPTION);
    }

    public int getEventId() {
        return mEventId;
    }

    public void setEventId(int mEventId) {
        this.mEventId = mEventId;
    }

    public int getItineraryId() {
        return mItineraryId;
    }

    public void setItineraryId(int mItineraryId) {
        this.mItineraryId = mItineraryId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}
