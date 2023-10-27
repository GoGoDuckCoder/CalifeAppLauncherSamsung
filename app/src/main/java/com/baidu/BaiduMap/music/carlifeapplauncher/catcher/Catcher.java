package com.baidu.BaiduMap.music.carlifeapplauncher.catcher;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

public class Catcher {
    private static String TAG = "CatcherReceiver";
    private Context context;
    private CatcherReceiver catcherReceiver;

    public Catcher(Context context) {
        this.context = context;
        this.catcherReceiver = new CatcherReceiver();
        IntentFilter filter = new IntentFilter();

        filter.addAction("android.intent.action.BOOT_COMPLETED");
        filter.addAction("android.intent.action.ACTION_SHUTDOWN");
        filter.addAction("com.samsung.intent.action.LAZY_BOOT_COMPLETE");
        filter.addAction("ACTION_SCREEN_ON");
        filter.addAction("ACTION_SCREEN_OFF");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(catcherReceiver, filter);
        Log.i(TAG, "Catcher: Running");
    }

    public void onDestory() {
        context.unregisterReceiver(catcherReceiver);
    }
}
