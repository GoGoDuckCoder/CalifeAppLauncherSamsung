package com.banqu.samsung.music.deeplink;

import android.os.Bundle;

import com.banqu.samsung.music.log.xLog;
import com.carlifeapplauncher.MyAccessibilityService;
import com.carlifeapplauncher.adapter.NavBar;
import com.carlifeapplauncher.adapter.NotificationFactory;
import com.carlifeapplauncher.adapter.TouchAssistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class DeepLinkService extends AppCompatActivity {

    private static String TAG = "DeepLinkService";
    public static DeepLinkService deepLinkService;


    public boolean auto_godmode;
    public boolean auto_ta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deepLinkService = this;

        auto_godmode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("godmode_auto", false);
        auto_ta = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("touchassistant_auto", false);

//        setContentView(R.layout.activity_main);
//        Intent i = getIntent();
//        String task = i.getData().toString();
//        task = task.substring(29);
//
//        xLog.log(TAG, task);
//
//        switch (task) {
//            case "navbar":
//                if (NavBar.getInstance() == null) {
//                    xLog.log(TAG, "Navbar Create");
//                    NavBar.createInstance(this).onStart();
//
//                } else {
//                    xLog.log(TAG, "Navbar Existed");
//                }
//                break;
//        }
//        finish();

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



        moveTaskToBack(true);
    }


    public static DeepLinkService getInstance() {
        return deepLinkService;
    }

    @Override
    protected void onDestroy() {
        destroyAllService();
        if(MyAccessibilityService.getInstance()!=null)
        {
            MyAccessibilityService.getInstance().setConnection(false);
        }
        deepLinkService = null;
        super.onDestroy();
    }

    public void destroyAllService() {
        //close all service
        if(auto_godmode)
        {
            if (NavBar.getInstance() != null) {
                NavBar.getInstance().onDestroy();
            }
        }
        if(auto_ta)
        {
            if (TouchAssistant.getInstance() != null) {
                TouchAssistant.getInstance().onDestroy();
            }
        }
    }

}