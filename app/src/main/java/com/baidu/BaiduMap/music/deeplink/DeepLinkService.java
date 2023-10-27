package com.baidu.BaiduMap.music.deeplink;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.baidu.BaiduMap.music.DebugUi;
import com.baidu.BaiduMap.music.MiniMapActivity;
import com.baidu.BaiduMap.music.R;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FlavorMode;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NotificationFactory;
import com.baidu.BaiduMap.music.carlifeapplauncher.MyAccessibilityService;
import com.baidu.BaiduMap.music.carlifeapplauncher.NotificationListener;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.Common;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FakeStart;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NavBar;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NightMode;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.TouchAssistant;
import com.baidu.BaiduMap.music.notouch.MouseService;
import com.baidu.mobstat.StatService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class DeepLinkService extends AppCompatActivity {

    private static String TAG = "DeepLinkService";
    public static DeepLinkService deepLinkService;

    public static NotificationFactory notificationFactory;

    public boolean auto_godmode;
    public boolean auto_ta;
    public boolean auto_noti;
    public boolean auto_fs;
    public boolean auto_ass;
    public boolean auto_mouse;
    public String auto_ass_list = "";


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NightMode.setCustomNightModeSetting(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_map);

        StatService.init(getApplicationContext(), "2621824db8", "GDJXC");
        StatService.setAuthorizedState(getApplicationContext(), true);
        StatService.autoTrace(getApplicationContext());


        Log.i("MAPTESTING", "DeepLinkService onCreate");
        deepLinkService = this;


        if (Common.getFlavor(this) == FlavorMode.zs && MyAccessibilityService.getInstance() != null) {
            MyAccessibilityService.getInstance().setConnection(true);
        }

        auto_godmode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("xc_god", false);
        auto_ta = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("xc_ta", false);
        auto_fs = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("xc_fs", false);
        auto_noti = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("xc_notifications", false);

        auto_ass = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("ass_keeper", false);
        auto_mouse = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("xc_mouse", false);

        //test
//        auto_ass = true;

        auto_ass_list = ":com.baidu.BaiduMap/com.baidu.BaiduMap.music.carlifeapplauncher.MyAccessibilityService";

        if (auto_ass) {
            String ass_list = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (!ass_list.contains(auto_ass_list)) {
                Settings.Secure.putString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, ass_list + auto_ass_list);
                Settings.Secure.putInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 1);
            }
        }

        if (auto_godmode) {
            if (NavBar.getInstance() == null) {
                NavBar.createInstance(this).onStart();
            } else {
            }
        }

        if (auto_ta) {
            if (TouchAssistant.getInstance() == null) {
                TouchAssistant.createInstance(this).onStart();
            } else {
            }
        }

        if (auto_noti) {
            if (NotificationListener.isEnabled(this)) {
                notificationFactory = NotificationFactory.getInstance(this, getComponentName());
            } else {
                Toast.makeText(this, "请重新开启通知助手开关,并开启读取通知权限.", Toast.LENGTH_LONG).show();
            }
        }


        //Boot
//        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("exp_autostart", false)) {
//            String pkg = PreferenceManager.getDefaultSharedPreferences(this).getString("exp_package", "false");
//            if (pkg != null && !pkg.equals("false")) {
//                FakeStart.Start(this, pkg);
//            }
//        }
        if (auto_fs) {
            Common.immersive_on(getApplicationContext());
        }


        if (auto_mouse) {
            if (!auto_ass && !Common.checkAccessibilityPermission(this, MyAccessibilityService.class)) {
                Common.requestAccessibilityPermission(this);
            } else {
                MouseService.createInstance(this);
                MouseService.getInstance(this).setDisplayId(this.getDisplay().getDisplayId());
            }
        }

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent("carlife.intent.action.openpage");
        i.setClassName("com.baidu.carlife", "com.baidu.carlife.CarlifeActivity");
        i.putExtra("pageid", 1);
        i.addFlags(0x10104000);
        startActivity(i);
    }

    public static DeepLinkService getInstance() {
        return deepLinkService;
    }

    @Override
    protected void onDestroy() {
        destroyAllService();
        super.onDestroy();

        if (Common.getFlavor(this) == FlavorMode.zs && MyAccessibilityService.getInstance() != null) {
            MyAccessibilityService.getInstance().setConnection(false);
        }

    }

    public void destroyAllService() {
        //close all service
        if (auto_ass) {
            String ass_list = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (ass_list.contains(auto_ass_list)) {
                ass_list = ass_list.replace(auto_ass_list, "");
                Settings.Secure.putString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, ass_list);
                Settings.Secure.putInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 1);
            }
        }
        if (auto_godmode) {
            if (NavBar.getInstance() != null) {
                NavBar.getInstance().onDestroy();
            }
        }
        if (auto_ta) {
            if (TouchAssistant.getInstance() != null) {
                TouchAssistant.getInstance().onDestroy();
            }
        }
        if (auto_fs) {
            Common.immersive_off(getApplicationContext());
        }
        if (auto_noti && notificationFactory != null) {
            notificationFactory.onDestroy();
        }
        if (auto_mouse) {
            if (MouseService.getInstance(this) != null) {
                MouseService.getInstance(this).onDestroy();
            }
        }

    }

}