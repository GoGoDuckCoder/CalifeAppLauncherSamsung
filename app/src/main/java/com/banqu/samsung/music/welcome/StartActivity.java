package com.banqu.samsung.music.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.banqu.samsung.music.OneUiHomeActivity;
import com.banqu.samsung.music.adapter.ActivityManager;
import com.banqu.samsung.music.carlifeapplauncher.adapter.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().add(this);
        boolean is = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FirstStartFlag", true);
        //判断是否为第一次打开软
        if (is) {
            if (Common.isInstalled(getApplicationContext(), "com.samsung.android.carlink")) {
                Toast.makeText(getApplicationContext(),"欢迎使用车联助手三星版",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StartActivity.this, IntroducttoryActivity.class));
            } else {
                Toast.makeText(this, "非最新国行系统:不支持三星Carlife定制版,请升级。", Toast.LENGTH_LONG).show();
            }
        } else {
            //直接进入首页
            startActivity(new Intent(StartActivity.this, OneUiHomeActivity.class));
        }
        finish();
    }

    @Override
    protected void onDestroy()
    {
        ActivityManager.getInstance().remove(this);
        super.onDestroy();
    }
}