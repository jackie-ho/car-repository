package generalassembly.yuliyakaleda.supportdifferentdevices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private TextView text;
  private Button button;
  private ImageView image;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    text = (TextView) findViewById(R.id.text_view);
    image = (ImageView) findViewById(R.id.image);
    button = (Button) findViewById(R.id.button);
    button.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    text.setText(R.string.answer);
    image.setBackgroundResource(R.drawable.ic_insert_emoticon_40dp);
  }
}
