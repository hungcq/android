package raijin.calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et;
    EditText et2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button plusButton = (Button) findViewById(R.id.button);
        Button minusButton = (Button) findViewById(R.id.button2);
        Button multiplyButton = (Button) findViewById(R.id.button5);
        Button divideButton = (Button) findViewById(R.id.button3);

        plusButton.setOnClickListener(this);
        minusButton.setOnClickListener(this);
        divideButton.setOnClickListener(this);
        multiplyButton.setOnClickListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        int firstNumber = Integer.parseInt(et.getText().toString());
        int secondNumber = Integer.parseInt(et2.getText().toString());
        int result;
        switch (v.getId()) {
            case R.id.button:
                result = firstNumber + secondNumber;
                intent.putExtra("operand","+");
                intent.putExtra("firstNumber",firstNumber);
                intent.putExtra("secondNumber",secondNumber);
                intent.putExtra("resultNumber",result);
                startActivity(intent);
                break;
            case R.id.button2:
                result = firstNumber - secondNumber;
                intent.putExtra("operand","-");
                intent.putExtra("firstNumber",firstNumber);
                intent.putExtra("secondNumber",secondNumber);
                intent.putExtra("resultNumber",result);
                startActivity(intent);
                break;
            case R.id.button5:
                result = firstNumber * secondNumber;
                intent.putExtra("operand","x");
                intent.putExtra("firstNumber",firstNumber);
                intent.putExtra("secondNumber",secondNumber);
                intent.putExtra("resultNumber",result);
                startActivity(intent);
                break;
            case R.id.button3:
                result = firstNumber / secondNumber;
                intent.putExtra("operand",":");
                intent.putExtra("firstNumber",firstNumber);
                intent.putExtra("secondNumber",secondNumber);
                intent.putExtra("resultNumber",result);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
