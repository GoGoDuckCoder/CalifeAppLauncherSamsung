package com.banqu.samsung.music.carlifeapplauncher.alive;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.banqu.samsung.music.R;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class Alive extends Service {

    private Context context;

    private NotificationManager notificationManager;
    private static String id = "mainactivity";
    private static String name = "mainactivityservice";

    public Alive(Context context) {
        this.context = context;
    }

    public void onCreate() {
        notificationManager = context.getSystemService(NotificationManager.class);
        Notification mirror_service = createNotification();
        if (notificationManager != null && mirror_service != null) {
            notificationManager.notify(3141, mirror_service);
            startForeground(3141, mirror_service);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public void onDestory() {
//        notificationManager.cancel(31415926);
//        stopSelf();
//    }

    private Notification createNotification() {
        if (notificationManager == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
            // you can't change any channel property after channel is registered
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext(), id)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("车联助手")
                    .setContentText("主应用正在运行")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            return notificationBuilder.build();
//            notificationManager.notify(1,notificationBuilder.build());
        }
        return null;
    }
}
