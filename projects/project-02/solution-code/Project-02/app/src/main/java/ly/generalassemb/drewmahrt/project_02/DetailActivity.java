package ly.generalassemb.drewmahrt.project_02;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class DetailActivity extends AppCompatActivity {
    private NeighborhoodDatabaseTable mDatabase;
    private String mIsFavorited = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mDatabase = new NeighborhoodDatabaseTable(this);
        final String id = getIntent().getStringExtra(SearchActivity.EXTRA_DB_ID);
        Cursor selectedCursor = mDatabase.getEntryById(id, null);

        if(selectedCursor != null) {
            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            collapsingToolbar.setTitle(selectedCursor.getString(selectedCursor.getColumnIndexOrThrow(NeighborhoodDatabaseTable.COL_TITLE)));
            TextView streetTextView = (TextView)findViewById(R.id.location_street_text_view);
            TextView typeTextView = (TextView)findViewById(R.id.location_neighborhood_text_view);
            TextView descriptionTextView = (TextView)findViewById(R.id.location_description_text_view);
            streetTextView.setText("Location: "+selectedCursor.getString(selectedCursor.getColumnIndexOrThrow(NeighborhoodDatabaseTable.COL_STREET)));
            typeTextView.setText("Neighborhood: "+selectedCursor.getString(selectedCursor.getColumnIndexOrThrow(NeighborhoodDatabaseTable.COL_NEIGHBORHOOD_TYPE)));
            descriptionTextView.setText(selectedCursor.getString(selectedCursor.getColumnIndexOrThrow(NeighborhoodDatabaseTable.COL_DESCRIPTION)));
            mIsFavorited = selectedCursor.getString(selectedCursor.getColumnIndexOrThrow(NeighborhoodDatabaseTable.COL_FAVORITE));
            if(mIsFavorited.equals("0")) {
                fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            } else {
                fab.setImageResource(R.drawable.ic_favorite_white_24dp);
            }
        } else {
            Log.d("PROJ2_DETAIL", "Problem searching for location in database");
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsFavorited.equals("0")) {
                    mIsFavorited = "1";
                    fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                } else {
                    mIsFavorited = "0";
                    fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }
                mDatabase.toggleFavorite(id,mIsFavorited);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
