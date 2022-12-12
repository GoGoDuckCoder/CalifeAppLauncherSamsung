package com.banqu.samsung.music.adapter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.banqu.samsung.music.R;
import com.banqu.samsung.music.SettingsActivity;
import com.banqu.samsung.music.adapter.ActivityManager;
import com.banqu.samsung.music.adapter.MyFragmentDisplayer;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_settings_preferences, rootKey);

        Preference close = findPreference("oneui_shortcut_close");
        close.setOnPreferenceClickListener(this);
        Preference ui = findPreference("oneui_shortcut_ui");
        ui.setOnPreferenceClickListener(this);
        Preference app = findPreference("oneui_shortcut_app");
        app.setOnPreferenceClickListener(this);
        Preference music = findPreference("oneui_shortcut_music");
        music.setOnPreferenceClickListener(this);
        Preference floath = findPreference("oneui_shortcut_float");
        floath.setOnPreferenceClickListener(this);
        Preference fullscreen = findPreference("oneui_shortcut_fullscreen");
        fullscreen.setOnPreferenceClickListener(this);
        Preference navbar = findPreference("oneui_shortcut_navbar");
        navbar.setOnPreferenceClickListener(this);
        Preference notification = findPreference("oneui_shortcut_notification");
        notification.setOnPreferenceClickListener(this);
        Preference launch = findPreference("oneui_shortcut_launch");
        launch.setOnPreferenceClickListener(this);
        Preference lab = findPreference("oneui_shortcut_lab");
        lab.setOnPreferenceClickListener(this);


    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {

        Intent i = new Intent();
        switch (preference.getKey()) {
            case "oneui_shortcut_close":
                ActivityManager.getInstance().clearAll();
                break;
            case "oneui_shortcut_ui":
                i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsUIFragment.class.getName());
                startActivity(i);
                break;

            case "oneui_shortcut_app":
                i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsAppDrawerFragment.class.getName());
                startActivity(i);
                break;

            case "oneui_shortcut_music":
                i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsMusicFragment.class.getName());
                startActivity(i);
                break;
            case "oneui_shortcut_float":
                i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsTaFragment.class.getName());
                startActivity(i);
                break;
            case "oneui_shortcut_fullscreen":
                i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsFsFragment.class.getName());
                startActivity(i);
                break;
            case "oneui_shortcut_navbar":
                i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsGodFragment.class.getName());
                startActivity(i);
                break;
            case "oneui_shortcut_notification":
                i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsNotificationFragment.class.getName());
                startActivity(i);
                break;

            case "oneui_shortcut_launch":
                i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsBootFragment.class.getName());
                startActivity(i);
                break;

            case "oneui_shortcut_lab":
                i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                i.putExtra("className", SettingsActivity.SettingsEXPFragment.class.getName());
                startActivity(i);
                break;

        }
        return true;
    }
}