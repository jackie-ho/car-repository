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
    private List<String> mSongTitleList;
    private List<String> mArtistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Instantiate your lists
        mSongTitleList = new ArrayList<>();
        mArtistList = new ArrayList<>();

        Button submitButton = (Button)findViewById(R.id.submit_button);
        mTitleText = (EditText)findViewById(R.id.title_text);
        mArtistText = (EditText)findViewById(R.id.artist_text);

        //TODO: Scroll down to the setupListView method for more instructions
        setupListView();

        //TODO: Setup click listener for button. Complete button logic to check for duplicates or empty input, and add new items to the lists
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mArtistList.contains(mArtistText.getText().toString()) && mSongTitleList.contains(mTitleText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "The song and artist you are trying to add already exists", Toast.LENGTH_LONG).show();
                } else if(mArtistText.getText().toString().length() == 0 || mTitleText.getText().toString().length() == 0){
                    Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                } else {
                    mSongTitleList.add(mTitleText.getText().toString());
                    mArtistList.add(mArtistText.getText().toString());
                    mArrayAdapter.notifyDataSetChanged();
                }
            }
        });

        mPlaylistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Complete the logic to remove the song and artist from their respective lists at the 'position' variable passed into this method
                mArtistList.remove(position);
                mSongTitleList.remove(position);

                //Leave this line alone
                mArrayAdapter.notifyDataSetChanged();
            }
        });


    }

    private void setupListView(){
        mPlaylistListView = (ListView)findViewById(R.id.playlist);
        //TODO: Replace YOUR_SONG_LIST with the variable name for your song List
        mArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2,android.R.id.text1,mSongTitleList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                //TODO: In the setText calls below, you need to pass the song title for text1, and the artist for text2
                text1.setText(mSongTitleList.get(position));
                text2.setText(mArtistList.get(position));

                return view;
            }
        };
        mPlaylistListView.setAdapter(mArrayAdapter);
    }
}
