package raijin.session3_hw;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by 1918 on 04-Jul-16.
 */
public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView nameText;
    private TextView birthDayText;
    private TextView birthPlaceText;
    private TextView genderText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String birthDay = bundle.getString("birthDay");
        String birthPlace = bundle.getString("birthPlace");
        String gender = bundle.getString("gender");

        nameText = (TextView) findViewById(R.id.name);
        birthPlaceText = (TextView) findViewById(R.id.birthplace);
        birthDayText = (TextView) findViewById(R.id.birthday);
        genderText = (TextView) findViewById(R.id.gender);

        backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(this);

        nameText.setText("Name: " + name);
        birthDayText.setText("BirthDay: " + birthDay);
        birthPlaceText.setText("BirthPlace: " + birthPlace);
        genderText.setText("Gender: " + gender);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, new MainActivity().getClass());
        if(view.getId() == R.id.back_button) {
            startActivity(intent);
        }
    }
}
