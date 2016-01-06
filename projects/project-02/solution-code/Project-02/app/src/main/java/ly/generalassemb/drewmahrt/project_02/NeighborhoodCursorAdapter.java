package ly.generalassemb.drewmahrt.project_02;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Drew on 11/25/15.
 */
public class NeighborhoodCursorAdapter extends CursorAdapter{
    public NeighborhoodCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.neighborhood_search_result_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleTextView = (TextView) view.findViewById(R.id.search_result_title);
        TextView streetTextView = (TextView) view.findViewById(R.id.search_result_street);
        TextView typeTextView = (TextView) view.findViewById(R.id.search_result_type);

        String title = cursor.getString(cursor.getColumnIndexOrThrow(NeighborhoodDatabaseTable.COL_TITLE));
        String street = cursor.getString(cursor.getColumnIndexOrThrow(NeighborhoodDatabaseTable.COL_STREET));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(NeighborhoodDatabaseTable.COL_NEIGHBORHOOD_TYPE));

        titleTextView.setText(title);
        streetTextView.setText(street);
        typeTextView.setText(type);
    }
}
