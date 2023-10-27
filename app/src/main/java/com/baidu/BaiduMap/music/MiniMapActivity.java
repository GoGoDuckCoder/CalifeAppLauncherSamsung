package com.baidu.BaiduMap.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FakeStart;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NightMode;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.OpenProvider;
import com.baidu.BaiduMap.music.deeplink.DeepLinkService;

public class MiniMapActivity extends AppCompatActivity {
    private static boolean ready = false;
    private static Context ins = null;
    private static String minimap = "false";

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MAPTESTING", "onCreate");
        NightMode.setCustomNightModeSetting(getWindow(), this);
        setContentView(R.layout.activity_mini_map);
        ins = this;
        minimap = PreferenceManager.getDefaultSharedPreferences(ins).getString("oneui_shortcut_xc", "false");

        String uri = "";
        try {
            uri = getIntent().getData().toString();
        } catch (Exception e) {

        }
        Log.i("MAPTESTING", "minimap:create:" + uri);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();

        Log.i("MAPTESTING", "onResume");
        String urit = "";
        try {
            urit = getIntent().getData().toString();
        } catch (Exception e) {

        }
        Log.i("MAPTESTING", "minimap:resume:" + urit);
        run();
        if (OpenProvider.isConnected(this)) {
            if (DeepLinkService.getInstance() == null) {
                Log.i("MAPTESTING", "Launching Deeplink");
                //deeplink to launch all the service
                String uri = "banqumusic://deeplinkservice/";
                Uri parse = Uri.parse("samsungcarlink://link/----" + uri);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(parse);
                intent.addFlags(0x10104000);
                startActivity(intent);
            }
        }
    }

    public static void run() {
        Log.i("MAPTESTING", "Run Called");
        FakeStart.Start(ins, minimap);
    }

    protected void onStop() {
        super.onStop();
    }

}