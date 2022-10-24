package com.carlifeapplauncher.adapter;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.preference.PreferenceManager;

public class FakeStart {


    private static final String TAG = "FakeStart";

    public static void Start(Context context, String pkgName) {
        Log.i(TAG, "Start: " + pkgName);

        if (pkgName.equals(context.getPackageName())) {
            StartSamsung(context, pkgName);
            return;
        }

        //load config
        Boolean fake_start = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("fake_start", false);
        if (fake_start) {
            Map<String, String> temp = new HashMap<>();
            //start
            try {
                ObjectInputStream oss = new ObjectInputStream(context.openFileInput("FakeStartConfig"));
                temp = (Map<String, String>) oss.readObject();
                oss.close();
            } catch (Exception e) {

            }
            if (temp.containsKey(pkgName)) {
                Log.i(TAG, "Mode: " + temp.get(pkgName));
                switch (temp.get(pkgName)) {
                    case "default":
                        StartDefault(context, pkgName);
                        break;
                    case "vivo":
                        StartVivo(context, pkgName);
                        break;
                    case "samsung":
                        StartSamsung(context, pkgName);
                        break;
                    case "huawei":
                        StartHuawei(context, pkgName);
                        break;
                    case "ucar":
                        StartUcar(context, pkgName);
                        break;
                }
            } else {
                //default mode
//                Toast.makeText(context, "未设置，默认模式启动", Toast.LENGTH_LONG).show();
                StartDefault(context, pkgName);
            }
        } else {
//            Toast.makeText(context, "未开启兼容模式启动", Toast.LENGTH_LONG).show();
            StartDefault(context, pkgName);
        }

    }

    @SuppressLint("WrongConstant")
    private static void StartDefault(Context context, String pkgName) {

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
//            launchIntent.addFlags(0x10104000);
        launchIntent.addFlags(0x10104000);
        context.startActivity(launchIntent);

    }


    @SuppressLint("WrongConstant")
    public static void StartVivo(Context context, String pkgName) {
        Intent i = new Intent("vivo.intent.action.carlink.kit", null);
        i.setPackage(pkgName);
        List<ResolveInfo> allApps = context.getPackageManager().queryIntentActivities(i, 0);
        ResolveInfo info = null;
        for (ResolveInfo ri : allApps) {
            info = ri;
            break;
        }
        if (info != null) {
            Intent launchIntent = new Intent("vivo.intent.action.carlink.kit");
            launchIntent.addFlags(0x10104000);
            launchIntent.putExtra("isVivoCarLinkMode", true);
            ///////////


            ////////////
            launchIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            try {
                context.startActivity(launchIntent);
                // Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // Toast.makeText(context, "启动失败，默认模式启动", Toast.LENGTH_LONG).show();
                StartDefault(context, pkgName);
            }
        } else {
//            Toast.makeText(context, "无适配，默认模式启动", Toast.LENGTH_LONG).show();
            StartDefault(context, pkgName);
        }
    }

    @SuppressLint("WrongConstant")
    public static void StartSamsung(Context context, String pkgName) {
        Intent i = new Intent("samsung.intent.action.carlink.kit", null);
        i.setPackage(pkgName);
        List<ResolveInfo> allApps = context.getPackageManager().queryIntentActivities(i, 0);
        ResolveInfo info = null;
        for (ResolveInfo ri : allApps) {
            info = ri;
            break;
        }
        if (info != null) {
            Intent launchIntent = new Intent("samsung.intent.action.carlink.kit");
            launchIntent.addFlags(0x10104000);
            launchIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            try {
                context.startActivity(launchIntent);
                //  Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                //Toast.makeText(context, "启动失败，默认模式启动", Toast.LENGTH_LONG).show();
                StartDefault(context, pkgName);
            }
        } else {
//            Toast.makeText(context, "无适配，默认模式启动", Toast.LENGTH_LONG).show();
            StartDefault(context, pkgName);
        }
    }

    @SuppressLint("WrongConstant")
    public static void StartHuawei(Context context, String pkgName) {

        if (pkgName.equals("com.kugou.android")) {
            Intent launchIntent = new Intent();
            launchIntent.setComponent(new ComponentName("com.kugou.android", "com.kugou.android.app.hicar.HiCarSplashActivity"));
            launchIntent.addFlags(0x10104000);
            launchIntent.putExtra("isHiCarMode", true);
            try {
                context.startActivity(launchIntent);
                //  Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(context, "启动失败，默认模式启动", Toast.LENGTH_LONG).show();
//                StartDefault(context, pkgName);
            }
        }
//        if(pkgName.equals("com.autonavi.minimap"))
//        {
//            Intent launchIntent = new Intent("android.intent.action.MAIN");
//            launchIntent.addCategory("android.intent.category.LAUNCHER");
////            launchIntent.setClassName(context.getClass().getName(), "com.autonavi.map.activity.SplashActivity");
//            launchIntent.setComponent(new ComponentName("com.autonavi.minimap", "com.autonavi.map.activity.HicarSplashActivity"));
//            launchIntent.setFlags(0x10104000);
//
//            launchIntent.putExtra("isHiCarMode",true);
////            launchIntent.putExtra("isUcarMode",true);
//
////            launchIntent.putExtra("isVivoCarLinkMode",true);
////            launchIntent.putExtra("screenMode",0);
//            launchIntent.putExtra("displayId",0);
////            launchIntent.putExtra("displayId",context.getDisplay().getDisplayId());
//
////            launchIntent.putExtra("isHiCarMode",true);
//            try {
//                context.startActivity(launchIntent);
//                //  Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
//            } catch (Exception e) {
//                Toast.makeText(context, "启动失败，默认模式启动", Toast.LENGTH_LONG).show();
////                StartDefault(context, pkgName);
//            }
//        }

//        Intent i = new Intent("huawei.intent.action.hicar.MEDIA", null);
//        i.setPackage(pkgName);
//        List<ResolveInfo> allApps = context.getPackageManager().queryIntentActivities(i, 0);
//        ResolveInfo info = null;
//        for (ResolveInfo ri : allApps) {
//            info = ri;
//            break;
//        }
//        if (info != null) {
//            Intent launchIntent = new Intent("huawei.intent.action.hicar.MEDIA");
//            launchIntent.addFlags(0x10104000);
//            launchIntent.putExtra("isHiCarMode",true);
//            launchIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
//            try {
//                context.startActivity(launchIntent);
//                //  Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
//            } catch (Exception e) {
//                //Toast.makeText(context, "启动失败，默认模式启动", Toast.LENGTH_LONG).show();
//                StartDefault(context, pkgName);
//            }
//        } else {
//            //Toast.makeText(context, "无适配，默认模式启动", Toast.LENGTH_LONG).show();
//            StartDefault(context, pkgName);
//        }
    }


