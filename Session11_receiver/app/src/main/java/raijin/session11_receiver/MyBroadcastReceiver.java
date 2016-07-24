package raijin.session11_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by 1918 on 24-Jul-16.
 */
public class MyBroadCastReceiver extends BroadcastReceiver {

    public MyBroadCastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String content = intent.getStringExtra("text");
        Toast.makeText(context,content, Toast.LENGTH_SHORT).show();
    }
}