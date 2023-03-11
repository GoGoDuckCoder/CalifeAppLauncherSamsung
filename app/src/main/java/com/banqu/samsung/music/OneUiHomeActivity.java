package com.banqu.samsung.music;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.banqu.samsung.music.adapter.ActivityManager;
import com.banqu.samsung.music.adapter.MyFragmentDisplayer;
import com.banqu.samsung.music.carlifeapplauncher.MyAccessibilityService;
import com.banqu.samsung.music.carlifeapplauncher.NotificationListener;
import com.banqu.samsung.music.carlifeapplauncher.adapter.AppAnnouncement;
import com.banqu.samsung.music.carlifeapplauncher.adapter.Common;
import com.banqu.samsung.music.carlifeapplauncher.adapter.NightMode;
import com.banqu.samsung.music.databinding.ActivityOneUiHomeBinding;
import com.banqu.samsung.music.ui.donate.DonateFragment;
import com.banqu.samsung.music.ui.group.GroupFragment;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class OneUiHomeActivity extends AppCompatActivity {

    private ActivityOneUiHomeBinding binding;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NightMode.setCustomNightModeSetting(getWindow(), this);
        binding = ActivityOneUiHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //BAIDU STATIC
        StatService.init(getApplicationContext(),"2621824db8","GDJ");
        StatService.setAuthorizedState(getApplicationContext(),true);
        StatService.autoTrace(getApplicationContext());


        ActivityManager.getInstance().add(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new ShortcutsFragment())
                    .commit();
        }

        WindowInsetsController wc = getWindow().getInsetsController();

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        AppAnnouncement.run(this);
        permissionCheck();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().remove(this);
        super.onDestroy();
    }

    public static class ShortcutsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference settings = findPreference("oneui_shortcut_settings");
            settings.setOnPreferenceClickListener(this);

            Preference version = findPreference("layoutils");

            if (Common.isInstalled(requireContext(), "com.samsung.android.carlink")) {
                String versionName = Common.getCarlinkVersionName(getContext());
                version.setTitle("车联服务：" + versionName);
            } else {
                version.setTitle("抱歉，您当前的系统环境不支持三星Carlife定制版与车联助手插件！请检查系统更新，并升级国行最新系统，外版可刷带车联服务的第三方国行移植固件。感谢您的支持");
            }

            Preference guide = findPreference("oneui_shortcut_instruction");
            guide.setOnPreferenceClickListener(this);

            Preference preview = findPreference("oneui_shortcut_preview");
            preview.setOnPreferenceClickListener(this);

            Preference app = findPreference("oneui_shortcut_app");
            app.setOnPreferenceClickListener(this);

            Preference music = findPreference("oneui_shortcut_music");
            music.setOnPreferenceClickListener(this);

            Preference close = findPreference("oneui_shortcut_close");
            close.setOnPreferenceClickListener(this);

            Preference donation = findPreference("oneui_shortcut_donation");
            donation.setOnPreferenceClickListener(this);

            Preference group = findPreference("oneui_shortcut_group");
            group.setOnPreferenceClickListener(this);

            Preference update = findPreference("oneui_shortcut_update");
            update.setOnPreferenceClickListener(this);
            AppAnnouncement.runUpdate(requireContext(), update);
        }

        @Override
        public boolean onPreferenceClick(@NonNull Preference preference) {
            switch (preference.getKey()) {
                case "oneui_shortcut_settings":
                    Intent i = new Intent();
                    i.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                    i.putExtra("className", com.banqu.samsung.music.adapter.fragments.SettingsFragment.class.getName());
                    startActivity(i);
                    break;
                case "oneui_shortcut_instruction":
                    Toast.makeText(requireContext(), "说明加载中", Toast.LENGTH_SHORT).show();
                    AppAnnouncement.runGuide(requireContext());
                    break;
                case "oneui_shortcut_preview":
                    Intent i2 = new Intent();
                    i2.setClass(requireContext(), MainActivity.class);
                    startActivity(i2);
                    break;
                case "oneui_shortcut_app":
                    Intent i3 = new Intent();
                    i3.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                    i3.putExtra("className", SettingsActivity.SettingsAppDrawerFragment.class.getName());
                    startActivity(i3);
                    break;
                case "oneui_shortcut_music":
                    Intent i4 = new Intent();
                    i4.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                    i4.putExtra("className", SettingsActivity.SettingsMusicFragment.class.getName());
                    startActivity(i4);
                    break;
                case "oneui_shortcut_close":
                    ActivityManager.getInstance().clearAll();
                    break;

                case "oneui_shortcut_donation":
                    Intent i5 = new Intent();
                    i5.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                    i5.putExtra("className", DonateFragment.class.getName());
                    startActivity(i5);
                    break;

                case "oneui_shortcut_group":
                    Intent i6 = new Intent();
                    i6.setClassName(requireContext().getPackageName(), MyFragmentDisplayer.class.getName());
                    i6.putExtra("className", GroupFragment.class.getName());
                    startActivity(i6);
                    break;
            }

            return true;
        }

        @Override
        public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
            return true;
        }
    }


    private void permissionCheck() {
        boolean phone_godmode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("godmode", false);
        boolean phone_ta = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("touchassistant", false);
        boolean phone_music = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("music_mirror", false);
        boolean phone_music_area = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("music_area_switch", false);
        boolean phone_noti = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notification_switch", false);
//        boolean phone_fs = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fs", false);
        boolean phone_fs = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fs", false);

        boolean ass_ta = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("touchassistant_auto", false);
        boolean ass_god = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("godmode_auto", false);
        boolean ass_noti = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notification_switch_auto", false);
        boolean ass_boot = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("exp_autostart", false);
        boolean ass_exp_lock = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("exp_autolock", false);

        if (phone_godmode || phone_ta) {
            if (!Common.checkFloatPermission(this)) {
                Common.requestFloatPermission(this);
            }
        }

        if (phone_music ||phone_music_area|| phone_noti) {
            if (!NotificationListener.isEnabled(this)) {
                startActivity(new Intent(
                        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            }
        }

        if (phone_fs) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "请重新开启全屏助手权限", Toast.LENGTH_LONG).show();
            }
        }


        if (ass_ta || ass_god || ass_noti || ass_boot || ass_exp_lock) {
            if (!Common.checkAccessibilityPermission(this, MyAccessibilityService.class)) {
                Common.requestAccessibilityPermission(this);
            }
        }

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FirstRun",true)) {
//             Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle()
//                     .setBigContentTitle("Big picture style notification ~ Expand title")
//                     .setSummaryText("Demo for big picture style notification ! ~ Expand summery")
////                     .bigPicture(BitmapFactory.decodeResource(context.getResources(),R.drawable.welcome3));
            Notification notification = new Notification.Builder(this, "88888888")
                    .setContentTitle("测试通知权限")
                    .setContentText("测试通知权限")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher_round)
//                     .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.id))   //设置大图标
//                     .setStyle(bigPictureStyle)
                    .build();
// 2. 获取系统的通知管理器
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// 3. 创建NotificationChannel(这里传入的channelId要和创建的通知channelId一致，才能为指定通知建立通知渠道)
            NotificationChannel channel = new NotificationChannel("88888888", "测试通知权限", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
// 4. 发送通知
            notificationManager.notify(1123, notification);
        }

//        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
//        editor.putBoolean("FirstStartFlag",false);
//        editor.apply();
    }
}