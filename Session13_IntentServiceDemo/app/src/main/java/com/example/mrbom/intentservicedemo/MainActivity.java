package com.example.mrbom.intentservicedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //
    public static final String KEY = "result";
    public static final String NUMBER_A = "a";
    public static final String NUMBER_B = "b";
    public static final String OPERATOR = "operator";

    //view
    private EditText edtNumberA;
    private EditText edtNumberB;
    private Button btnOperator01;
    private Button btnOperator02;
    private Button btnOperator03;
    private Button btnOperator04;
    private Button btnSubmit;
    private TextView txtOperation;
    private TextView txtResult;

    //
    private int numberA;
    private int numberB;
    private String operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init view
        init();
        //add listener
        addListener();

        //
        IntentFilter intentFilter = new IntentFilter(MyReceiver.RESPONSE_ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver, intentFilter);
    }

    private void init() {
        edtNumberA = (EditText) findViewById(R.id.edt_number_a);
        edtNumberB = (EditText) findViewById(R.id.edt_number_b);
        btnOperator01 = (Button) findViewById(R.id.btn_operator_01);
        btnOperator02 = (Button) findViewById(R.id.btn_operator_02);
        btnOperator03 = (Button) findViewById(R.id.btn_operator_03);
        btnOperator04 = (Button) findViewById(R.id.btn_operator_04);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        txtOperation = (TextView) findViewById(R.id.txt_operation);
        txtResult = (TextView) findViewById(R.id.txt_result);
        setDefaultValue();
    }

    private void addListener() {
        btnOperator01.setOnClickListener(this);
        btnOperator02.setOnClickListener(this);
        btnOperator03.setOnClickListener(this);
        btnOperator04.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void fillData(String operator) {
        try {
            this.operator = operator;
            numberA = Integer.parseInt(edtNumberA.getText().toString());
            numberB = Integer.parseInt(edtNumberB.getText().toString());
            txtOperation.setText(String.format("%d " + operator + " %d", numberA, numberB));
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Fail cmnr", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDefaultValue() {
        edtNumberA.setText("");
        edtNumberB.setText("");
        txtOperation.setText("");
        txtResult.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_operator_01: {
                fillData("+");
                break;
            }
            case R.id.btn_operator_02: {
                fillData("-");
                break;
            }
            case R.id.btn_operator_03: {
                fillData("*");
                break;
            }
            case R.id.btn_operator_04: {
                fillData("/");
                break;
            }
            case R.id.btn_submit: {
                try {
                    numberA = Integer.parseInt(edtNumberA.getText().toString());
                    numberB = Integer.parseInt(edtNumberB.getText().toString());
                    if (operator != null) {
                        Intent intent = new Intent(MainActivity.this, MyService.class);
                        intent.putExtra(MainActivity.NUMBER_A, numberA);
                        intent.putExtra(MainActivity.NUMBER_B, numberB);
                        intent.putExtra(MainActivity.OPERATOR, operator);
                        Toast.makeText(v.getContext(), "Bố đẩy đi rồi", Toast.LENGTH_SHORT).show();
                        startService(intent);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(v.getContext(), "Fail cmnr", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }

    public class MyReceiver extends BroadcastReceiver {
        public static final String RESPONSE_ACTION = "android.intent.action.MAIN";

        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(MainActivity.KEY, 0);
            Toast.makeText(context, "Tao nhận được rồi", Toast.LENGTH_SHORT).show();
            txtResult.setText(String.valueOf(result));
        }
    }
}
