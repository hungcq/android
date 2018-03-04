package raijin.chapter7_lightsactionsoundredux;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickLightsActionSound(View view) {
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("LightsActionSoundRedux")
                .setContentText("Lights, Action & Sound")
                .setSound(notificationSoundUri)
                .setLights(Color.BLUE, 500, 500)
                .setVibrate(new long[]{250, 500, 250, 500, 250, 500});
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

        //use addAction() to create button with pending intent
    }

    public void expandedNotificationInboxStyle(View view) {
        NotificationCompat.Builder notificationBuilderInboxStyle =
                new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("InboxStyle - Big Content Title")
                .addLine("Line 1")
                .addLine("Line 2");
        notificationBuilderInboxStyle.setStyle(inboxStyle);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilderInboxStyle.build());
    }

    public void expandedNotificationBigPictureStyle(View view) {
        NotificationCompat.Builder notificationBuilderBigPictureStyle = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("LightsActionSoundRedux")
                .setContentText("BigPictureStyle");
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        notificationBuilderBigPictureStyle.setStyle(bigPictureStyle);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,
                notificationBuilderBigPictureStyle.build());
    }

    public void expandedNotificationBigTextStyle(View view) {
        NotificationCompat.Builder notificationBuilderBigTextStyle = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("LightsActionSoundRedux");
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText("This is an example of the BigTextStyle expanded notification.");
        notificationBuilderBigTextStyle.setStyle(bigTextStyle);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilderBigTextStyle.build());
    }
}
