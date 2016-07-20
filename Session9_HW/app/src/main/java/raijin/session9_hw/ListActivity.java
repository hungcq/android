package raijin.session9_hw;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listView;
    private static List<PersonInfo> listEmployee = new ArrayList();
    private MyAdapter myAdapter;
    private Button backButton;
    private FrameLayout frameLayout;
    private boolean isTwoPart = false;

    public ListActivity() {
    }

    private void initializeUI() {
        this.listView = (ListView)this.findViewById(R.id.list_employee);
        this.backButton = (Button)this.findViewById(R.id.back_button);
        this.frameLayout = (FrameLayout)this.findViewById(R.id.container);
        if(this.frameLayout != null) {
            this.isTwoPart = true;
        } else {
            this.isTwoPart = false;
        }

        this.myAdapter = new MyAdapter(listEmployee, this);
        this.listView.setAdapter(this.myAdapter);
        this.listView.setOnItemClickListener(this);
        if(this.isTwoPart) {
            this.listView.performItemClick(null, 0, (long)this.listView.getFirstVisiblePosition());
        }

        this.backButton.setOnClickListener(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list);
        Bundle bundle = this.getIntent().getExtras();
        PersonInfo personInfo = new PersonInfo(bundle.getString(MainActivity.NAME_KEY),
                bundle.getString(MainActivity.AGE_KEY), bundle.getString(MainActivity.PHONE_KEY),
                bundle.getString(MainActivity.ADDRESS_KEY), bundle.getString(MainActivity.GENDER_KEY));
        listEmployee.add(personInfo);
        this.initializeUI();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.setContentView(R.layout.activity_list);
        this.initializeUI();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(this.isTwoPart) {
            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.NAME_KEY, listEmployee.get(i).getName());
            bundle.putString(MainActivity.AGE_KEY, listEmployee.get(i).getAge());
            bundle.putString(MainActivity.PHONE_KEY, listEmployee.get(i).getPhone());
            bundle.putString(MainActivity.ADDRESS_KEY, listEmployee.get(i).getAddress());
            bundle.putString(MainActivity.GENDER_KEY, listEmployee.get(i).getGender());
            detailFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, detailFragment);
            fragmentTransaction.commit();
        } else {
            Dialog dialog1 = new Dialog(this);
            dialog1.setContentView(R.layout.custom_dialog);
            dialog1.setTitle((listEmployee.get(i)).getName() + " Details");
            ((TextView)dialog1.findViewById(R.id.dialog_nametext)).setText("Name: " + listEmployee.get(i).getName());
            ((TextView)dialog1.findViewById(R.id.dialog_agetext)).setText("Age: " + listEmployee.get(i).getAge());
            ((TextView)dialog1.findViewById(R.id.dialog_addresstext)).setText("Address: " + listEmployee.get(i).getAddress());
            ((TextView)dialog1.findViewById(R.id.dialog_phonetext)).setText("Phone: " + listEmployee.get(i).getPhone());
            ((TextView)dialog1.findViewById(R.id.dialog_gendertext)).setText("Gender: " + listEmployee.get(i).getGender());
            dialog1.show();
        }
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, (new MainActivity()).getClass());
        if(view.getId() == R.id.back_button) {
            this.startActivity(intent);
        }
    }
}
