package com.baidu.BaiduMap.music;

import static com.baidu.BaiduMap.music.SettingsActivity.createEntrie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.AppAnnouncement;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.AppInfo;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.Common;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FlavorMode;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NightMode;
import com.baidu.BaiduMap.music.databinding.ActivityOneUiHomeBinding;
import com.baidu.BaiduMap.music.notouch.NoTouchFragment;
import com.baidu.BaiduMap.music.ui.donate.DonateFragment;
import com.baidu.BaiduMap.music.ui.group.GroupFragment;
import com.baidu.mobstat.StatService;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class OneUiHomeActivity extends AppCompatActivity {

    private ActivityOneUiHomeBinding binding;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Night Mode
        NightMode.setCustomNightModeSetting(getWindow(), this);
        //Enable Binding
        binding = ActivityOneUiHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //BAIDU STATIC
        StatService.init(getApplicationContext(), "2621824db8", "GDJXC");
        StatService.setAuthorizedState(getApplicationContext(), true);
        StatService.autoTrace(getApplicationContext());
        //Dealing with fragments (root.xml files)
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new ShortcutsFragment())
                    .commit();
        }
        //Tool Bar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        //Notifications and Updates
        AppAnnouncement.run(this);
    }


    public static class ShortcutsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);


            Preference version = findPreference("layoutils");
            if (Common.isInstalled(requireContext(), "com.samsung.android.carlink")) {
                String versionName = Common.getCarlinkVersionName(getContext());
                version.setTitle("车联服务：" + versionName);
            } else {
                version.setTitle("抱歉，您当前的系统环境不支持三星Carlife定制版与车联助手插件！请检查系统更新，并升级国行最新系统，外版可刷带车联服务的第三方国行移植固件。感谢您的支持。如系统为ONEUI5.1.1以上，请手动打开读取应用列表权限。");
            }


            String[] keys = {
                    "oneui_shortcut_support_lockass",
                    "oneui_shortcut_instruction",
                    "oneui_shortcut_preview",
                    "oneui_shortcut_app",
                    "oneui_shortcut_music",
                    "oneui_shortcut_notouch",
                    "oneui_shortcut_close",
                    "oneui_shortcut_donation",
                    "oneui_shortcut_group",
                    "oneui_shortcut_update",
                    "oneui_shortcut_ui",
                    "oneui_shortcut_float",
                    "oneui_shortcut_fullscreen",
                    "oneui_shortcut_navbar",
                    "oneui_shortcut_launch",
                    "oneui_shortcut_lab",
                    "oneui_shortcut_notification",
                    "oneui_shortcut_minimap",
                    "oneui_shortcut_xc","oneui_shortcut_xc_bar",
            "oneui_shortcut_mouse"};
            for (String key_id : keys) {
                Preference key_preference = findPreference(key_id);
                if (key_preference != null) {
                    key_preference.setOnPreferenceClickListener(this);
                    if (key_id.equals("oneui_shortcut_update")) {
                        AppAnnouncement.runUpdate(requireContext(), key_preference);
                    }

                    if(Common.getFlavor(getContext())== FlavorMode.xc)
                    {
                        PreferenceCategory pc = findPreference("xc_pc");
                        pc.setVisible(true);
                        if (key_id.equals("oneui_shortcut_xc")||key_id.equals("oneui_shortcut_xc_bar")) {
                            ListPreference pref_jump_lp = findPreference(key_id);

                            String pkg = pref_jump_lp.getValue();

                            if (!Common.isInstalled(requireContext(), pkg)) {
                                pref_jump_lp.setValue("false");
                            }

                            ArrayList<SpannableString> entries = new ArrayList<SpannableString>();
                            ArrayList<String> values = new ArrayList<String>();
                            ArrayList<AppInfo> all_app = Common.getAllApps(requireContext());
                            for (AppInfo app : all_app) {
                                entries.add(createEntrie(requireContext(), app.label.toString(), app.icon));
                                values.add(app.packageName.toString());
                            }
                            entries.add(0, new SpannableString("关闭"));
                            values.add(0, "false");

                            pref_jump_lp.setEntries(entries.toArray(new SpannableString[entries.size()]));
                            pref_jump_lp.setEntryValues(values.toArray(new String[values.size()]));

                        }
                    }

                }
            }

//            boolean jumpswitch_value = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("jumpnotouch", false);
//            if (jumpswitch_value) {
//                navi_to_notouch();
//            }
        }


        @Override
        public boolean onPreferenceClick(@NonNull Preference preference) {
            Intent i = new Intent();
            switch (preference.getKey()) {
                case "oneui_shortcut_instruction":
                    Toast.makeText(requireContext(), "说明加载中", Toast.LENGTH_SHORT).show();
                    AppAnnouncement.runGuide(requireContext());
                    break;
                case "oneui_shortcut_preview":
                    i.setClass(requireContext(), MainActivity.class);
                    i.putExtra("helper",true);
                    startActivity(i);
                    break;

                case "oneui_shortcut_notouch":
                    navi_to_notouch();
                    break;

                case "oneui_shortcut_donation":
                    i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                    i.putExtra("className", DonateFragment.class.getName());
                    startActivity(i);
                    break;

                case "oneui_shortcut_group":
                    i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                    i.putExtra("className", GroupFragment.class.getName());
                    startActivity(i);
                    break;
                case "oneui_shortcut_close":
//                    ActivityManager.getInstance().clearAll();
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

                case "oneui_shortcut_mouse":
                    i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                    i.putExtra("className", SettingsActivity.SettingsMouseFragment.class.getName());
                    startActivity(i);
                    break;

                case "oneui_shortcut_support_lockass":
                    i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                    i.putExtra("className", SettingsActivity.SettingsASSFragment.class.getName());
                    startActivity(i);
                    break;

            }

            return true;
        }


        private void navi_to_notouch() {
            Intent i = new Intent();
            i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
            i.putExtra("className", NoTouchFragment.class.getName());
            startActivity(i);
        }

        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            return true;
        }
    }

}