package com.carlifeapplauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;

public class NotificationListener extends NotificationListenerService {


    private static String TAG = "NotificationListenerService";

    private static boolean task_notification;

    private static NotificationListener instance;
    private static boolean isReady;
    private ArrayList<String> whitelist;

    public NotificationListener() {
        instance = this;
        isReady = false;
        task_notification = false;
    }

    public static NotificationListener getInstance() {
        return instance;
    }

    public static boolean isEnabled(Context context) {
        return NotificationManagerCompat
                .getEnabledListenerPackages(context)
                .contains(context.getPackageName());
    }

    public static boolean isReady() {
        return instance != null && isReady;
    }

    public static void ensureConnection(Context context) {
        if (isReady()) {
//            Log.i(TAG, "ensureConnection: 已运行,返回");
        } else {
//            Log.i(TAG, "ensureConnection: 未运行，重连");

            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(new ComponentName(context.getApplicationContext(), NotificationListener.class),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(new ComponentName(context.getApplicationContext(), NotificationListener.class),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//            NotificationListener.requestRebind(new ComponentName(context, NotificationListener.class));

        }
    }
//    public void toggleNotificationListenerService() {
//        PackageManager pm = getPackageManager();
//        pm.setComponentEnabledSetting(new ComponentName(getApplicationContext(), NotifyService.class),
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//        pm.setComponentEnabledSetting(new ComponentName(getApplicationContext(), NotifyService.class),
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//    }

//    public static void ensureConnection(Context context) {
//        if (isReady()) {
//            Log.i(TAG, "ensureConnection: 已运行,返回");
//        } else {
//            Log.i(TAG, "ensureConnection: 未运行，重连");
//            NotificationListener.requestRebind(new ComponentName(context, NotificationListener.class));
//        }
//    }

    public void killConnection(Context context) {
        //clear all task
        if (isReady()) {
//            Log.i(TAG, "killConnection: kill");
            isReady = false;
            task_notification = false;
//            task_music = false;
//            focus = null;
//            title = null;
//            try {
//                task_music_timer.cancel();
//            } catch (Exception e) {
//
//            }
//            task_music_timer = null;
//            instance.requestUnbind();

            PackageManager pm = context.getPackageManager();

            pm.setComponentEnabledSetting(new ComponentName(context.getApplicationContext(), NotificationListener.class),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        }
    }


    @Override
    public void onListenerConnected() {
//        Log.i(TAG, "onListenerConnected: 服务已连接");
        isReady = true;
        task_notification = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("notification_switch", false);
        whitelist = getwhitelist();
    }

    @Override
    public void onListenerDisconnected() {
        task_notification = false;
        isReady = false;
//        Log.i(TAG, "onListenerDisconnected: 服务已断开");
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (task_notification) {
            
            if (!whitelist.contains(sbn.getPackageName())) {
                return;
            }
            Log.i(TAG, "onNotificationPosted: got message");
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            Log.i(TAG, sbn.getNotification().extras.toString());
            Log.i(TAG, sbn.toString());
            ApplicationInfo applicationInfo = (ApplicationInfo) sbn.getNotification().extras.get("android.appInfo");
            bundle.putString("packagename", sbn.getPackageName());
            bundle.putString("label", applicationInfo.loadLabel(getPackageManager()).toString());
            bundle.putString("title", sbn.getNotification().extras.getString("android.title"));
            bundle.putString("text", sbn.getNotification().extras.getString("android.text"));
            intent.putExtras(bundle);
            intent.setAction("GAODONGJIA.GetNotifications");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    private ArrayList<String> getwhitelist() {
        Set<String> set = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getStringSet("notification_whitelist", new HashSet<>());
        ArrayList<String> temp = new ArrayList<String>();
        temp.addAll(set);
        return temp;
    }

//    private Boolean get_music_info(StatusBarNotification sbn) {
//        if (sbn.getPackageName().equals(focus)) {
//            String titlefromextra = sbn.getNotification().extras.getString("android.title");
//            if (titlefromextra != null && titlefromextra.equals(title)) {
////                Log.i(TAG, "找到Album");
//                Icon a = sbn.getNotification().getLargeIcon();
//                if (a == null) {
//                    return false;
//                }
//                a.loadDrawable(getApplicationContext());
//                BitmapDrawable bd = (BitmapDrawable) (a.loadDrawable(getApplicationContext()));
//                MusicService.getInstance(getApplicationContext()).setup_Album_From_Notification(focus, title, bd.getBitmap());
//                return true;
//            }
//        }
//        return false;
//    }

//    private int tiktok;

//    public void task_music_start(String focus, String title) {
//        if (!isReady()) {
//            return;
//        }
//
//        Log.i(TAG, "封面采集：新任务");
//        if (task_music_timer != null) {
//            Log.i(TAG, "封面采集：新任务重置Timer");
//            task_music_timer.cancel();
//            task_music_timer = null;
//        }
//        //new task
//        task_music = true;
//        this.focus = focus;
//        this.title = title;
//        //Find if existed
//        StatusBarNotification[] statusBarNotifications = getActiveNotifications();
//        for (StatusBarNotification sbn : statusBarNotifications) {
//            if (get_music_info(sbn)) {
//                return;
//            }
//        }
//        //A New Task to Wait
//        tiktok = 0;
//        task_music_timer = new Timer();
//        task_music_timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (tiktok == 20) {
//                    Log.i(TAG, "封面采集：Timer结束");
//                    task_music_timer.cancel();
//                    task_music_timer = null;
//                    task_music = false;
//                    instance.focus = null;
//                    instance.title = null;
//                } else {
//                    StatusBarNotification[] statusBarNotifications = getActiveNotifications();
//                    for (StatusBarNotification sbn : statusBarNotifications) {
//                        if (get_music_info(sbn)) {
//                            break;
//                        }
//                    }
//                    tiktok++;
//                }
//            }
//        }, 0, 500);
//    }
}



