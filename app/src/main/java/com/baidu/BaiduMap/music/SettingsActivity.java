package com.baidu.BaiduMap.music;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.Toast;

import com.baidu.BaiduMap.music.carlifeapplauncher.MyAccessibilityService;
import com.baidu.BaiduMap.music.carlifeapplauncher.NotificationListener;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.AppInfo;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.BitmapOperator;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.Common;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.CopyFileByUri;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FakeStartFragment;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FavoFragment;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FlavorMode;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NightMode;
import com.baidu.BaiduMap.music.carlifeapplauncher.phone.phone;
import com.baidu.BaiduMap.music.carlifeapplauncher.widget.LauncherAppWidgetHost;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    private static String TAG = "Setting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NightMode.setCustomNightModeSetting(this);
        setContentView(R.layout.settings_activity);
    }


    @Override
    protected void onDestroy() {

        Toast.makeText(this, "请关闭助手！重新启动车机界面生效！", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    public static class SettingsAdaptiveModeFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_adaptivemode_preferences, rootKey);

        }

    }
    public static class SettingsMouseFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.root_mouse_preferences, rootKey);
            Preference a = findPreference("xc_mouse_size");
            a.setOnPreferenceChangeListener(this);
            Preference b = findPreference("xc_mouse_speed");
            b.setOnPreferenceChangeListener(this);
            Preference c = findPreference("xc_mouse_timeout");
            c.setOnPreferenceChangeListener(this);
            Preference d = findPreference("xc_mouse");
            d.setOnPreferenceChangeListener(this);

        }

        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case "xc_mouse":
                    if ((Boolean) newValue) {
                        if(!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("ass_keeper",false))
                        {
                            if (!Common.checkAccessibilityPermission(getContext(), MyAccessibilityService.class)) {
                                Common.requestAccessibilityPermission(getContext());
                                return false;
                            }
                        }
                        if(Common.getFlavor(getContext())== FlavorMode.zs)
                        {
                            if (!Common.checkAccessibilityPermission(getContext(), MyAccessibilityService.class)) {
                                Common.requestAccessibilityPermission(getContext());
                                return false;
                            }
                        }
                    }
                    return true;
                case "xc_mouse_size":
                case "xc_mouse_timeout":
                    if (is_numeric((String) newValue, 1, 999)) {
                        return true;
                    }
                case "xc_mouse_speed":
                    if (is_numeric((String) newValue, 1, 10)) {
                        return true;
                    }
            }
            return false;
        }
    }
    public static class SettingsASSFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.root_ass_patch, rootKey);
            SwitchPreference ass_keeper = findPreference("ass_keeper");

            Preference ass = findPreference("ass_sum");
            ass.setSummary("adb的直接使用以下adb代码即可: adb shell pm grant "+getContext().getPackageName()+" android.permission.WRITE_SECURE_SETTINGS");


            ass_keeper.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    if ((boolean) newValue) {
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_SECURE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(requireContext(), "请参照说明授权后，方可开启", Toast.LENGTH_LONG).show();
                            if (ass_keeper.isChecked()) {
                                ass_keeper.setChecked(false);
                            }
                            return false;
                        } else {
                            ass_keeper.setChecked(true);
                            Toast.makeText(getContext(), "enabled", Toast.LENGTH_LONG).show();
                            return true;
                        }
                    }
                    return true;
                }
            });
        }
    }

    public static class SettingsEXPFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_exp_preferences, rootKey);
            SwitchPreference exp_autolockbyphone = findPreference("exp_autolockbyphone");
