package raijin.session6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listViewContact;
    private List<String> listContactName;
    int x = 0;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewContact = (ListView) findViewById(R.id.list_contact);

        listContactName = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            listContactName.add("Hung" + Integer.toString(x + i));
        }

        myAdapter = new MyAdapter(listContactName,this);
        listViewContact.setAdapter(myAdapter);
        listViewContact.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this,"abc",Toast.LENGTH_SHORT).show();
    }
}
