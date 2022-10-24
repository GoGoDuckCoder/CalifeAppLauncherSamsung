package com.carlifeapplauncher.catcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CatcherReceiver extends BroadcastReceiver {

    private static String TAG ="CatcherReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: "+intent.getAction());
    }
}
