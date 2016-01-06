package generalassembly.yuliyakaleda.solutioncode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
  private EditText input;
  private TextView text;
  private Button addButton;
  private LinearLayout wishList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    input = (EditText) findViewById(R.id.input);
    text  = (TextView) findViewById(R.id.text);

    addButton = (Button) findViewById(R.id.add_button);
    addButton.setOnClickListener(this);

    wishList = (LinearLayout) findViewById(R.id.wish_list);
    // This is an alternative way to enable layout animation if you didn't add an attribute
    // android:animateLayoutChanges="true" in the activity_main.xml file
//     LayoutTransition l = new LayoutTransition();
//     l.enableTransitionType(LayoutTransition.CHANGING);
//     wishList.setLayoutTransition(l);

  }

  @Override
  public void onClick(View v) {
    //Animation of the text
    String wish = input.getText().toString();
    text.setText(wish);
    Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
    text.startAnimation(animation);

    //Adding wish to the list
    TextView newView = new TextView(this);
    newView.setText(wish);
    wishList.addView(newView, 0);

    //Clearing the input field
    input.setText("");
  }
}
