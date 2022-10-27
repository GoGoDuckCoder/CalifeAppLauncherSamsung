package com.banqu.samsung.music.deeplink;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.banqu.samsung.music.adapter.ActivityManager;
import com.banqu.samsung.music.carlifeapplauncher.adapter.FakeStart;

public class Deeplink extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().add(this);

        Intent i = getIntent();
        String uri = i.getData().toString();
        uri = uri.substring(20);

        FakeStart.Start(this,uri);

        finish();
    }

    @Override
    protected void onDestroy()
    {
        ActivityManager.getInstance().remove(this);
        super.onDestroy();
    }


}