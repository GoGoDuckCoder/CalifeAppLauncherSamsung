package com.banqu.samsung.music.carlifeapplauncher.adapter;

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
}
