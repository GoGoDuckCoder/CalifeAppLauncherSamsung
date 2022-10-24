package com.banqu.samsung.music.deeplink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.carlifeapplauncher.adapter.FakeStart;

public class Deeplink extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent i = getIntent();
        String uri = i.getData().toString();
        uri = uri.substring(20);

        FakeStart.Start(this,uri);

        finish();
    }


}