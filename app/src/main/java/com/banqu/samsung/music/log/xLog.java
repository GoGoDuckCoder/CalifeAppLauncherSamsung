package com.banqu.samsung.music.log;

import android.util.Log;

public class xLog {

    private static boolean DEBUG_FLAG = true;
    private static boolean LOG_FLAG = false;

    public static void log(String TAG, String LOG) {
        if (DEBUG_FLAG) {
            Log.i(TAG, LOG);
        }
    }

    private static void append(String LOG)
    {

    }
}
