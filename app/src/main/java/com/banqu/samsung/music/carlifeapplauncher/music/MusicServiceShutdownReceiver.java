package com.banqu.samsung.music.carlifeapplauncher.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.banqu.samsung.music.MainActivity;

public class MusicServiceShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TTTT", "onReceive: " + intent.getAction());
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_OFF:
                if (MainActivity.mainActivity == null&&MediaSessionConnectionOperator.getInstance(null)!=null) {
                    MediaSessionConnectionOperator.getInstance(null).disconnect();
                }
                break;
        }
    }
}
