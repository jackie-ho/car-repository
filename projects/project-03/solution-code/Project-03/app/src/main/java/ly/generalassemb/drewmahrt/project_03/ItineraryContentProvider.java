package ly.generalassemb.drewmahrt.project_03;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import ly.generalassemb.drewmahrt.project_03.model.Itinerary;
import ly.generalassemb.drewmahrt.project_03.model.ItineraryEvent;

/**
 * Created by Drew on 12/2/15.
 */
public class ItineraryContentProvider extends ContentProvider {
    private ItinerarySQLiteOpenHelper dbHelper;
    private static final String AUTHORITY = "ly.generalassemb.drewmahrt.project_03.provider";

    private static final int ALL_ITINERARIES = 1;
    private static final int ALL_EVENTS = 2;
    private static final int SINGLE_EVENT = 3;
    private static final int SINGLE_ITINERARY = 4;

    public static final String ITINERARIES_CONTENT_URI = "content://"+AUTHORITY+"/itineraries";
    public static final String EVENTS_CONTENT_URI = "content://"+AUTHORITY+"/events";

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "itineraries", ALL_ITINERARIES);
        uriMatcher.addURI(AUTHORITY, "events/#", ALL_EVENTS);
        uriMatcher.addURI(AUTHORITY, "events/#/#", SINGLE_EVENT);
        uriMatcher.addURI(AUTHORITY, "itineraries/#", SINGLE_ITINERARY);
    }

    @Override
    public boolean onCreate() {
        dbHelper = ItinerarySQLiteOpenHelper.getInstance(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case ALL_ITINERARIES:
                return "vnd.android.cursor.dir/vnd."+AUTHORITY+".itineraries";
            case ALL_EVENTS:
                return "vnd.android.cursor.dir/vnd."+AUTHORITY+".events";
            case SINGLE_EVENT:
                return "vnd.android.cursor.item/vnd."+AUTHORITY+".event";
            case SINGLE_ITINERARY:
                return "vnd.android.cursor.item/vnd."+AUTHORITY+".itinerary";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int id;
        switch (uriMatcher.match(uri)) {
            case ALL_ITINERARIES:
                queryBuilder.setTables(ItinerarySQLiteOpenHelper.ITIN_LIST_TABLE_NAME);
                break;
            case SINGLE_ITINERARY:
                queryBuilder.setTables(ItinerarySQLiteOpenHelper.ITIN_LIST_TABLE_NAME);
                id = Integer.parseInt(uri.getPathSegments().get(1));
                queryBuilder.appendWhere(ItinerarySQLiteOpenHelper.COL_ID+"="+id);
                break;
            case ALL_EVENTS:
                queryBuilder.setTables(ItinerarySQLiteOpenHelper.ITIN_EVENTS_TABLE_NAME);
                id = Integer.parseInt(uri.getPathSegments().get(1));
                queryBuilder.appendWhere(ItinerarySQLiteOpenHelper.COL_LIST_ID + "=" + id);
                break;
            case SINGLE_EVENT:
                queryBuilder.setTables(ItinerarySQLiteOpenHelper.ITIN_EVENTS_TABLE_NAME);
                id = Integer.parseInt(uri.getPathSegments().get(2));
                queryBuilder.appendWhere(ItinerarySQLiteOpenHelper.COL_ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        String selectedURI = "";
        switch (uriMatcher.match(uri)) {
            case ALL_ITINERARIES:
                selectedURI = ITINERARIES_CONTENT_URI;
                id = dbHelper.addList(values.getAsString(ItinerarySQLiteOpenHelper.COL_LIST_TITLE),
                        values.getAsString(ItinerarySQLiteOpenHelper.COL_LIST_STARTING_DATE),
                        values.getAsString(ItinerarySQLiteOpenHelper.COL_LIST_STARTING_LOC));
                break;
            case ALL_EVENTS:
                selectedURI = EVENTS_CONTENT_URI;
                id = dbHelper.addEvent(values.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_TITLE),
                        values.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_DESCRIPTION),
                        values.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_DATE),
                        values.getAsString(ItinerarySQLiteOpenHelper.COL_EVENT_LOCATION),
                        values.getAsInteger(ItinerarySQLiteOpenHelper.COL_LIST_ID));
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return Uri.parse(selectedURI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleteCount = 0;
        String id;
        switch (uriMatcher.match(uri)) {
            case ALL_ITINERARIES:
                break;
            case ALL_EVENTS:
                id = uri.getPathSegments().get(1);
                deleteCount = dbHelper.deleteEventsByListId(Integer.parseInt(id));
                break;
            case SINGLE_EVENT:
                id = uri.getPathSegments().get(2);
                deleteCount = dbHelper.deleteEvent(Integer.parseInt(id));
                break;
            case SINGLE_ITINERARY:
                id = uri.getPathSegments().get(1);
                deleteCount = dbHelper.deleteItinerary(Integer.parseInt(id));
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    public int update(
            Uri uri,
            ContentValues values,
            String selection,
            String[] selectionArgs) {
        String id;
        int updateCount = 0;
        switch (uriMatcher.match(uri)) {
            case SINGLE_EVENT:
                id = uri.getPathSegments().get(2);
                updateCount = dbHelper.updateEvent(new ItineraryEvent(Integer.parseInt(id),values));
                break;
            case SINGLE_ITINERARY:
                id = uri.getPathSegments().get(1);
                updateCount = dbHelper.updateItinerary(new Itinerary(Integer.parseInt(id),values));
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
