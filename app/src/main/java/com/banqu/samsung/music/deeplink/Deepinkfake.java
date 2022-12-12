package com.banqu.samsung.music.deeplink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.banqu.samsung.music.R;
import com.banqu.samsung.music.adapter.ActivityManager;
import com.banqu.samsung.music.carlifeapplauncher.MyAccessibilityService;
import com.banqu.samsung.music.carlifeapplauncher.adapter.NightMode;

public class Deepinkfake extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_deepinkfake);
        NightMode.setCustomNightModeSetting(this);
        ActivityManager.getInstance().add(this);

        Intent i = new Intent();
        i.setClassName(this,DeepLinkService.class.getName());
        startActivity(i);

        finish();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().remove(this);
        super.onDestroy();
    }

}