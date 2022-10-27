package com.banqu.samsung.music.carlifeapplauncher.autostart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutostartReceiver extends BroadcastReceiver {

//    private  static AutostartReceiver screenONOFFBroadcastReceiver;
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("music_mirror", false)
//                && PreferenceManager.getDefaultSharedPreferences(context).getBoolean("music_mirror_autostart", false)) {
//            Log.i(TAG, "开机自启动音乐镜像服务，待命中");
//            MediaSessionConnectionOperator.getInstance(context).connect();
//        }
//        Toast.makeText(context,intent.getAction().toString(),Toast.LENGTH_LONG).show();
//        Log.i(TAG, "onReceive: "+intent.getAction().toString());
//        Intent i = new Intent();
//        i.setClassName(context.getPackageName(), MainActivity.class.getName());
//        context.startActivity(i);

//        AutostartReceiver screenONOFFBroadcastReceiver = this;
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.intent.action.SCREEN_ON");
//        intentFilter.addAction("android.intent.action.SCREEN_OFF");
//        context.registerReceiver(this,intentFilter);
//
//        Log.i("AutostartReceiver", "onReceive: "+intent.getAction());
//        Intent service = new Intent(context, Detect.class);
//        context.startService(service);
    }


}
