package raijin.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by 1918 on 03-Jul-16.
 */
public class ResultActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent resultIntent = getIntent();
        int firstNumber = resultIntent.getIntExtra("firstNumber",0);
        int secondNumber = resultIntent.getIntExtra("secondNumber",0);
        int result = resultIntent.getIntExtra("resultNumber",0);
        String operand = resultIntent.getStringExtra("operand");

        Button firstNumberButton = (Button) findViewById(R.id.button4);
        Button secondNumberButton = (Button) findViewById(R.id.button7);
        Button operandButton = (Button) findViewById(R.id.button6);
        Button resultNumberButton = (Button) findViewById(R.id.button8);

        firstNumberButton.setText(Integer.toString(firstNumber));
        secondNumberButton.setText(Integer.toString(secondNumber));
        resultNumberButton.setText(Integer.toString(result));
        operandButton.setText(operand);
    }
}
