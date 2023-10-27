package com.baidu.BaiduMap.music.deeplink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NightMode;

public class Deepinkfake extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_deepinkfake);
        NightMode.setCustomNightModeSetting(this);

        Intent i = new Intent();
        i.setClassName(this,DeepLinkService.class.getName());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


}