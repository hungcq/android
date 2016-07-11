package raijin.session6_hw;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listView;
    private static List<PersonInfo> listEmployee = new ArrayList<>();
    private MyAdapter myAdapter;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView) findViewById(R.id.list_employee);
        backButton = (Button) findViewById(R.id.back_button);
        Bundle bundle = getIntent().getExtras();
        PersonInfo personInfo = new PersonInfo(bundle.getString("name"),bundle.getString("age"),
                bundle.getString("phone"),bundle.getString("address"),bundle.getString("gender"));

        listEmployee.add(personInfo);
        myAdapter = new MyAdapter(listEmployee,this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle(listEmployee.get(i).getName()+" Details");
        ((TextView) dialog.findViewById(R.id.dialog_nametext)).setText(listEmployee.get(i).getName());
        ((TextView) dialog.findViewById(R.id.dialog_agetext)).setText(listEmployee.get(i).getAge());
        ((TextView) dialog.findViewById(R.id.dialog_addresstext)).setText(listEmployee.get(i).getAddress());
        ((TextView) dialog.findViewById(R.id.dialog_phonetext)).setText(listEmployee.get(i).getPhone());
        ((TextView) dialog.findViewById(R.id.dialog_gendertext)).setText(listEmployee.get(i).getGender());
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, new MainActivity().getClass());
        if(view.getId() == R.id.back_button) {
            startActivity(intent);
        }
    }
}
