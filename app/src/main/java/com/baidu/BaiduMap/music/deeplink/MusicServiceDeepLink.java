package com.baidu.BaiduMap.music.deeplink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;


import com.baidu.BaiduMap.music.carlifeapplauncher.music.MediaSessionConnectionOperator;

public class MusicServiceDeepLink extends AppCompatActivity {

    private static String TAG = "CLINK MusicServiceDeepLink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.i(TAG, " 服务被调用！");
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("music_mirror", false)) {
            MediaSessionConnectionOperator.getInstance(getApplicationContext()).connect();
        } else {
            // Toast.makeText(this, "请在设置中启动音乐镜像服务，再尝试！", Toast.LENGTH_LONG).show();
        }

        finish();
    }


}