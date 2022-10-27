package com.banqu.samsung.music.deeplink;

import android.os.Bundle;

import com.banqu.samsung.music.adapter.ActivityManager;
import com.banqu.samsung.music.log.xLog;
import com.banqu.samsung.music.carlifeapplauncher.MyAccessibilityService;
import com.banqu.samsung.music.carlifeapplauncher.NotificationListener;
import com.banqu.samsung.music.carlifeapplauncher.adapter.Common;
import com.banqu.samsung.music.carlifeapplauncher.adapter.FakeStart;
import com.banqu.samsung.music.carlifeapplauncher.adapter.NavBar;
import com.banqu.samsung.music.carlifeapplauncher.adapter.NightMode;
import com.banqu.samsung.music.carlifeapplauncher.adapter.NotificationFactory;
import com.banqu.samsung.music.carlifeapplauncher.adapter.TouchAssistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class DeepLinkService extends AppCompatActivity {

    private static String TAG = "DeepLinkService";
    public static DeepLinkService deepLinkService;


    public boolean auto_godmode;
    public boolean auto_ta;
    public boolean auto_noti;
    public boolean auto_fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NightMode.setCustomNightModeSetting(this);
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().add(this);
        deepLinkService = this;

        auto_godmode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("godmode_auto", false);
        auto_ta = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("touchassistant_auto", false);
        auto_noti = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notification_switch_auto", false);
        auto_fs = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fs_auto", false);

        if (auto_godmode) {
            if (NavBar.getInstance() == null) {
                xLog.log(TAG, "Navbar Create");
                NavBar.createInstance(this).onStart();
            } else {
                xLog.log(TAG, "Navbar Existed");
            }
        }

        if (auto_ta) {
            if (TouchAssistant.getInstance() == null) {
                xLog.log(TAG, "TouchAssistant Create");
                TouchAssistant.createInstance(this).onStart();
            } else {
                xLog.log(TAG, "TouchAssistant Existed");
            }
        }

        if (auto_noti) {
            NotificationListener.ensureConnection(getApplicationContext());
            if (NotificationFactory.getInstance() == null) {
                NotificationFactory.createInstance(this);
            }
        }


        //Boot
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("exp_autostart",false))
        {
            String pkg = PreferenceManager.getDefaultSharedPreferences(this).getString("exp_package","false");
            if(pkg!=null&&!pkg.equals("false"))
            {
                FakeStart.Start(this,pkg);
            }
        }
        if(auto_fs)
        {
            Common.immersive_on(getApplicationContext());
        }


        moveTaskToBack(true);
    }


    public static DeepLinkService getInstance() {
        return deepLinkService;
    }

    @Override
    protected void onDestroy() {
        destroyAllService();
        if (MyAccessibilityService.getInstance() != null) {
            MyAccessibilityService.getInstance().setConnection(false);
        }
        deepLinkService = null;
        ActivityManager.getInstance().remove(this);
        super.onDestroy();
    }

    public void destroyAllService() {
        //close all service
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
        if (auto_noti) {
            if (NotificationFactory.getInstance() != null) {
                NotificationFactory.getInstance().onDestroy();
            }
            if (NotificationListener.isReady()) {
                NotificationListener.getInstance().killConnection(this);
            }
        }
        if(auto_fs)
        {
            Common.immersive_off(getApplicationContext());
        }
    }

}