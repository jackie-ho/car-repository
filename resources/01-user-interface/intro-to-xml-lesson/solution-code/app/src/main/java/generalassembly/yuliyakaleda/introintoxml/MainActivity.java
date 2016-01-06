package generalassembly.yuliyakaleda.introintoxml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Spinner spinner = (Spinner) findViewById(R.id.spinner);

    // Spinner is set to an adapter.
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.holidays_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
  }
}
