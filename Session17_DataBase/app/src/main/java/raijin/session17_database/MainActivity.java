package raijin.session17_database;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText;
    private EditText ageEditText;
    private EditText addressEditText;
    private Button saveButton;
    private ListView listView;
    private DBcontext dBcontext;
    //
    private Realm realm;
    //
    private String name;
    private String age;
    private String address;
    //
    private ListAdapter adapter;
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
            }
        }
    }

    private void init() {
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        ageEditText = (EditText) findViewById(R.id.age_edit_text);
        addressEditText = (EditText) findViewById(R.id.address_edit_text);
        saveButton = (Button) findViewById(R.id.save_button);
        listView = (ListView) findViewById(R.id.list_view);
        realm = Realm.getDefaultInstance();
        dBcontext = DBcontext.getInst();
        saveButton.setOnClickListener(this);
        studentList = new ArrayList<>();
    }

    private boolean validateData(String name, String age) {
        return !TextUtils.isEmpty(name.trim()) && !TextUtils.isEmpty(age.trim());
    }

    private void getData() {
        studentList = dBcontext.getAllStudent();
        if (adapter == null) {
            adapter = new ListAdapter(studentList, this);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        name = nameEditText.getText().toString();
        age = ageEditText.getText().toString();
        address = addressEditText.getText().toString();
        int id = 0;

        if (validateData(name, age)) {
            //make auto increment id
            try {
                id = realm.where(Student.class).max("id").intValue() + 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

            //save to database
            try {
                int ageNumber = Integer.parseInt(age);

                Student student = new Student(id, name, ageNumber, address);
                dBcontext.addStudent(student);

                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //sync data for list view
            getData();
        }
    }
}
