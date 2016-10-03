package raijin.session11_receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String text;

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
            switch (intent.getAction()) {
                case "abc":
                    String content = intent.getStringExtra("text");
                    text = content;
                    Notification.Builder builder = new Notification.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Warning!")
                            .setContentText(content);
                    NotificationManager notificationManager = (NotificationManager)
                            context.getSystemService(context.NOTIFICATION_SERVICE);
                    Intent resultIntent = new Intent(context, new ResultActivity().getClass());
                    resultIntent.putExtra("content", content);
                    PendingIntent resultPendingIntent = PendingIntent.getActivity(
                            context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
                    builder.setContentIntent(resultPendingIntent);
                    notificationManager.notify(0, builder.build());
                    break;
                case Intent.ACTION_AIRPLANE_MODE_CHANGED:
                    builder = new Notification.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Warning!")
                            .setContentText("Air Plane Mode Changed!");
                    notificationManager = (NotificationManager)
                            context.getSystemService(context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, builder.build());

                    break;
            }
        }
    }
}
