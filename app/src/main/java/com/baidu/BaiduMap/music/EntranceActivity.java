package com.baidu.BaiduMap.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.Common;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FakeStart;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NightMode;
import com.baidu.mobstat.StatService;

import java.util.HashMap;

public class EntranceActivity extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        StatService.init(getApplicationContext(), "2621824db8", "GDJXC");
        StatService.setAuthorizedState(getApplicationContext(), true);
        StatService.autoTrace(getApplicationContext());

        Log.i("Bar", "Run Called");
        String uri = "";
        try {
            uri = getIntent().getData().toString();
        } catch (Exception e) {

        }
        Log.i("MAPTESTING", "bar:create:" + uri);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();

        String minimap_bar = PreferenceManager.getDefaultSharedPreferences(this).getString("oneui_shortcut_xc_bar", "false");
        if (minimap_bar.equals("false")) {
            Log.i("Bar", "self");
            Intent i = new Intent();
            i.setClass(this, MainActivity.class);
            i.addFlags(0x10104000);
            ActivityOptions ao = ActivityOptions.makeBasic();
            ao.setLaunchDisplayId(this.getDisplay().getDisplayId());
            this.startActivity(i, ao.toBundle());
        } else {
            Log.i("Bar", "selected");
            String minimap = PreferenceManager.getDefaultSharedPreferences(this).getString("oneui_shortcut_xc", "false");

            if (minimap_bar.equals(minimap)) {
                FakeStart.StartMiniMapToContext(this);
            } else {
                FakeStart.Start(this, minimap_bar);
            }
        }


        Boolean du = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("du", false);
        if(du)
        {
            Intent intent = getIntent();
            if (intent != null && intent.getData() != null) {
                String uri = intent.getData().toString();
                Log.i("MAPTESTING", "bar:resume:" + uri);
                if (uri.startsWith("baidumap://hicarmap/direction")) {
                    Uri u = Uri.parse(uri);
                    String dest = u.getQueryParameter("destination");
                    String[] arr = dest.split("\\|");
                    String dest_String = arr[0].substring(5);
                    Log.i("MAPTESTING", "bar:dest_String:" + dest_String);
                    String dest_po = arr[1].substring(7);
                    String[] dest_po_arr = dest_po.split(",");
                    String dest_po_la = dest_po_arr[0];
                    Log.i("MAPTESTING", "bar:dest_po_la:" + dest_po_la);
                    String dest_po_ln = dest_po_arr[1];
                    Log.i("MAPTESTING", "bar:dest_po_ln:" + dest_po_ln);

                    openMapOperation("androidauto://route?sourceApplication=" + getPackageName() + "&dlat=" +
                            dest_po_la + "&dlon=" + dest_po_ln + "&dname=" + dest_String + "&dev= " + 0 + "&m=" + -1);

                }

                if (uri.startsWith("baidumap://hicarmap/navi/common?addr=home")) {
                    openMapOperation("androidauto://navi2SpecialDest?sourceApplication=" + getPackageName() + "&dest=home");
                }

                if (uri.startsWith("baidumap://hicarmap/navi/common?addr=company")) {
                    openMapOperation("androidauto://navi2SpecialDest?sourceApplication=" + getPackageName() + "&dest=crop");
                }
                setIntent(null);

            }
        }
    }


    private void openMapOperation(String url) {
        Intent intent = new Intent("android.intent.action.VIEW",
                android.net.Uri.parse(url));
        intent.setPackage("com.autonavi.amapauto");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}