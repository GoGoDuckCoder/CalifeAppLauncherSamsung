package com.baidu.BaiduMap.music.deeplink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FakeStart;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NightMode;
import com.baidu.BaiduMap.music.notouch.MirrorDisplay;

public class Deeplink extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NightMode.setCustomNightModeSetting(getWindow(), this);
        super.onCreate(savedInstanceState);


        String uri = getIntent().getData().toString();
        uri = uri.substring(20);
//        Log.i("logging",uri);
        String[] data = uri.split("/");
        String pkg = data[0];
        String task = null;
        if (data.length > 1) {
            task = data[1];
        }

        if (pkg.equals("com.banqu.samsung.music")&&task!=null) {
            if(task.equals("mirror"))
            {
                Intent i = new Intent();
                i.setClass(this, MirrorDisplay.class);
                startActivity(i);
            }

        } else {
            FakeStart.Start(this, pkg);
        }

        finish();
    }



}