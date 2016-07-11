package raijin.session6_hw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    List<String> listEmployee;
    EditText nameEditText;
    EditText ageEditText;
    EditText phoneEditText;
    EditText addressEditText;
    Button saveButton;
    LinearLayout itemLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_employee);
        nameEditText = (EditText) findViewById(R.id.name_et);
        ageEditText = (EditText) findViewById(R.id.age_et);
        phoneEditText = (EditText) findViewById(R.id.phone_et);
        addressEditText = (EditText) findViewById(R.id.addr_et);
        saveButton = (Button) findViewById(R.id.save_button);
        itemLayout = (LinearLayout) findViewById(R.id.item_layout);

        saveButton.setOnClickListener(this);

        listEmployee = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {

    }
}