//


            SwitchPreference exp_autolock = findPreference("exp_autolock");
            exp_autolock.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    if ((Boolean) newValue) {

                        if (!Common.checkAccessibilityPermission(getContext(), MyAccessibilityService.class)) {
                            Common.requestAccessibilityPermission(getContext());
                            return false;
                        }
                        exp_autolockbyphone.setChecked(false);

                    }
                    return true;
                }
            });

            exp_autolockbyphone.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    if ((Boolean) newValue) {

                        if (!Common.checkAccessibilityPermission(getContext(), MyAccessibilityService.class)) {
                            Common.requestAccessibilityPermission(getContext());
                            return false;
                        }
                        if (!phone.ensurePermission(getContext())) {
                            return false;
                        }
                        exp_autolock.setChecked(false);
                    }
                    return true;

                }
            });

            Preference phonenumber = findPreference("exp_autolockbyphone_number");
            phonenumber.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    if (is_numeric((String) newValue)) {
                        return true;
                    }
                    return false;
                }
            });
        }

    }

    public static class SettingsUIFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
        private SwitchPreference pref_cus_background;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_ui_preferences, rootKey);

            Preference layoutll = findPreference("layoutils");
            layoutll.setOnPreferenceClickListener(this);

            pref_cus_background = findPreference("cusbackground");
            pref_cus_background.setOnPreferenceChangeListener(this);

            Preference p1 = findPreference("cardopacity");
            p1.setOnPreferenceChangeListener(this);
            Preference p2 = findPreference("radius");
            p2.setOnPreferenceChangeListener(this);
            Preference p3 = findPreference("margin");
            p3.setOnPreferenceChangeListener(this);

            Preference p4 = findPreference("areaA");
            p4.setOnPreferenceChangeListener(this);
            Preference p5 = findPreference("areaB");
            p5.setOnPreferenceChangeListener(this);
            Preference p6 = findPreference("areaC");
            p6.setOnPreferenceChangeListener(this);

            Preference p7 = findPreference("weightA");
            p7.setOnPreferenceChangeListener(this);
            Preference p8 = findPreference("weightRight");
            p8.setOnPreferenceChangeListener(this);
            Preference p9 = findPreference("weightB");
            p9.setOnPreferenceChangeListener(this);
            Preference p10 = findPreference("weightC");
            p10.setOnPreferenceChangeListener(this);
        }

        @Override
        public boolean onPreferenceClick(@NonNull Preference preference) {
            switch (preference.getKey()) {
                case "layoutils":
                    Toast.makeText(getContext(), "？？？你在干啥！！！", Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case 31415926:
                    if (resultCode == RESULT_OK && data != null) {
//                        Intent launchIntent = new Intent("samsung.intent.action.carlink.kit");
//                        launchIntent.setComponent(new ComponentName("com.sec.android.gallery3d", "com.samsung.android.gallery.app.activity.external.GalleryExternalActivity"));
//                        launchIntent.setDataAndType(data.getData(), "image/*");
//                        launchIntent.setAction("com.android.camera.action.CROP");
//                        launchIntent.putExtra("crop", "true");
////                        launchIntent.putExtra("aspectX", 1);
//////                        launchIntent.putExtra("aspectY", 1);
////                        launchIntent.putExtra("outputX", 150);
////                        launchIntent.putExtra("outputY", 150);
//                        launchIntent.putExtra("return-data", true);
//                        startActivityForResult(launchIntent, 3141592);
                        boolean result = CopyFileByUri.getPath(getContext(), data.getData());
                        if (result) {
                            Toast.makeText(getContext(), "背景设置成功", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    pref_cus_background.setChecked(false);
                    Toast.makeText(getContext(), "背景设置失败", Toast.LENGTH_LONG).show();
                    break;
                case 3141592:
                    if (resultCode == RESULT_OK && data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap photo = extras.getParcelable("data");

                        if (BitmapOperator.saveBackground(getContext(), photo)) {
                            Toast.makeText(getContext(), "背景设置成功", Toast.LENGTH_LONG).show();
                            return;
                        }

                    }
                    pref_cus_background.setChecked(false);
                    Toast.makeText(getContext(), "背景设置失败，请重新尝试", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case "cusbackground":
                    if ((Boolean) newValue) {
//                        mGetContent.launch("image/png");
                        Intent launchIntent = new Intent("samsung.intent.action.carlink.kit");
                        launchIntent.setComponent(new ComponentName("com.sec.android.gallery3d", "com.samsung.android.gallery.app.activity.external.GalleryExternalActivity"));
                        launchIntent.setType("image/*");
                        launchIntent.setAction("android.intent.action.GET_CONTENT");
                        startActivityForResult(launchIntent, 31415926);
                    }
                    return true;
                case "cardopacity":
                    if (is_numeric((String) newValue, 0, 100)) {
                        return true;
                    }
                    break;
                case "radius":
                case "margin":
                    if (is_numeric((String) newValue, 0, 999999)) {
                        return true;
                    }
                    break;
                case "areaA":
                    return change("areaA", (String) newValue);
                case "areaB":
                    return change("areaB", (String) newValue);
                case "areaC":
                    return change("areaC", (String) newValue);
                case "weightA":
                case "weightRight":
                case "weightB":
                case "weightC":
                    if (is_numeric((String) newValue, 1, 9)) {
                        return true;
                    }
                    break;
            }
            return false;
        }

        private boolean change(String location, String plugin) {
            boolean change = false;

            //check if able to change
            if (plugin.equals("widget") || plugin.equals("false")) {
                change = true;
            }

            if (plugin.equals("app") || plugin.equals("music") || plugin.equals("msgbox")) {
                if (!PreferenceManager.getDefaultSharedPreferences(getContext()).getString("areaA", "false").equals(plugin)
                        && !PreferenceManager.getDefaultSharedPreferences(getContext()).getString("areaB", "false").equals(plugin)
                        && !PreferenceManager.getDefaultSharedPreferences(getContext()).getString("areaC", "false").equals(plugin)) {
                    change = true;
                }
            }


            //check with old and new value
            String oldvalue = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(location, "false");
            if (oldvalue.equals("widget") && !plugin.equals("widget") && change) {
                //remove id
                try {
                    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
                    try {
                        ObjectInputStream oss = new ObjectInputStream(getContext().openFileInput("WidgetConfig"));
                        map.putAll((HashMap<Integer, Integer>) oss.readObject());
                        oss.close();
                    } catch (Exception e) {

                    }
                    int id = -1;
                    switch (location) {
                        case "areaA":
                            id = R.id.containerA;
                            break;
                        case "areaB":
                            id = R.id.containerB;
                            break;
                        case "areaC":
                            id = R.id.containerC;
                            break;
                    }
                    LauncherAppWidgetHost appWidgetHost = new LauncherAppWidgetHost(getContext(), MainActivity.APPWIDGET_HOST_ID);
                    appWidgetHost.deleteAppWidgetId(map.get(id));
                } catch (Exception e) {

                }
            }

            if (oldvalue.equals("music") && change) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                editor.putBoolean("music_area_switch", false);
                editor.apply();
            }

            //new staff check permission
            if (plugin.equals("music") && change) {
                if (!NotificationListener.isEnabled(getContext())) {
                    startActivity(new Intent(
                            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    change = false;
                } else {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                    editor.putBoolean("music_area_switch", true);
                    editor.apply();
                    change = true;
                }
            }
            if (plugin.equals("msgbox") && change) {
                if (!NotificationListener.isEnabled(getContext())) {
                    startActivity(new Intent(
                            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    change = false;
                } else {
                    Toast.makeText(getContext(), "请在通知助手中设置白名单！", Toast.LENGTH_LONG).show();
                }
            }

            if (!change) {
                Toast.makeText(getContext(), "请取消其他区域该插件之后或开启相应权限后，再重试！", Toast.LENGTH_LONG).show();
            }
            return change;
        }

//        private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
//                new ActivityResultCallback<Uri>() {
//                    @Override
//                    public void onActivityResult(Uri uri) {
//                        if (uri != null) {
//                            String result = CopyFileByUri.getPath(getContext(), uri);
//                            if (result != null) {
//                                Toast.makeText(getContext(), "已选择并保存到:" + result, Toast.LENGTH_LONG).show();
//                                return;
//                            }
//                        }
//                        pref_cus_background.setChecked(false);
//                        Toast.makeText(getContext(), "未选择/选择出错，请重新尝试", Toast.LENGTH_LONG).show();
//                    }
//                });
    }

    public static class SettingsAppDrawerFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_apps_preferences, rootKey);

            Preference p1 = findPreference("launcher_col");
            p1.setOnPreferenceChangeListener(this);
            Preference p2 = findPreference("launcher_icon_size");
            p2.setOnPreferenceChangeListener(this);
            Preference p3 = findPreference("launcher_font_size");
            p3.setOnPreferenceChangeListener(this);
            Preference p4 = findPreference("launcher_addapp");
            p4.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    Intent i = new Intent();
                    i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                    i.putExtra("className", FavoFragment.class.getName());
                    startActivity(i);
                    return true;
                }
            });
        }

        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case "launcher_col":
                case "launcher_icon_size":
                case "launcher_font_size":
                    if (is_numeric((String) newValue, 1, 999999)) {
                        return true;
                    }
                    break;
            }
            return false;
        }
    }

    public static class SettingsMusicFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_music_preferences, rootKey);

            ListPreference lock_music_player_pkg = findPreference("lock_music_player_pkg");
            ArrayList<SpannableString> entries = new ArrayList<SpannableString>();
            ArrayList<String> values = new ArrayList<String>();
            ArrayList<AppInfo> all_app = Common.getAllApps(requireContext());
            for (AppInfo app : all_app) {
                entries.add(createEntrie(requireContext(), app.label.toString(), app.icon));
                values.add(app.packageName.toString());
            }
            entries.add(0, new SpannableString("关闭"));
            values.add(0, "false");

            if (!Common.isInstalled(getContext(), lock_music_player_pkg.getValue())) {
                lock_music_player_pkg.setValue("false");
            }

            lock_music_player_pkg.setEntries(entries.toArray(new SpannableString[entries.size()]));
            lock_music_player_pkg.setEntryValues(values.toArray(new String[values.size()]));

            Preference p3 = findPreference("music_mirror");
            p3.setOnPreferenceChangeListener(this);

            Preference p4 = findPreference("album_musk_alpha");
            p4.setOnPreferenceChangeListener(this);

            Preference p5 = findPreference("music_area_switch");
            p5.setOnPreferenceChangeListener(this);
        }


        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case "music_area_switch":
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                    if ((boolean) newValue) {
                        if (!NotificationListener.isEnabled(getContext())) {
                            Toast.makeText(getContext(), "请同意权限后重新开启开关", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(
                                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                            return false;
                        }
                        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getString("areaA", "false").equals("false")) {
                            editor.putString("areaA", "music");
                            editor.apply();
                            Toast.makeText(getContext(), "已添加音乐控制器", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getString("areaB", "false").equals("false")) {
                            editor.putString("areaB", "music");
                            editor.apply();
                            Toast.makeText(getContext(), "已添加音乐控制器", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getString("areaC", "false").equals("false")) {
                            editor.putString("areaC", "music");
                            editor.apply();
                            Toast.makeText(getContext(), "已添加音乐控制器", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        Toast.makeText(getContext(), "已添加音乐控制器或无可用位置。", Toast.LENGTH_LONG).show();
                        return false;
                    } else {
                        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getString("areaA", "false").equals("music")) {
                            editor.putString("areaA", "false");
                            editor.apply();
                            Toast.makeText(getContext(), "已移除音乐控制器", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getString("areaB", "false").equals("music")) {
                            editor.putString("areaB", "false");
                            editor.apply();
                            Toast.makeText(getContext(), "已移除音乐控制器", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getString("areaC", "false").equals("music")) {
                            editor.putString("areaC", "false");
                            editor.apply();
                            Toast.makeText(getContext(), "已移除音乐控制器", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        return true;
                    }
                case "music_mirror":
                    if ((boolean) newValue == true && !NotificationListener.isEnabled(getContext())) {
                        Toast.makeText(getContext(), "请同意权限后重新开启开关", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(
                                "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                        return false;
                    } else {
                        return true;
                    }
                case "music_mirror_autostart":
                    if ((boolean) newValue == true) {
                        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("exp", false)) {
                            return false;
                        } else {
                            if (isIgnoringBatteryOptimizations(getContext())) {
                                return true;
                            } else {
                                requestIgnoreBatteryOptimizations(getContext());
                                return false;
                            }
                        }

                    } else {
                        return true;
                    }
                case "album_musk_alpha":
                    if (is_numeric((String) newValue, 0, 255)) {
                        return true;
                    }
                    return false;
            }
            return false;
        }
    }

    public static class SettingsTaFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
//        private SwitchPreference pref_touchassistant;
        private SwitchPreference xc_ta;
        private SwitchPreference pref_touchassistant_auto;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_floatmenu_preferences, rootKey);

//            pref_touchassistant = findPreference("touchassistant");
//            pref_touchassistant.setOnPreferenceChangeListener(this);
            xc_ta = findPreference("xc_ta");
            xc_ta.setOnPreferenceChangeListener(this);

            pref_touchassistant_auto = findPreference("touchassistant_auto");
            pref_touchassistant_auto.setOnPreferenceChangeListener(this);

            Preference pref_touchassistant1 = findPreference("ta_dot_autohide_time");
            pref_touchassistant1.setOnPreferenceChangeListener(this);
            Preference pref_touchassistant2 = findPreference("ta_icon_size");
            pref_touchassistant2.setOnPreferenceChangeListener(this);
            Preference pref_touchassistant3 = findPreference("ta_opacity");
            pref_touchassistant3.setOnPreferenceChangeListener(this);
//            Preference pref_touchassistant4 = findPreference("ta_x");
//            pref_touchassistant4.setOnPreferenceChangeListener(this);
//            Preference pref_touchassistant5 = findPreference("ta_y");
//            pref_touchassistant5.setOnPreferenceChangeListener(this);
            Preference pref_touchassistant6 = findPreference("ta_drawer_autohide_time");
            pref_touchassistant6.setOnPreferenceChangeListener(this);
            Preference pref_touchassistant7 = findPreference("ta_rgb");
            pref_touchassistant7.setOnPreferenceChangeListener(this);
//            Preference pref_touchassistant8 = findPreference("ta_radius");
//            pref_touchassistant8.setOnPreferenceChangeListener(this);

            ListPreference pref_jump_lp = findPreference("ta_favo_app");
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


        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case "xc_ta":
                    if ((boolean) newValue) {
                        if (!Common.checkFloatPermission(requireContext())) {
                            Common.requestFloatPermission(requireContext());
                            return false;
                        }
                        if(Common.getFlavor(getContext())== FlavorMode.zs)
                        {
                            if (!Common.checkAccessibilityPermission(getContext(), MyAccessibilityService.class)) {
                                Common.requestAccessibilityPermission(getContext());
                                return false;
                            }
                        }
                    }
                    return true;
                case "touchassistant":
                    if ((boolean) newValue) {
                        if (!Common.checkFloatPermission(requireContext())) {
                            Common.requestFloatPermission(requireContext());
                            return false;
                        }
                        if (pref_touchassistant_auto.isChecked()) {
                            pref_touchassistant_auto.setChecked(false);
                        }
                    }
                    return true;
                case "touchassistant_auto":
                    if ((boolean) newValue) {
                        if (!Common.checkFloatPermission(requireContext())) {
                            Common.requestFloatPermission(requireContext());
                            return false;
                        }
                        if (!Common.checkAccessibilityPermission(requireContext(), MyAccessibilityService.class)) {
                            Common.requestAccessibilityPermission(requireContext());
                            return false;
                        }
//                        if (pref_touchassistant.isChecked()) {
//                            pref_touchassistant.setChecked(false);
//                        }
                    }
                    return true;
                case "ta_dot_autohide_time":
                case "ta_icon_size":
                    if (is_numeric((String) newValue, 0, 999999)) {
                        return true;
                    }
                case "ta_opacity":
//                case "ta_x":
//                case "ta_y":
                case "ta_drawer_autohide_time":
                    if (is_numeric((String) newValue, 1, 999999)) {
                        return true;
                    }
                    break;
                case "ta_rgb":
                    String rgb_string = newValue.toString();
                    String[] rgb_array = rgb_string.split("#");
                    if (rgb_array.length != 3) {
                        Toast.makeText(getContext(), "请按照正确的格式输入背景色", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    for (int i = 0; i < rgb_array.length; i++) {
                        try {
                            int digit = Integer.parseInt(rgb_array[i]);
                            if (digit > 255 || digit < 0) {
                                Toast.makeText(getContext(), "请按照正确的格式输入背景色", Toast.LENGTH_LONG).show();
                                return false;
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "请按照正确的格式输入背景色", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }
                    return true;
            }
            return false;
        }
    }

    public static class SettingsBootFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.root_boot_preferences, rootKey);

            Preference configure = findPreference("fake_start_configure");
            configure.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    Intent i = new Intent();
                    i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                    i.putExtra("className", FakeStartFragment.class.getName());
                    startActivity(i);
                    return true;
                }
            });
            SwitchPreference exp_autostart = findPreference("exp_autostart");
            exp_autostart.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                            if ((Boolean) newValue) {
                                if (!Common.checkAccessibilityPermission(getContext(), MyAccessibilityService.class)) {
                                    Common.requestAccessibilityPermission(getContext());
                                    return false;
                                }
                            }
                            return true;
                        }
                    }
            );

            ListPreference pref_jump_lp = findPreference("exp_package");

            String pkg = pref_jump_lp.getValue();

            if (!Common.isInstalled(requireContext(), pkg)) {
                pref_jump_lp.setValue("false");
            }

            ArrayList<SpannableString> entries = new ArrayList<SpannableString>();
            ArrayList<String> values = new ArrayList<String>();
            ArrayList<AppInfo> all_app = Common.getAllApps(requireContext());
            for (AppInfo app : all_app) {
//                if (!app.packageName.toString().equals("com.banqu.samsung.music")) {
                entries.add(createEntrie(requireContext(), app.label.toString(), app.icon));
                values.add(app.packageName.toString());
//                }
            }
            entries.add(0, new SpannableString("关闭"));
            values.add(0, "false");

            pref_jump_lp.setEntries(entries.toArray(new SpannableString[entries.size()]));
            pref_jump_lp.setEntryValues(values.toArray(new String[values.size()]));


            ListPreference pref_jump_lp_real = findPreference("jump_pkg_lp");

            String pkg2 = pref_jump_lp_real.getValue();

            if (!Common.isInstalled(requireContext(), pkg2)) {
                pref_jump_lp_real.setValue("false");
            }
            pref_jump_lp_real.setEntries(entries.toArray(new SpannableString[entries.size()]));
            pref_jump_lp_real.setEntryValues(values.toArray(new String[values.size()]));


        }


        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            return false;
        }
    }

    public static class SettingsNotificationFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        SwitchPreference pref_touchassistant;
        SwitchPreference pref_touchassistant_auto;
        SwitchPreference pref_touchassistant_xc;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_notification_preferences, rootKey);

            pref_touchassistant = findPreference("notification_switch");
            pref_touchassistant.setOnPreferenceChangeListener(this);
            pref_touchassistant_auto = findPreference("notification_switch_auto");
            pref_touchassistant_xc = findPreference("xc_notifications");
            pref_touchassistant_xc.setOnPreferenceChangeListener(this);
            pref_touchassistant_auto.setOnPreferenceChangeListener(this);

            Preference pref_touchassistant1 = findPreference("notification_display_seconds");
            pref_touchassistant1.setOnPreferenceChangeListener(this);
            Preference pref_touchassistant2 = findPreference("notification_opacity");
            pref_touchassistant2.setOnPreferenceChangeListener(this);


            ArrayList<SpannableString> entries = new ArrayList<SpannableString>();
            ArrayList<String> values = new ArrayList<String>();
            ArrayList<AppInfo> all_app = Common.getAllApps(requireContext());
            for (AppInfo app : all_app) {
                entries.add(createEntrie(requireContext(), app.label.toString(), app.icon));
                values.add(app.packageName.toString());
            }

            MultiSelectListPreference pref_notification_whitelist = (MultiSelectListPreference) findPreference("notification_whitelist");
            pref_notification_whitelist.setEntries(entries.toArray(new SpannableString[entries.size()]));

            pref_notification_whitelist.setEntryValues(values.toArray(new String[values.size()]));


            Preference pref_touchassistant5 = findPreference("play_ringtone_volume");
            pref_touchassistant5.setOnPreferenceChangeListener(this);
        }


        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case "notification_switch":
                    if ((boolean) newValue) {
                        if (!Common.checkFloatPermission(requireContext())) {
                            Common.requestFloatPermission(requireContext());
                            return false;
                        }
                        if (!NotificationListener.isEnabled(requireContext())) {
                            startActivity(new Intent(
                                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                            return false;
                        }
                        if (pref_touchassistant_auto.isChecked()) {
                            pref_touchassistant_auto.setChecked(false);
                        }
                    }
                    return true;
                case "xc_notifications":
                    if ((boolean) newValue) {
                        if (!Common.checkFloatPermission(requireContext())) {
                            Common.requestFloatPermission(requireContext());
                            return false;
                        }
                        if(Common.getFlavor(getContext())== FlavorMode.zs)
                        {
                            if (!Common.checkAccessibilityPermission(getContext(), MyAccessibilityService.class)) {
                                Common.requestAccessibilityPermission(getContext());
                                return false;
                            }
                        }
                        if (!NotificationListener.isEnabled(requireContext())) {
                            startActivity(new Intent(
                                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                            return false;
                        }
                    }
                    return true;
                case "notification_switch_auto":

                    if ((boolean) newValue) {
                        if (!Common.checkFloatPermission(requireContext())) {
                            Common.requestFloatPermission(requireContext());
                            return false;
                        }
                        if (!NotificationListener.isEnabled(requireContext())) {
                            startActivity(new Intent(
                                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                            return false;
                        }
                        if (!Common.checkAccessibilityPermission(requireContext(), MyAccessibilityService.class)) {
                            Common.requestAccessibilityPermission(requireContext());
                            return false;
                        }
                        if (pref_touchassistant.isChecked()) {
                            pref_touchassistant.setChecked(false);
                        }
                    }
                    return true;
                case "notification_display_seconds":
                case "notification_opacity":
                case "play_ringtone_volume":
                    if (is_numeric((String) newValue, 1, 100)) {
                        return true;
                    }
                    break;
            }
            return false;
        }
    }

    public static class SettingsGodFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        private SwitchPreference god;
        private SwitchPreference god_auto;
        private SwitchPreference god_minimap;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_god_preferences, rootKey);

            god = findPreference("godmode");
            god.setOnPreferenceChangeListener(this);
            god_auto = findPreference("godmode_auto");
            god_auto.setOnPreferenceChangeListener(this);

            god_minimap = findPreference("xc_god");
            god_minimap.setOnPreferenceChangeListener(this);

            Preference god1 = findPreference("godmodeiconsize");
            god1.setOnPreferenceChangeListener(this);

//            Preference god2 = findPreference("godmodephone");
//            god2.setOnPreferenceChangeListener(this);

//            Preference god3 = findPreference("godmodephonenumbertictok");
//            god3.setOnPreferenceChangeListener(this);

            Preference god5 = findPreference("godmodeautohidetime");
            god5.setOnPreferenceChangeListener(this);

            Preference god6 = findPreference("godmode_slot_1");
            god6.setOnPreferenceChangeListener(this);
            Preference god7 = findPreference("godmode_slot_2");
            god7.setOnPreferenceChangeListener(this);
            Preference god8 = findPreference("godmode_slot_3");
            god8.setOnPreferenceChangeListener(this);
            Preference god9 = findPreference("godmode_slot_4");
            god9.setOnPreferenceChangeListener(this);
            Preference god10 = findPreference("godmode_slot_5");
            god10.setOnPreferenceChangeListener(this);
            Preference god11 = findPreference("godmode_opacity");
            god10.setOnPreferenceChangeListener(this);
            Preference god12 = findPreference("godmode_rgb");
            god10.setOnPreferenceChangeListener(this);

//            Preference god11 = findPreference("min_height");
//            god11.setOnPreferenceChangeListener(this);


            setDefault(((ListPreference) god6));
            setDefault(((ListPreference) god7));
            setDefault(((ListPreference) god8));
            setDefault(((ListPreference) god9));
            setDefault(((ListPreference) god10));

            ArrayList<SpannableString> entries = new ArrayList<SpannableString>();
            ArrayList<String> values = new ArrayList<String>();
            ArrayList<AppInfo> all_app = Common.getAllApps(requireContext());
            for (AppInfo app : all_app) {
//                if (!app.packageName.equals("com.banqu.samsung.music")) {
                entries.add(createEntrie(requireContext(), app.label.toString(), app.icon));
                values.add(app.packageName.toString());
//                }
            }
            entries.add(0, new SpannableString("关闭"));
            values.add(0, "false");

            entries.add(1, new SpannableString("助手主页按钮"));
            values.add(1, "home");

            entries.add(2, new SpannableString("返回按钮"));
            values.add(2, "back");

            entries.add(3, new SpannableString("Carlife主页按钮"));
            values.add(3, "back_carlife");

//            entries.add(4, new SpannableString("清理后台按钮"));
//            values.add(4, "clear");

            setData(((ListPreference) god6), entries, values);
            setData(((ListPreference) god7), entries, values);
            setData(((ListPreference) god8), entries, values);
            setData(((ListPreference) god9), entries, values);
            setData(((ListPreference) god10), entries, values);

        }

        private void setDefault(ListPreference lp) {
            if (lp.getValue().equals("home") || lp.getValue().equals("back") || lp.getValue().equals("false") || lp.getValue().equals("back_carlife") || lp.getValue().equals("clear")) {
                return;
            }
            if (!Common.isInstalled(requireContext(), lp.getValue())) {
                lp.setValue("false");
            }
        }

        private void setData(ListPreference lp, ArrayList<SpannableString> entries, ArrayList<String> values) {

            lp.setEntries(entries.toArray(new SpannableString[entries.size()]));
            lp.setEntryValues(values.toArray(new String[values.size()]));

        }


        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case "godmode":
                    if ((boolean) newValue) {
                        if (!Common.checkFloatPermission(requireContext())) {
                            Common.requestFloatPermission(requireContext());
                            return false;
                        }
                        if (god_auto.isChecked()) {
                            god_auto.setChecked(false);
                        }
                    }
                    return true;
                case "godmode_auto":
                    if ((boolean) newValue) {
                        if (!Common.checkFloatPermission(requireContext())) {
                            Common.requestFloatPermission(requireContext());
                            return false;
                        }
                        if (!Common.checkAccessibilityPermission(requireContext(), MyAccessibilityService.class)) {
                            Common.requestAccessibilityPermission(requireContext());
                            return false;
                        }
                        if (god.isChecked()) {
                            god.setChecked(false);
                        }
                    }
                    return true;
                case "godmodeiconsize":
                    if (is_numeric((String) newValue, 0, 999999)) {
                        return true;
                    }
                case "xc_god":
                    if ((boolean) newValue) {
                        if (!Common.checkAccessibilityPermission(getContext(), MyAccessibilityService.class)) {
                            Common.requestAccessibilityPermission(getContext());
                            return false;
                        }
                        if(Common.getFlavor(getContext())== FlavorMode.zs)
                        {
                            if (!Common.checkAccessibilityPermission(getContext(), MyAccessibilityService.class)) {
                                Common.requestAccessibilityPermission(getContext());
                                return false;
                            }
                        }
                        if (god_auto.isChecked()) {
                            god_auto.setChecked(false);
                        }
                    }
                    return true;
                case "godmodeautohidetime":
//                case "min_height":
                    if (is_numeric((String) newValue, 1, 999999)) {
                        return true;
                    }
                    break;
                case "godmodephonenumbertictok":
                case "godmode_opacity":
                    if (is_numeric((String) newValue, 0, 999999)) {
                        return true;
                    }
                    break;
                case "godmode_rgb":
                    String rgb_string = newValue.toString();
                    String[] rgb_array = rgb_string.split("#");
                    if (rgb_array.length != 3) {
                        Toast.makeText(getContext(), "请按照正确的格式输入背景色", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    for (int i = 0; i < rgb_array.length; i++) {
                        try {
                            int digit = Integer.parseInt(rgb_array[i]);
                            if (digit > 255 || digit < 0) {
                                Toast.makeText(getContext(), "请按照正确的格式输入背景色", Toast.LENGTH_LONG).show();
                                return false;
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "请按照正确的格式输入背景色", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }
                    return true;
                case "godmodephone":
                    return phone.ensurePermission(getContext());
                case "godmode_slot_1":
                case "godmode_slot_2":
                case "godmode_slot_3":
                case "godmode_slot_4":
                case "godmode_slot_5":
                    if (((String) newValue).equals("back")) {
                        if (!Common.checkAccessibilityPermission(getContext(), MyAccessibilityService.class)) {
                            Common.requestAccessibilityPermission(getContext());
                            return false;
                        }
                    }
                    return true;
            }
            return false;
        }
    }


    @Override
    public boolean onPreferenceStartFragment(@NonNull PreferenceFragmentCompat caller, @NonNull Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(pref.getKey())
                .commit();
        return true;
    }


    //helper functions
    public static SpannableString createEntrie(Context context, String str, Drawable drawable) {
        String replacedStr = "i";
        final SpannableString spannableString = new SpannableString(replacedStr + "  " + str);
        int size = context.getResources().getDimensionPixelSize(R.dimen.span_icon_size);
        drawable.setBounds(0, 0, size, size);
        CenteredImageSpan span = new CenteredImageSpan(drawable);
        spannableString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private static class CenteredImageSpan extends ImageSpan {
        public CenteredImageSpan(Drawable d) {
            super(d);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text,
                         int start, int end, float x,
                         int top, int y, int bottom, Paint paint) {
            Drawable b = getDrawable();
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            int transY = (y + fm.descent + y + fm.ascent) / 2
                    - b.getBounds().bottom / 2;
            canvas.save();
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }
    }

    public static boolean is_numeric(String str, int start, int end) {
        Log.i("TEST", str);
        Pattern pattern = Pattern.compile("^[0-9]\\d*$");
        if (pattern.matcher(str).matches()) {
            Log.i("TEST", "match");
            int value = Integer.valueOf(str);
            if (value >= start && value <= end) {
                return true;
            }
        }
        return false;
    }

    public static boolean is_numeric(String str) {
        Pattern pattern = Pattern.compile("^[1-9]\\d*$");
        return pattern.matcher(str).matches();
    }


    private static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        return isIgnoring;
    }

    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }

    public void requestIgnoreBatteryOptimizations() {
        try {
            Toast.makeText(this, "即将打开授权窗口：请授予应用后台无限制权限，以保证该功能使用体验。", Toast.LENGTH_LONG).show();
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }, 3000);

        } catch (Exception e) {
        }
    }

    public static void requestIgnoreBatteryOptimizations(Context context) {
        try {
            Toast.makeText(context, "即将打开授权窗口：请授予应用后台无限制权限，以保证该功能使用体验。", Toast.LENGTH_LONG).show();
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                }
            }, 3000);
        } catch (Exception e) {
        }

    }

    public static class SettingsFsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case "xc_fs":
                    if ((boolean) newValue) {
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_SECURE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(requireContext(), "请参照说明授权后，方可开启", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        if(Common.getFlavor(getContext())== FlavorMode.zs)
                        {
                            if (!Common.checkAccessibilityPermission(getContext(), MyAccessibilityService.class)) {
                                Common.requestAccessibilityPermission(getContext());
                                return false;
                            }
                        }
                    }
//                case "fs":
//                    if ((boolean) newValue) {
//                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_SECURE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
//                            Toast.makeText(requireContext(), "请参照说明授权后，方可开启", Toast.LENGTH_LONG).show();
//                            return false;
//                        }
//                        if (fs_auto.isChecked()) {
//                            fs_auto.setChecked(false);
//                        }
//                    }
//                    return true;
//                case "fs_auto":
//                    if ((boolean) newValue) {
//                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_SECURE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
//                            Toast.makeText(requireContext(), "请参照说明授权后，方可开启", Toast.LENGTH_LONG).show();
//                            return false;
//                        }
//                        if (!Common.checkAccessibilityPermission(requireContext(), MyAccessibilityService.class)) {
//                            Common.requestAccessibilityPermission(requireContext());
//                            return false;
//                        }
//                        if (fs.isChecked()) {
//                            fs.setChecked(false);
//                        }
//                    }
                    return true;
            }
            return true;
        }

        private SwitchPreference xc_fs;
        private SwitchPreference fs_auto;
        private SwitchPreference fs;

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.root_fs_preferences, rootKey);
//            fs_auto = findPreference("fs_auto");
//            fs_auto.setOnPreferenceChangeListener(this);
            xc_fs = findPreference("xc_fs");
            xc_fs.setOnPreferenceChangeListener(this);
//            fs = findPreference("fs");
//            fs.setOnPreferenceChangeListener(this);
            Preference fss = findPreference("fs_sum");
            fss.setSummary("adb的直接使用以下adb代码即可: adb shell pm grant "+getContext().getPackageName()+" android.permission.WRITE_SECURE_SETTINGS");

            ArrayList<SpannableString> entries = new ArrayList<SpannableString>();
            ArrayList<String> values = new ArrayList<String>();
            ArrayList<AppInfo> all_app = Common.getAllApps(requireContext());
            for (AppInfo app : all_app) {
                entries.add(createEntrie(requireContext(), app.label.toString(), app.icon));
                values.add(app.packageName.toString());
            }

            MultiSelectListPreference pref_notification_whitelist = (MultiSelectListPreference) findPreference("xc_fs_list");
            pref_notification_whitelist.setEntries(entries.toArray(new SpannableString[entries.size()]));
            pref_notification_whitelist.setEntryValues(values.toArray(new String[values.size()]));

        }
    }
}