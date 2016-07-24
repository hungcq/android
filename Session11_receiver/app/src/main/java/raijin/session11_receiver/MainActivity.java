package raijin.session11_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static class MyBroadCastReceiver extends BroadcastReceiver {

        public MyBroadCastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String content = intent.getStringExtra("text");
            Toast.makeText(context,content, Toast.LENGTH_SHORT).show();
        }
    }
}
