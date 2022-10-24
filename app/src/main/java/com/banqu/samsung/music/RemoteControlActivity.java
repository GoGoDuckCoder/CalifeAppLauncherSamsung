package com.banqu.samsung.music;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.banqu.samsung.music.adapter.MyFragmentDisplayer;
import com.carlifeapplauncher.adapter.Common;
import com.carlifeapplauncher.adapter.NightMode;
import com.carlifeapplauncher.apps.AppsUI;

import androidx.appcompat.app.AppCompatActivity;

public class RemoteControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NightMode.setCustomNightModeSetting(this);
        setContentView(R.layout.activity_remote_control);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewGroup location_vg = findViewById(R.id.remoteholder);
        location_vg.removeAllViews();
        AppsUI appsUI = new AppsUI(this, getLayoutInflater(), location_vg, true);

        TextView version = findViewById(R.id.version);
        version.setText("车联服务版本: " + Common.getCarlinkVersionName(this));

        Button add = findViewById(R.id.addfavo);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsAppDrawerFragment.class.getName());
                startActivity(i);
            }
        });
    }
}