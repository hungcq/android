package raijin.session3_hw;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveButton;
    public EditText birthdayET;
    public EditText nameET;
    public Spinner spinner;
    public RadioGroup radioGroup;
    private RadioButton radioButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveButton = (Button) findViewById(R.id.save_button);
        birthdayET = (EditText) findViewById(R.id.birthday_et);
        nameET = (EditText) findViewById(R.id.name_et);
        spinner = (Spinner) findViewById(R.id.spinner);
        radioGroup = (RadioGroup) findViewById(R.id.gender_rg);

        saveButton.setOnClickListener(this);
        birthdayET.setOnClickListener(this);

        List<String> places = new ArrayList<>();
        places.add("Hanoi");
        places.add("Haiphong");
        places.add("Another planet");
        ArrayAdapter<String> adaper = new ArrayAdapter<>(
                this,android.R.layout.simple_spinner_item,places);
        adaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaper);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ResultActivity.class);
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.birthday_et:
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                birthdayET.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }
                        }, 2000, 0, 1);
                dpd.show();
                break;
            case R.id.save_button:
                if(!nameET.getText().toString().isEmpty() &&
                        !birthdayET.getText().toString().isEmpty()) {
                    int radioButtonCheckedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(radioButtonCheckedId);
                    bundle.putString("gender",radioButton.getText().toString());
                    bundle.putString("name",nameET.getText().toString());
                    bundle.putString("birthDay",birthdayET.getText().toString());
                    bundle.putString("birthPlace",spinner.getSelectedItem().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}