package ly.generalassemb.drewmahrt.project_03.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import ly.generalassemb.drewmahrt.project_03.ItinerarySQLiteOpenHelper;

/**
 * Created by Drew on 11/23/15.
 */

public class Itinerary {
    private int mId;
    private String mTitle;
    private String mStartingDate;
    private String mStartingLocation;

    public Itinerary(){

    }

    public Itinerary(int id, ContentValues values){
        mId = id;
        mTitle = values.getAsString(ItinerarySQLiteOpenHelper.COL_LIST_TITLE);
        mStartingDate = values.getAsString(ItinerarySQLiteOpenHelper.COL_LIST_STARTING_DATE);
        mStartingLocation = values.getAsString(ItinerarySQLiteOpenHelper.COL_LIST_STARTING_LOC);
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getStartingDate() {
        return mStartingDate;
    }

    public void setStartingDate(String mStartingDate) {
        this.mStartingDate = mStartingDate;
    }

    public String getStartingLocation() {
        return mStartingLocation;
    }

    public void setStartingLocation(String mStartingLocation) {
        this.mStartingLocation = mStartingLocation;
    }
}
