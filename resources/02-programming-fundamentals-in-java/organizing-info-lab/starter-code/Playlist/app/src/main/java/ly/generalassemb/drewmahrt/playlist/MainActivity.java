package ly.generalassemb.drewmahrt.playlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mPlaylistListView;
    private ArrayAdapter<String> mArrayAdapter;
    private EditText mTitleText;
    private EditText mArtistText;

    //TODO: Define your two Lists here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Instantiate your lists

        Button submitButton = (Button)findViewById(R.id.submit_button);
        mTitleText = (EditText)findViewById(R.id.title_text);
        mArtistText = (EditText)findViewById(R.id.artist_text);

        //TODO: Scroll down to the setupListView method for more instructions
        setupListView();

        //TODO: Setup click listener for button. Complete button logic to check for duplicates or empty input, and add new items to the lists
        //Add the following line of code (without comments) directly after you finish modifying your lists:
        // mArrayAdapter.notifyDataSetChanged();

        mPlaylistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Complete the logic to remove the song and artist from their respective lists at the 'position' variable passed into this method

                //Leave this line alone
                mArrayAdapter.notifyDataSetChanged();
            }
        });


    }

    private void setupListView(){
        mPlaylistListView = (ListView)findViewById(R.id.playlist);
        //TODO: Replace YOUR_SONG_LIST in the line below with the variable name for your song List
        mArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2,android.R.id.text1,YOUR_SONG_LIST){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                //TODO: In the setText calls below, you need to pass the song title for text1, and the artist for text2
                text1.setText();
                text2.setText();

                return view;
            }
        };
        mPlaylistListView.setAdapter(mArrayAdapter);
    }
}