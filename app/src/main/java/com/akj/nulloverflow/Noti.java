package com.akj.nulloverflow;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Noti extends Application {
    public static final String SEAT_CHANNEL = "roomAlarm";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notiChannel = new NotificationChannel(SEAT_CHANNEL, "자리가 사용 가능 함", NotificationManager.IMPORTANCE_DEFAULT);
            notiChannel.setDescription("자리가 사용 가능할 때 사용되는 채널입니다.");

            NotificationManager notiManager = (NotificationManager) getSystemService(NotificationManager.class);
            notiManager.createNotificationChannel(notiChannel);
        }
    }
}
