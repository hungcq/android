package raijin.session5;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private MyAdapter myAdapter;
    private DrawerLayout drawerLayout;
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.drawer_list);
//        myAdapter =  new MyAdapter(this,R.layout.list_item);
//        myAdapter.add("Inbox");
//        myAdapter.add("Sent");
//        listView.setAdapter(myAdapter);
        setTitle("DrawerLayout App");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        List<String> places = new ArrayList<>();
        places.add("Hanoi");
        places.add("Haiphong");
        places.add("Another planet");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,R.layout.drawer_list_item,places);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_setting:
                Intent intent = new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.open_drawer:
                if(flag) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                    flag = false;
                } else {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    flag = true;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    class MyAdapter extends ArrayAdapter<String> {
//        public MyAdapter(Context context, int resource) {
//            super(context,resource);
//        }
//    }
}
