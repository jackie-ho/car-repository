package ly.generalassemb.drewmahrt.colorchooser;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView sampleText = (TextView)findViewById(R.id.sample_text);

        Button blackTextButton = (Button)findViewById(R.id.black_text);
        Button greenTextButton = (Button)findViewById(R.id.green_text);
        Button redTextButton = (Button)findViewById(R.id.red_text);
        Button whiteTextButton = (Button)findViewById(R.id.white_text);

        Button blackBackButton = (Button)findViewById(R.id.black_back);
        Button greenBackButton = (Button)findViewById(R.id.green_back);
        Button redBackButton = (Button)findViewById(R.id.red_back);
        Button whiteBackButton = (Button)findViewById(R.id.white_back);

        //Implement the listener for the TextView


        //Implement all of the button listeners

    }
}
