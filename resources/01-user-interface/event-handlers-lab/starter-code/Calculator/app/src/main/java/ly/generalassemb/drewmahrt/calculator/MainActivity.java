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

        //Set the rest of your buttons


        //Set click listeners for number buttons
        oneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView('1');
            }
        });

            //Finish rest of the number buttons

        //set click listeners for operators


        //set click listeners for other buttons

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
    }


    private void performCalculation(){
        String[] splitArray = mCalculationTextView.getText().toString().split(" ");

        //TODO: Add check for divide by 0 in the if statement
        //The string is formatted like "2.0 / 0", so the array above would look like [2.0, / , 0]
        if(){
            Toast.makeText(MainActivity.this,"Error: Cannot divide by 0",Toast.LENGTH_SHORT).show();
        }else{
            switch (splitArray[1]){
                case "+":
                    //TODO: Complete the addition operation
                    //A string can be converted to a double by calling the following: Double.parseDouble(yourDoubleAsAString)
                    break;
                case "-":
                    //TODO: Complete the subtraction operation
                    //A string can be converted to a double by calling the following: Double.parseDouble(yourDoubleAsAString)
                    break;
                case "*":
                    //TODO: Complete the multiplication operation
                    //A string can be converted to a double by calling the following: Double.parseDouble(yourDoubleAsAString)
                    break;
                case "/":
                    //TODO: Complete the division operation
                    //A string can be converted to a double by calling the following: Double.parseDouble(yourDoubleAsAString)
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
