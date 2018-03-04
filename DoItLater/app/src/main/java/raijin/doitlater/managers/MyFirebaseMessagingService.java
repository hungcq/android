package raijin.doitlater.managers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import raijin.doitlater.R;
import raijin.doitlater.activities.NotificationActivity;

/**
 * Created by HP on 9/15/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private RemoteViews remoteViews;
    private ImageView imageView;
    private TextView textView;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody, String messageTitle) {
        createView(messageBody);
        //
        Intent intent = new Intent(this, NotificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("content", messageBody);
        intent.putExtra("mypackage", bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setCustomContentView(remoteViews)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void createView(String messageBody) {
        if (messageBody.length() > 20) {
            messageBody = messageBody.substring(0, 20);
            messageBody += messageBody + "...";
        }
        remoteViews = new RemoteViews(getPackageName(), R.layout.firebase_cloud_messaging);
        remoteViews.setTextViewText(R.id.txt_content, messageBody);
    }
}
