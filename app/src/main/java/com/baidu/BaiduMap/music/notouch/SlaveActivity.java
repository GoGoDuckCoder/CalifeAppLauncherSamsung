package com.baidu.BaiduMap.music.notouch;

import static com.baidu.BaiduMap.music.notouch.MirrorDisplay.mMediaProjectionManager;
import static com.baidu.BaiduMap.music.notouch.MirrorDisplay.mirrorDisplay;

import android.app.Activity;
import android.content.Context;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SlaveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("NOWING","1");
        if (mMediaProjectionManager == null) {
            Log.i("NOWING","2");
            mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }
        if(mirrorDisplay!=null)
        {
            Log.i("NOWING","3");
            mirrorDisplay.request_permission();
        }
        else
        {
            Log.i("NOWING","4");
            Toast.makeText(getApplicationContext(),"running",Toast.LENGTH_LONG).show();
        }
    }


}