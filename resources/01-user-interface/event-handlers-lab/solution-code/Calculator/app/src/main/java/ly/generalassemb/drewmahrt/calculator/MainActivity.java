package ly.generalassemb.drewmahrt.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView mCalculationTextView;
    private Button mExecuteButton;
    private Button mPeriodButton;
    private Button mAddButton;
    private Button mSubtractButton;
    private Button mMultButton;
    private Button mDivideButton;
    private double mCurrentResult;
    private String mCurrentInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentResult = 0;
        mCurrentInput = "";

        mCalculationTextView = (TextView)findViewById(R.id.calculation_text_view);

        Button oneButton = (Button)findViewById(R.id.one_button);
        Button twoButton = (Button)findViewById(R.id.two_button);
        Button threeButton = (Button)findViewById(R.id.three_button);
        Button fourButton = (Button)findViewById(R.id.four_button);
        Button fiveButton = (Button)findViewById(R.id.five_button);
        Button sixButton = (Button)findViewById(R.id.six_button);
        Button sevenButton = (Button)findViewById(R.id.seven_button);
        Button eightButton = (Button)findViewById(R.id.eight_button);
        Button nineButton = (Button)findViewById(R.id.nine_button);
        Button zeroButton = (Button)findViewById(R.id.zero_button);

        mAddButton = (Button)findViewById(R.id.add_button);
        mSubtractButton = (Button)findViewById(R.id.subtract_button);
        mMultButton = (Button)findViewById(R.id.mult_button);
        mDivideButton = (Button)findViewById(R.id.divide_button);

        mPeriodButton = (Button)findViewById(R.id.period_button);
        Button clearButton = (Button)findViewById(R.id.clear_button);
        Button deleteButton = (Button)findViewById(R.id.del_button);
        mExecuteButton = (Button)findViewById(R.id.execute_button);

        //Set click listeners for number buttons
        oneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('1');
            }
        });

        twoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('2');
            }
        });

        threeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('3');
            }
        });

        fourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('4');
            }
        });

        fiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('5');
            }
        });

        sixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('6');
            }
        });

        sevenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('7');
            }
        });

        eightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('8');
            }
        });

        nineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('9');
            }
        });

        zeroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('0');
            }
        });

        //set click listeners for operators
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('+');
            }
        });

        mSubtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('-');
            }
        });

        mMultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('*');
            }
        });

        mDivideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('/');
            }
        });


        //set click listeners for other buttons
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('d');
            }
        });

        mPeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('.');
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalculationTextView.setText("");
                mCurrentInput = "";
                mCurrentResult = 0;
                mAddButton.setEnabled(true);
                mSubtractButton.setEnabled(true);
                mDivideButton.setEnabled(true);
                mMultButton.setEnabled(true);
                mPeriodButton.setEnabled(true);
                mExecuteButton.setEnabled(false);
            }
        });

        mExecuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCalculation();
            }
        });
    }

    private void performCalculation(){
        String[] splitArray = mCalculationTextView.getText().toString().split(" ");

        //TODO: Add check for divide by 0 in the if statement
        //The string is formatted like "2.0 / 0", so the array above would look like [2.0, / , 0]
        if(splitArray[2].equals("0") && splitArray[1].equals("/")){
            Toast.makeText(MainActivity.this,"Error: Cannot divide by 0",Toast.LENGTH_SHORT).show();
        }else{
            switch (splitArray[1]){
                case "+":
                    mCurrentResult = mCurrentResult + Double.parseDouble(splitArray[2]);
                    break;
                case "-":
                    mCurrentResult = mCurrentResult - Double.parseDouble(splitArray[2]);
                    break;
                case "*":
                    mCurrentResult = mCurrentResult * Double.parseDouble(splitArray[2]);
                    break;
                case "/":
                    mCurrentResult = mCurrentResult / Double.parseDouble(splitArray[2]);
                    break;
                default:
                    Toast.makeText(MainActivity.this,"Error: No operand found",Toast.LENGTH_SHORT).show();
                    return;
            }
            mCalculationTextView.setText(mCurrentResult + "");
            mCurrentInput = "";
            mAddButton.setEnabled(true);
            mSubtractButton.setEnabled(true);
            mDivideButton.setEnabled(true);
            mMultButton.setEnabled(true);
            mPeriodButton.setEnabled(true);
            mExecuteButton.setEnabled(false);
        }
    }

    private void updateTextView(char c){
        String currentStr = mCalculationTextView.getText().toString();
        switch (c){
            case '+':
            case '-':
            case '/':
            case '*':
                mCurrentInput = " "+c+" ";
                mCalculationTextView.setText(mCurrentResult+mCurrentInput);
                mAddButton.setEnabled(false);
                mSubtractButton.setEnabled(false);
                mDivideButton.setEnabled(false);
                mMultButton.setEnabled(false);
                mPeriodButton.setEnabled(true);
                break;
            case 'd':
                if(mCurrentInput.length() > 0){
                    mCurrentInput = mCurrentInput.substring(0,mCurrentInput.length()-1);
                    mCalculationTextView.setText(currentStr.substring(0,currentStr.length()-1));

                    Toast.makeText(MainActivity.this,"currentInput: "+mCurrentInput,Toast.LENGTH_SHORT).show();

                    if(mCurrentInput.length() == 0){
                        mAddButton.setEnabled(true);
                        mSubtractButton.setEnabled(true);
                        mDivideButton.setEnabled(true);
                        mMultButton.setEnabled(true);
                    }
                    if(!mCurrentInput.contains(".")){
                        mPeriodButton.setEnabled(true);
                    }
                }
                break;
            case '.':
                mPeriodButton.setEnabled(false);
            default:
                mCurrentInput = mCurrentInput + c;
                if(!currentStr.contains("/") && !currentStr.contains("*") && !currentStr.contains("+") && !currentStr.contains("-"))
                    mCurrentResult = Double.parseDouble(mCurrentInput);
                mCalculationTextView.setText(currentStr+c);
                break;
        }

        String[] splitArray = mCalculationTextView.getText().toString().split(" ");
        if(splitArray.length == 3)
            mExecuteButton.setEnabled(true);
    }
}
