package ly.generalassemb.drewmahrt.project_03;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.List;

import ly.generalassemb.drewmahrt.project_03.model.Itinerary;
import ly.generalassemb.drewmahrt.project_03.model.ItineraryEvent;

/**
 * Created by Drew on 12/2/15.
 */
public class ItinerarySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "ITINERARY_DB";
    public static final String ITIN_LIST_TABLE_NAME = "ITINERARY_LIST";
    public static final String ITIN_EVENTS_TABLE_NAME = "ITINERARY_EVENTS";

    public static final String COL_ID = "_id";
    public static final String COL_LIST_ID = "LIST_ID";
    public static final String COL_LIST_TITLE = "LIST_TITLE";
    public static final String COL_LIST_STARTING_LOC = "STARTING_LOCATION";
    public static final String COL_LIST_STARTING_DATE = "STARTING_DATE";
    public static final String COL_LAST_MODIFIED = "LAST_MODIFIED";

    public static final String COL_EVENT_ID = "EVENT_ID";
    public static final String COL_EVENT_TITLE = "EVENT_TITLE";
    public static final String COL_EVENT_DESCRIPTION = "EVENT_DESCRIPTION";
    public static final String COL_EVENT_DATE = "EVENT_DATE";
    public static final String COL_EVENT_LOCATION = "EVENT_LOCATION";

    public static final String[] ITINERARY_COLUMNS = {COL_ID,COL_LIST_TITLE,COL_LIST_STARTING_DATE,COL_LIST_STARTING_LOC};
    public static final String[] EVENT_COLUMNS = {COL_ID,COL_EVENT_TITLE,COL_EVENT_DESCRIPTION,COL_EVENT_DATE,COL_EVENT_LOCATION,COL_LIST_ID};

    private static final String CREATE_ITINERARY_LIST_TABLE =
            "CREATE TABLE " + ITIN_LIST_TABLE_NAME +
                    "(" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_LIST_TITLE + " TEXT, " +
                    COL_LIST_STARTING_DATE + " TEXT, " +
                    COL_LIST_STARTING_LOC + " TEXT, " +
                    COL_LAST_MODIFIED + " TEXT )";

    private static final String CREATE_ITINERARY_EVENTS_TABLE =
            "CREATE TABLE " + ITIN_EVENTS_TABLE_NAME +
                    "(" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_EVENT_TITLE + " TEXT, " +
                    COL_EVENT_DESCRIPTION + " TEXT, " +
                    COL_EVENT_DATE + " TEXT, " +
                    COL_EVENT_LOCATION + " TEXT, " +
                    COL_LAST_MODIFIED + " TEXT, " +
                    COL_LIST_ID + " INTEGER, " +
                    "FOREIGN KEY("+COL_LIST_ID+") REFERENCES "+ ITIN_LIST_TABLE_NAME +"("+ COL_ID +") )";

    private static ItinerarySQLiteOpenHelper instance;

    public static ItinerarySQLiteOpenHelper getInstance(Context context){
        if(instance == null){
            instance = new ItinerarySQLiteOpenHelper(context);
        }
        return instance;
    }

    private ItinerarySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITINERARY_LIST_TABLE);
        db.execSQL(CREATE_ITINERARY_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ITIN_EVENTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITIN_LIST_TABLE_NAME);
        this.onCreate(db);
    }

    //Add new itinerary list
    public long addList(String title, String startingDate, String startingLocation){
        ContentValues values = new ContentValues();
        values.put(COL_LIST_TITLE, title);
        values.put(COL_LIST_STARTING_DATE, startingDate);
        values.put(COL_LIST_STARTING_LOC, startingLocation);
        values.put(COL_LAST_MODIFIED, (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString()));

        SQLiteDatabase db = this.getWritableDatabase();
        long returnId = db.insert(ITIN_LIST_TABLE_NAME, null, values);
        db.close();
        return returnId;
    }

    //Add new event item
    public long addEvent(String title, String description, String date, String location, int listId){
        ContentValues values = new ContentValues();
        values.put(COL_EVENT_TITLE,title);
        values.put(COL_EVENT_DESCRIPTION,description);
        values.put(COL_EVENT_DATE,date);
        values.put(COL_EVENT_LOCATION,location);
        values.put(COL_LIST_ID, listId);
        values.put(COL_LAST_MODIFIED, (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString()));

        SQLiteDatabase db = this.getWritableDatabase();
        long returnId = db.insert(ITIN_EVENTS_TABLE_NAME, null, values);
        db.close();
        return returnId;
    }

    public Itinerary getItineraryById(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(ITIN_LIST_TABLE_NAME, // a. table
                        ITINERARY_COLUMNS, // b. column names
                        COL_ID+" = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if(cursor != null)
            cursor.moveToFirst();

        Itinerary itinerary = new Itinerary();
        itinerary.setId(id);
        itinerary.setTitle(cursor.getString(cursor.getColumnIndex(COL_LIST_TITLE)));
        itinerary.setStartingDate(cursor.getString(cursor.getColumnIndex(COL_LIST_STARTING_DATE)));
        itinerary.setStartingLocation(cursor.getString(cursor.getColumnIndex(COL_LIST_STARTING_LOC)));

        return itinerary;
    }

    public List<ItineraryEvent> getEventsById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(ITIN_EVENTS_TABLE_NAME, // a. table
                        EVENT_COLUMNS, // b. column names
                        COL_LIST_ID + " = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        List<ItineraryEvent> events = new ArrayList<>();
        ItineraryEvent itineraryEvent = null;

        if(cursor.moveToFirst()){
            do{
                itineraryEvent = new ItineraryEvent();
                itineraryEvent.setTitle(cursor.getString(cursor.getColumnIndex(COL_EVENT_TITLE)));
                itineraryEvent.setDate(cursor.getString(cursor.getColumnIndex(COL_EVENT_DATE)));
                itineraryEvent.setDescription(cursor.getString(cursor.getColumnIndex(COL_EVENT_DESCRIPTION)));
                itineraryEvent.setEventId(cursor.getInt(cursor.getColumnIndex(COL_EVENT_ID)));
                itineraryEvent.setItineraryId(id);

                events.add(itineraryEvent);
            }while (cursor.moveToNext());
        }

        return events;
    }

    public int updateItinerary(Itinerary itinerary){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_LIST_TITLE,itinerary.getTitle());
        values.put(COL_LIST_STARTING_LOC, itinerary.getStartingLocation());
        values.put(COL_LIST_STARTING_DATE, itinerary.getStartingDate());
        values.put(COL_LAST_MODIFIED, (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString()));

        int i = db.update(ITIN_LIST_TABLE_NAME,
                values,
                COL_ID+" = ?",
                new String[]{String.valueOf(itinerary.getId())});

        db.close();
        return i;
    }

    public int updateEvent(ItineraryEvent event){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_EVENT_TITLE,event.getTitle());
        values.put(COL_EVENT_LOCATION, event.getLocation());
        values.put(COL_EVENT_DATE,event.getDate());
        values.put(COL_LIST_ID, event.getItineraryId());
        values.put(COL_EVENT_DESCRIPTION,event.getDescription());
        values.put(COL_LAST_MODIFIED, (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString()));

        int i = db.update(ITIN_EVENTS_TABLE_NAME,
                values,
                COL_ID+" = ?",
                new String[]{String.valueOf(event.getEventId())});

        db.close();
        return i;
    }

    public int deleteEvent(int id){
        SQLiteDatabase db = getWritableDatabase();
        int deleteNum = db.delete(ITIN_EVENTS_TABLE_NAME,
                COL_ID+" = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return deleteNum;
    }

    public int deleteEventsByListId(int id){
        SQLiteDatabase db = getWritableDatabase();
        int deleteNum = db.delete(ITIN_EVENTS_TABLE_NAME,
                COL_LIST_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return deleteNum;
    }

    public int deleteItinerary(int id){
        SQLiteDatabase db = getWritableDatabase();
        deleteEventsByListId(id);
        int deleteNum = db.delete(ITIN_LIST_TABLE_NAME,
                COL_ID+" = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return deleteNum;
    }
}
