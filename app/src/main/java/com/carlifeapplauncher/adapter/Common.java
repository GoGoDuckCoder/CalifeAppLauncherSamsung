package com.carlifeapplauncher.adapter;

import android.accessibilityservice.AccessibilityService;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Outline;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.preference.PreferenceManager;

public class Common {

    public static ArrayList<AppInfo> getAllApps(Context context) {

        try {
            Toast.makeText(context, "加载应用列表中，请稍后！", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
        ArrayList<AppInfo> all_apps = new ArrayList<AppInfo>();
        ArrayList<AppInfo> chinese = new ArrayList<AppInfo>();
        ArrayList<AppInfo> nonchinese = new ArrayList<AppInfo>();

        PackageManager pm = context.getPackageManager();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(i, PackageManager.MATCH_ALL);
        for (ResolveInfo ri : allApps) {
            AppInfo app = new AppInfo();
            app.label = ri.loadLabel(pm).toString();
            app.packageName = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(pm);
            String label = ri.loadLabel(pm).toString();
            if (String.valueOf(label.charAt(0)).matches("[\u4e00-\u9fa5]")) {
                chinese.add(app);
            } else {
                nonchinese.add(app);
            }
        }
        chinese.sort(new AppInfo());
        nonchinese.sort(new AppInfo());
        all_apps.addAll(nonchinese);
        all_apps.addAll(chinese);
        return all_apps;
    }

    public static AppInfo loadAppInfo(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        AppInfo app = new AppInfo();
        try {
            ApplicationInfo temp_info = pm.getApplicationInfo(pkg, 0);
            app.label = temp_info.loadLabel(pm);
            app.packageName = temp_info.packageName;
            app.icon = temp_info.loadIcon(pm);
        } catch (Exception e) {
        }
        return app;
    }

    public static ArrayList<String> get_favorite_list(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String set = sp.getString("favorite_apps", "[]");
        ArrayList<String> list = new ArrayList<>(new Gson().fromJson(set, new TypeToken<ArrayList<String>>() {
        }.getType()));
        ArrayList<String> result = new ArrayList<>();
        for (String pkg : list) {
            if (isInstalled(context, pkg)) {
                result.add(pkg);
            } else {
                remove_from_favorite_list_new(context, pkg);
            }
        }
        return result;
    }


    public static void switchLocation(Context context, int targetA, int targetB) {
        ArrayList<String> list = get_favorite_list(context);
        if (list.size() > targetA && list.size() > targetB && list.size() > 2) {
            String a = list.get(targetA);
            list.set(targetA, list.get(targetB));
            list.set(targetB, a);

            SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
            ed.putString("favorite_apps", new Gson().toJson(list));
            ed.apply();
        }

    }

    public static ArrayList<String> get_adapt_list(Context context) {
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> res = new ArrayList<String>();
        temp.addAll(get_favorite_list(context));
        temp.add( PreferenceManager.getDefaultSharedPreferences(context).getString("godmode_slot_1", "false"));
        temp.add( PreferenceManager.getDefaultSharedPreferences(context).getString("godmode_slot_2", "false"));
        temp.add( PreferenceManager.getDefaultSharedPreferences(context).getString("godmode_slot_3", "false"));
        temp.add( PreferenceManager.getDefaultSharedPreferences(context).getString("godmode_slot_4", "false"));
        temp.add( PreferenceManager.getDefaultSharedPreferences(context).getString("godmode_slot_5", "false"));

        for (String pkg : temp) {
            if (isInstalled(context, pkg)&&!res.contains(pkg)) {
                res.add(pkg);
            }
        }
        return res;
    }

    public static boolean isInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List installedList = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        Iterator iterator = installedList.iterator();
        PackageInfo info;
        String name;
        while (iterator.hasNext()) {
            info = (PackageInfo) iterator.next();
            name = info.packageName;
            if (name.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void add_to_favorite_list_new(Context context, String pkgname) {
        ArrayList<String> list = get_favorite_list(context);
        if (!list.contains(pkgname)) {
            list.add(pkgname);
            SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
            ed.putString("favorite_apps", new Gson().toJson(list));
            ed.apply();
        }
    }

    public static void remove_from_favorite_list_new(Context context, String pkgname) {

        ArrayList<String> list = get_favorite_list(context);
        if (list.contains(pkgname)) {
            list.remove(pkgname);
            SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
            ed.putString("favorite_apps", new Gson().toJson(list));
            ed.apply();
        }
    }

//    public boolean add_to_favorite_list(String pkgname) {
//        ArrayList<String> temp = new ArrayList<String>();
//        //read from storage
//        try {
//            ObjectInputStream oss = new ObjectInputStream(context.openFileInput("FullAppList"));
//            temp.addAll((ArrayList<String>) oss.readObject());
//            oss.close();
//        } catch (Exception e) {
//
//        }
//        //do
//        if (!temp.contains(pkgname)) {
//            temp.add(pkgname);
//            try {
//                ObjectOutputStream oss = new ObjectOutputStream(context.openFileOutput("FullAppList", context.MODE_PRIVATE));
//                oss.writeObject(temp);
//                oss.close();
//            } catch (Exception e) {
//                return false;
//            }
//        }
//        //write
//        return true;
//    }

//    public boolean remove_from_favorite_list(String pkgname) {
//        ArrayList<String> temp = new ArrayList<String>();
//        //read
//        try {
//            ObjectInputStream oss = new ObjectInputStream(context.openFileInput("FullAppList"));
//            temp.addAll((ArrayList<String>) oss.readObject());
//            oss.close();
//        } catch (Exception e) {
//
//        }
//        //do
//        if (temp.contains(pkgname)) {
//            temp.remove(pkgname);
//            try {
//                ObjectOutputStream oss = new ObjectOutputStream(context.openFileOutput("FullAppList", context.MODE_PRIVATE));
//                oss.writeObject(temp);
//                oss.close();
//            } catch (Exception e) {
//                return false;
//            }
//        }
//        //write
//        return true;
//    }

    public static boolean checkAccessibilityPermission(Context context, Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + clazz.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void requestAccessibilityPermission(Context context) {
        Toast.makeText(context, "请在‘已安装应用’中打开无障碍权限", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Boolean checkFloatPermission(Context context) {
        AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (appOpsMgr == null)
            return false;
        int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                .getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
    }

    public static void requestFloatPermission(Context context) {
        Toast.makeText(context, "请打开悬浮窗权限", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }


    public static String getVersionName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List installedList = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        Iterator iterator = installedList.iterator();
        PackageInfo info;
        String name;
        while (iterator.hasNext()) {
            info = (PackageInfo) iterator.next();
            name = info.packageName;
            if (name.equals(packageName)) {

                return info.versionName;
            }
        }
        return null;
    }

    public static String getCarlinkVersionName(Context context) {
        return getVersionName(context, "com.samsung.android.carlink");
    }

    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        return isIgnoring;
    }

    public static void requestIgnoreBatteryOptimizations(Context context) {
        try {
            Toast.makeText(context, "为保证插件联机时后台运行!不使用则无后台。请放心开启！", Toast.LENGTH_LONG).show();
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                }
            }, 1000);
        } catch (Exception e) {
        }
    }


    public static void setBgRadiusWithCutOut(View layoutContent, int cutout, int radius) {

        //设置圆角大小
        layoutContent.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                //设置矩形
                outline.setRoundRect(cutout, cutout, view.getWidth() - cutout, view.getHeight() - cutout, radius);
                // 可以指定圆形，矩形，圆角矩形，path
                //outline.setOval(0, 0, view.getWidth(), view.getHeight()
            }
        });
        //设置阴影
        //layoutContent.setElevation(10);
        //设置圆角裁切
        layoutContent.setClipToOutline(true);

    }

    public static void setBgRadius(View layoutContent, int radius) {
        //设置圆角大小
        layoutContent.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                //设置矩形
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
                // 可以指定圆形，矩形，圆角矩形，path
                //outline.setOval(0, 0, view.getWidth(), view.getHeight()
            }
        });
        //设置阴影
        //layoutContent.setElevation(10);
        //设置圆角裁切
        layoutContent.setClipToOutline(true);

    }

    public static void setBgRadiusOval(View layoutContent) {
        //设置圆角大小
        layoutContent.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                //设置矩形
//                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
                // 可以指定圆形，矩形，圆角矩形，path
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
        //设置阴影
        //layoutContent.setElevation(10);
        //设置圆角裁切
        layoutContent.setClipToOutline(true);

    }


    public static void setMargin(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static void setup_rlf_mode_from_linerlayout(LinearLayout v) {
        LinearLayout ll = v;
        ArrayList<View> views = new ArrayList();
        for (int x = 0; x < ll.getChildCount(); x++) {
            views.add(ll.getChildAt(x));
        }
        ll.removeAllViews();
        for (int x = views.size() - 1; x >= 0; x--) {
            ll.addView(views.get(x));
        }
    }
}
