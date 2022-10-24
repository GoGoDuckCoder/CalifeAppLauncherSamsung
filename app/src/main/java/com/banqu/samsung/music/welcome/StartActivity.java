package com.banqu.samsung.music.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;

import com.banqu.samsung.music.HomeActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean is= PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FirstStartFlag",true);
        //判断是否为第一次打开软
        if (is){
            //跳转到引导页
            startActivity(new Intent(StartActivity.this,IntroducttoryActivity.class));
            finish();
        }else {
            //直接进入首页
//            startActivity(new Intent(StartActivity.this, HomeActivity.class));
            startActivity(new Intent(StartActivity.this, HomeActivity.class));
            finish();
        }

    }
}