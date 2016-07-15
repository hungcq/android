package raijin.session7_hw;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private static List<PersonInfo> listEmployee = new ArrayList<>();
    private MyAdapter myAdapter;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        backButton = (Button) findViewById(R.id.back_button);
        Bundle bundle = getIntent().getExtras();
        PersonInfo personInfo = new PersonInfo(bundle.getString("name"),bundle.getString("age"),
                bundle.getString("phone"),bundle.getString("address"),bundle.getString("gender"));

        listEmployee.add(personInfo);
        myAdapter = new MyAdapter(listEmployee,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(myAdapter);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, new MainActivity().getClass());
        if(view.getId() == R.id.back_button) {
            startActivity(intent);
        }
    }
}
