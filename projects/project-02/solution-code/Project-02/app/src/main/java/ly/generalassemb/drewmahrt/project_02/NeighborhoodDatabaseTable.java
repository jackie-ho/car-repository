package ly.generalassemb.drewmahrt.project_02;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Drew on 11/25/15.
 */
public class NeighborhoodDatabaseTable {
    private static final String TAG = "NeighborhoodDatabase";

    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_NEIGHBORHOOD_TYPE = "NEIGHBORHOOD_TYPE";
    public static final String COL_STREET = "STREET";
    public static final String COL_FAVORITE = "FAVORITE";

    private static final String DATABASE_NAME = "NEIGHBORHOOD";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 8;

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    public NeighborhoodDatabaseTable(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    public Cursor getEntryById(String query, String[] columns) {
        String selection = COL_ID + " MATCH ?";
        String[] selectionArgs = new String[] {query};
        return query(selection, selectionArgs, columns);
    }

    public Cursor getSearchMatches(String query) {
        return mDatabaseOpenHelper.getReadableDatabase().rawQuery("SELECT * FROM FTS WHERE FTS MATCH ('TITLE:"+query+" OR DESCRIPTION:"+query+" OR STREET:"+query+" OR NEIGHBORHOOD_TYPE:"+query+"')",null);
//        String selection = COL_TITLE + " MATCH ?";
//        String[] selectionArgs = new String[] {query+"*"};
//        return query(selection, selectionArgs, columns);
    }

    public Cursor getFavorites() {
        SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM FTS WHERE FAVORITE='1'",null);
    }

    public void toggleFavorite(String id, String isFavorite){
        ContentValues newValues = new ContentValues();
        newValues.put(COL_FAVORITE, isFavorite);
        mDatabaseOpenHelper.setValuesForId(id, newValues);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_ID + ", " +
                        COL_TITLE + ", " +
                        COL_DESCRIPTION + ", " +
                        COL_STREET + ", " +
                        COL_NEIGHBORHOOD_TYPE + ", " +
                        COL_FAVORITE + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            loadInfo();
        }

        //TODO: REMOVE THIS BEFORE POSTING FINAL VERSION
        @Override
        public void onOpen(SQLiteDatabase db) {
            mDatabase = db;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }

        private void loadInfo() {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadNeighborhoodInfo();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        private void loadNeighborhoodInfo() throws IOException {
            final Resources resources = mHelperContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.neighborhood_info);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] strings = TextUtils.split(line, "~");
                    if (strings.length < 5) continue;
                    long id = addEntry(strings[0].trim(), strings[1].trim(), strings[2].trim(), strings[3].trim(), strings[4].trim());
                    if (id < 0) {
                        Log.e(TAG, "unable to add entry");
                    }
                }
            } finally {
                reader.close();
            }
        }

        public long addEntry(String id, String title, String description, String street, String type) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_ID, id);
            initialValues.put(COL_TITLE, title);
            initialValues.put(COL_DESCRIPTION,description);
            initialValues.put(COL_STREET, street);
            initialValues.put(COL_NEIGHBORHOOD_TYPE,type);
            initialValues.put(COL_FAVORITE,false);
            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }

        public void setValuesForId(String id, ContentValues newVals){
            mDatabase.update(FTS_VIRTUAL_TABLE, newVals, COL_ID + "='" + id + "'", null);
        }
    }
}
