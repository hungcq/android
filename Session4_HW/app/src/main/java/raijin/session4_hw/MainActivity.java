package raijin.session4_hw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout haiphongLayout;
    private LinearLayout namdinhLayout;
    private LinearLayout hanamLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter(MyReceiver.RESPONE_ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver, intentFilter);

        haiphongLayout = (LinearLayout) findViewById(R.id.haiphong);
        namdinhLayout = (LinearLayout) findViewById(R.id.namdinh);
        hanamLayout = (LinearLayout) findViewById(R.id.hanam);
        haiphongLayout.setOnClickListener(this);
        namdinhLayout.setOnClickListener(this);
        hanamLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,WeatherService.class);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.haiphong: {
                bundle.putString("city","Haiphong");
                intent.putExtras(bundle);
                startService(intent);
                break;
            }
            case R.id.namdinh: {
                bundle.putString("city","Namdinh");
                intent.putExtras(bundle);
                startService(intent);
                break;
            }
            case R.id.hanam: {
                bundle.putString("city","Hanam");
                intent.putExtras(bundle);
                startService(intent);
                break;
            }
        }
    }

    public class MyReceiver extends BroadcastReceiver {
        public static final String RESPONE_ACTION = "android.intent.action.MAIN";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.d("DMM","xong");
            Intent changeActivityIntent = new Intent(MainActivity.this,DetailsActivity.class);
            changeActivityIntent.putExtras(bundle);
            startActivity(changeActivityIntent);
        }
    }
}
