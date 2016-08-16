package com.example.linhdq.sessiondbrealm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //view
    private EditText edtName;
    private EditText edtAge;
    private EditText edtAddress;
    private Button btnSave;
    private ListView listView;

    //realm
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

    }

    private void init() {
        edtName = (EditText) findViewById(R.id.edt_name);
        edtAge = (EditText) findViewById(R.id.edt_age);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        btnSave = (Button) findViewById(R.id.btn_save);
        listView = (ListView) findViewById(R.id.list_view);

        //
        realm = Realm.getDefaultInstance();

        //
        btnSave.setOnClickListener(this);
    }

    private boolean validateData(String name, String age) {
        return !TextUtils.isEmpty(name.trim()) && !TextUtils.isEmpty(age.trim());
    }

    private void getData(){

        studentList=realm.where(Student.class).findAll();

        if(adapter==null){
            adapter=new ListAdapter(studentList,this);
            listView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        name = edtName.getText().toString();
        age = edtAge.getText().toString();
        address = edtAddress.getText().toString();
        int id=0;

        if (validateData(name, age)) {
            //make auto increment id
            try{
                id = realm.where(Student.class).max("id").intValue() + 1;
            }catch (Exception e){
                e.printStackTrace();
            }

            //save to database
            try {
                int ageNumber=Integer.parseInt(age);

                realm.beginTransaction();
                Student student = new Student(id,name,ageNumber,address);
                realm.copyToRealm(student);
                realm.commitTransaction();

                Toast.makeText(this,"Saved!",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }
            //sync data for list view
            getData();
        }
    }
}
