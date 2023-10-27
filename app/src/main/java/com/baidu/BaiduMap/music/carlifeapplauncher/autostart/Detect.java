package com.baidu.BaiduMap.music.carlifeapplauncher.autostart;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

public class Detect extends Service {

    private static String TAG = "AutostartReceiver";
    private static int count = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
           // Log.i(TAG, "onCreate: called");
        if(!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("exp_autostart", false))
        {
//            Log.i(TAG, "onCreate: stopself");
            stopSelf();
            return;
        }

        boolean adapt = false;

        ContentResolver cr = getContentResolver();
        try {
            Bundle bd = cr.call("com.samsung.android.carlink.carlife.openprovider", "get_connection_status", null, null);
            int status = bd.getInt("connection_status");
            adapt = true;
        } catch (Exception e) {
//            Log.i("TTTT", "adapt: " + e);
        }

        if (adapt && !PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("exp_package", "false").equals("false")) {
            Timer tiktok = new Timer();
            int limit =Integer.parseInt( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("exp_delay","3"));
            tiktok.schedule(new TimerTask() {
                @SuppressLint("WrongConstant")
                @Override
                public void run() {
                    Bundle bd = cr.call("com.samsung.android.carlink.carlife.openprovider", "get_connection_status", null, null);
                    int status = bd.getInt("connection_status");
                    if (status == 0) {
//                        Log.i(TAG, "Disconnected!");
                        count =0;
                    } else {
//                        Log.i(TAG, "Connected!");

                        if(count==limit)
                        {
                            String uri = "banqumusic://tasker/"+PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("exp_package","false");
//                            String uri = "banqumusic://zombie/";
                            Uri parse = Uri.parse("samsungcarlink://link/----"+uri);
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.setData(parse);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(0x10104000);
                            Log.i("MyAccessibilityService", "context change: ");
                            startActivity(intent);
                        }
                        count++;
                    }
                }
            }, 0, 1000);
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
//        Intent service = new Intent(getApplicationContext(), Detect.class);
//        startService(service);
//        super.onDestroy();

    }
}
