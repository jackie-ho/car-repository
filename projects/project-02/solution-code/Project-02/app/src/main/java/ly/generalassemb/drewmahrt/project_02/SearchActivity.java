package ly.generalassemb.drewmahrt.project_02;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    public static final String EXTRA_DB_ID = "EXTRA_ID";

    private NeighborhoodDatabaseTable mDatabase;
    private NeighborhoodCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mDatabase = new NeighborhoodDatabaseTable(this);
        mDatabase.getEntryById("", null);
        handleIntent(getIntent());

        ListView searchResultsListView = (ListView)findViewById(R.id.neighborhood_search_results_list_view);
        searchResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor selectedCursor = (Cursor) parent.getAdapter().getItem(position);
                Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                String selectedId = selectedCursor.getString(selectedCursor.getColumnIndexOrThrow(NeighborhoodDatabaseTable.COL_ID));
                detailIntent.putExtra(EXTRA_DB_ID, selectedId);
                startActivity(detailIntent);
            }
        });

        Cursor favoritesCursor = mDatabase.getFavorites();
        mCursorAdapter = new NeighborhoodCursorAdapter(this,favoritesCursor,0);
        searchResultsListView.setAdapter(mCursorAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Cursor cursor = mDatabase.getSearchMatches(query);
            ListView searchResultsListView = (ListView)findViewById(R.id.neighborhood_search_results_list_view);
            mCursorAdapter = new NeighborhoodCursorAdapter(this,cursor,0);
            searchResultsListView.setAdapter(mCursorAdapter);
        }
    }


}
