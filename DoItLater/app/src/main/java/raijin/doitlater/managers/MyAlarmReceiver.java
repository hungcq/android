package raijin.doitlater.managers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import raijin.doitlater.R;
import raijin.doitlater.activities.NoteActivity;

/**
 * Created by Qk Lahpita on 9/3/2016.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(context, NoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("ID", intent.getExtras().getInt("ID"));
        intent1.putExtra("widget", 1918);
        intent1.putExtras(bundle);

//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 13, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(intent1)
                .getPendingIntent(13, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(intent.getStringExtra("detail"));
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(false)
                .setContentTitle(intent.getStringExtra("title"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setLights(Color.BLUE, 500, 500)
                .setVibrate(new long[]{250, 500, 250, 500})
                .setSound(notificationSoundUri)
                .setStyle(bigTextStyle);

        mManager.notify(0, builder.build());
    }
}
