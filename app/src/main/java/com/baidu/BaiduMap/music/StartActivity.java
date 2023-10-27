package com.baidu.BaiduMap.music;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.BaiduMap.music.OneUiHomeActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(StartActivity.this, OneUiHomeActivity.class));
        finish();
    }
}