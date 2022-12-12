package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class NightMode {
    public static void setCustomNightModeSetting(AppCompatActivity activity) {
       String ui_mode = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getString("ui_mode", "dark");
        switch (ui_mode) {
            case "dark":
                activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "light":
                activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "system":
                activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            default:
                activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
    public static void setCustomNightModeSetting(Window window,AppCompatActivity activity) {
        String ui_mode = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getString("ui_mode", "dark");


        switch (ui_mode) {
            case "dark":
                activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                break;
            case "light":
                activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                break;
            case "system":
                activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            default:
                activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
