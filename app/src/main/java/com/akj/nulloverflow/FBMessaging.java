package com.akj.nulloverflow;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.akj.nulloverflow.Noti.CAMERA_SERVICE;
import static com.akj.nulloverflow.Noti.SEAT_CHANNEL;

public class FBMessaging extends FirebaseMessagingService {
    private final Handler hander = new Handler(Looper.getMainLooper());
    private NotificationManagerCompat notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getData().get("message"));
        Log.i("test", remoteMessage.getData().get("message"));
        //Toast.makeText(FBMessaging.this, "messagereceived", Toast.LENGTH_SHORT).show();
        //Log.d("dd", "messagereceived");
    }

    private void showNotification(String body) {
        try {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            }
            else {
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            }

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notificationManager = NotificationManagerCompat.from(this);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, SEAT_CHANNEL)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    //.setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }
        catch (NullPointerException e) {
            hander.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "알림에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }, 0);
            Log.e("error Notify", e.toString());
        }

    }

}