    @SuppressLint("WrongConstant")
    public static void StartUcar(Context context, String pkgName) {

        if (pkgName.equals("com.autonavi.minimap")) {
            StartMiniMap(context);
            return;
        }

        Intent i = new Intent("com.ucar.intent.action.UCAR", null);
        i.setPackage(pkgName);
        List<ResolveInfo> allApps = context.getPackageManager().queryIntentActivities(i, 0);
        ResolveInfo info = null;
        for (ResolveInfo ri : allApps) {
            info = ri;
            break;
        }
        if (info != null) {
            Intent launchIntent = new Intent("com.ucar.intent.action.UCAR");
            launchIntent.addFlags(0x10104000);
            launchIntent.putExtra("isUcarMode", true);

//            launchIntent.putExtra("isVivoCarLinkMode",true);
//            launchIntent.putExtra("screenMode",0);
//            launchIntent.putExtra("displayId",context.getDisplay().getDisplayId());

            launchIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            try {
                context.startActivity(launchIntent);
                //  Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                //Toast.makeText(context, "启动失败，默认模式启动", Toast.LENGTH_LONG).show();
                StartDefault(context, pkgName);
            }
        } else {
            //Toast.makeText(context, "无适配，默认模式启动", Toast.LENGTH_LONG).show();
            StartDefault(context, pkgName);
        }
    }

    @SuppressLint("WrongConstant")
    public static void StartMiniMap(Context context) {


        String pkgName = "com.autonavi.minimap";

//        i(context,pkgName);

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.killBackgroundProcesses(pkgName);

        Intent i = new Intent("com.ucar.intent.action.UCAR", null);
        i.setPackage(pkgName);
        List<ResolveInfo> allApps = context.getPackageManager().queryIntentActivities(i, 0);
        ResolveInfo info = null;
        for (ResolveInfo ri : allApps) {
            info = ri;
            break;
        }
        if (info != null) {
            Intent launchIntent = new Intent("com.ucar.intent.action.UCAR");
//            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    |Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    |Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            launchIntent.addFlags(0x10104000);

            launchIntent.putExtra("isUcarMode", true);
//            launchIntent.putExtra("isVivoCarLinkMode",true);
            launchIntent.putExtra("screenMode", 1);
            launchIntent.putExtra("UCarBigMapDisplayId", context.getDisplay().getDisplayId());
            launchIntent.putExtra("UCarSmallMapDisplayId", context.getDisplay().getDisplayId());
//            launchIntent.putExtra("displayId",context.getDisplay().getDisplayId());
            launchIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));

            try {
                context.startActivity(launchIntent);
                //  Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                //Toast.makeText(context, "启动失败，默认模式启动", Toast.LENGTH_LONG).show();
                StartDefault(context, pkgName);
            }
        } else {
            //Toast.makeText(context, "无适配，默认模式启动", Toast.LENGTH_LONG).show();
            StartDefault(context, pkgName);
        }
    }

//    @SuppressLint("WrongConstant")
//    public static void StartMiniMapDeeplink(Context context)
//    {
//        String uri = "amapurihicar://";
//        Uri parse = Uri.parse("samsungcarlink://link/----"+uri);
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        intent.setData(parse);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(0x10104000);
//        Log.i("MyAccessibilityService", "context change: ");
//        context.startActivity(intent);
//    }


    public static void StartSpecial(Context context, String pkgName) {

    }


    public static void i(Context context, String str) {
        Log.d("AppStartUtil", "killProcess:" + str);
        try {
            Class<?> cls = Class.forName("android.app.ActivityManager");
            cls.getMethod("forceStopPackage", String.class).invoke(context.getSystemService(cls), str);
        } catch (Exception e) {
            Log.i("AppStartUtil", "killProcess:", e);
        }
    }


    @SuppressLint("WrongConstant")
    public static void StartUsingDeepLink(Context context, String packagename) {
        if (OpenProvider.isSupported(context)) {

            if (OpenProvider.isConnected(context)) {
                String uri = "banqumusic://tasker/" + packagename;
                Uri parse = Uri.parse("samsungcarlink://link/----" + uri);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(parse);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(0x10104000);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "未连接车机", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "服务版本不支持", Toast.LENGTH_LONG).show();
        }


    }
}

