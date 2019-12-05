package net.learn2develop.myapplication111.AlarmProfile;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import net.learn2develop.myapplication111.AlarmHistoryActivity.AlarmHistoryActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intentNot = new Intent(context, AlarmHistoryActivity.class);
        intentNot.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,intentNot,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("Notification title")
                .setContentText("Text")

                .setAutoCancel(true);



        notificationManager.notify(100,builder.build());
        Log.e("Ми в приемнике?", "Yay");  // потверждение того что вислали информацию в Receiver
        String get_your_string = intent.getExtras().getString("extra"); // стосуэться виключення музикик
        Log.e("What is the key?", get_your_string); // стосуэться виключення музикик
        Intent service_intent = new Intent(context, RingtonePlayingService.class);
        service_intent.putExtra("extra",get_your_string);
        // стосуэться виключення музикик
        context.startService(service_intent);
    }
}