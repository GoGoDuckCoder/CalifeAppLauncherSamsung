package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.PixelFormat;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.banqu.samsung.music.log.xLog;
import com.banqu.samsung.music.R;
import com.banqu.samsung.music.carlifeapplauncher.MyAccessibilityService;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.preference.PreferenceManager;

public class NavBar {


    private static String TAG = "Navbar";


    private TextView time_View;
    private updateTimeReceiver tiktok;


    //System
    private Context context;
    private WindowManager wm;
    private PackageManager pm;
    public static NavBar navBar;

    public static NavBar getInstance() {
        return navBar;
    }

    //create new navbar only when navBar is null (using getInstance)
    public static NavBar createInstance(Context context) {
        navBar = new NavBar(context);
        return navBar;
    }


    public NavBar(Context context) {
        this.context = context;
        this.wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.pm = context.getPackageManager();
    }

    //AutoHide
    private boolean autohide;
    private int autohide_time;
    private Timer autohide_timer;

    //UI
    private LinearLayout navbar_wrapper;
    private View navbar_view;
    public boolean shown = false;
    private int radius;
    private int iconsize;

    @SuppressLint("ClickableViewAccessibility")
    public void onStart() {

        //double check setting and permission
        if (!Common.checkFloatPermission(context)) {
            Common.requestFloatPermission(context);
            onDestroy();
        }


        //自动隐藏开关及时间
        this.autohide = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("godmodeautohide", false);
        this.autohide_time = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("godmodeautohidetime", "3"));

        //Timer
        tiktok = new updateTimeReceiver();
        context.registerReceiver(tiktok, new IntentFilter(Intent.ACTION_TIME_TICK));

        //UI
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
        WindowInsets windowInsets = windowMetrics.getWindowInsets();
        Insets insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());


        this.radius = (int) (insets.left * 0.35) ;

        this.iconsize = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("godmodeiconsize", "0"));
        if(iconsize==0) {
            this.iconsize = (int) (insets.left * 0.55);
        }

        navbar_view = View.inflate(context, R.layout.godmod, null);
//        navbar_view.setBackgroundColor(Color.argb(255, 123, 45, 155));
        int opacity = (int) (Double.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("godmode_opacity", "100")) / 100 * 255);
        String rgb_string = PreferenceManager.getDefaultSharedPreferences(context).getString("godmode_rgb", "55#55#55");
        String[] rgb_array = rgb_string.split("#");
        int trans = Color.argb(opacity, Integer.parseInt(rgb_array[0]), Integer.parseInt(rgb_array[1]), Integer.parseInt(rgb_array[2]));
        navbar_view.setBackgroundColor(trans);
//        navbar_view.setBackgroundColor(Color.argb(255, 55, 55, 55));
        navbar_view.setFocusable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(insets.left, WindowManager.LayoutParams.MATCH_PARENT,
                2038,
                262184,
                PixelFormat.TRANSLUCENT);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT,
//                2038,
//                262184,
//                PixelFormat.TRANSLUCENT);
//        lp.flags = WindowManager.LayoutParams.FLAG;
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.setFitInsetsTypes(5);



        lp.windowAnimations = R.style.anim_notification;
        lp.token = null;
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        lp.setTitle("carlife navigation");

        lp.x = 0;
        lp.y = 0;


        navbar_wrapper = navbar_view.findViewById(R.id.godmodelist);

        //动画 卡
//LinearLayout.LayoutParams godlistwrap_lp = godlistwrap.generateLayoutParams();
//godlistwrap_lp.layoutAnimationParameters = R.style.anim_notification;
//godlistwrap.setBackgroundColor(trans);

        constructView("godmode_slot_1");
        constructView("godmode_slot_2");
        constructView("godmode_slot_3");
        constructView("godmode_slot_4");
        constructView("godmode_slot_5");


        //time
        time_View = navbar_view.findViewById(R.id.time);
        time_View.setText(getSystemTime());
        time_View.setFocusable(false);
        Common.setBgRadius(time_View, radius);


        time_View.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            private float ox1, oy1;
            private float x1, x2;
            private float y1, y2;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x1 = motionEvent.getRawX();//得到相对应屏幕左上角的坐标
                        y1 = motionEvent.getRawY();

                        ox1 = motionEvent.getRawX();
                        oy1 = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2 = motionEvent.getRawX();
                        y2 = motionEvent.getRawY();


                        if (navbar_wrapper.getVisibility() != View.VISIBLE) {
                            lp.x += x2 - x1;
                            lp.y += y2 - y1;
                            wm.updateViewLayout(navbar_view, lp);
                        }
                        x1 = x2;
                        y1 = y2;

                        Log.i(TAG, "Current X: " + lp.x + " Y: " + lp.y);
                        break;
                    case MotionEvent.ACTION_UP:

                        x2 = motionEvent.getRawX();
                        y2 = motionEvent.getRawY();
//                        Log.i("i", x1 + ",,," + y1 + ",,," + x2 + ",,," + y2);
                        double distance = Math.sqrt(Math.abs(ox1 - x2) * Math.abs(ox1 - x2) + Math.abs(oy1 - y2) * Math.abs(oy1 - y2));//两点之间的距离
                        Log.i(TAG, "x1 - x2>>>>>>" + distance);
                        if (distance < 15) { // 距离较小，当作click事件来处理
                            Log.d(TAG, "onTouch: 点击事件");


                            if (navbar_wrapper == null) {
                                return false;
                            }
                            if (navbar_wrapper.getVisibility() == View.VISIBLE) {
                                setVisibility(false);
                            } else {
                                lp.x = 0;
                                lp.y = 0;
                                wm.updateViewLayout(navbar_view, lp);
                                setVisibility(true);
                            }
                            return false;
                        }

                }
                return true;
            }
        });


        navbar_view.setMinimumHeight(Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("min_height", "0")));
        wm.addView(navbar_view, lp);

        WindowInsetsControllerCompat controllerCompat = ViewCompat.getWindowInsetsController(navbar_view);
        controllerCompat.hide(WindowInsetsCompat.Type.statusBars());
        controllerCompat.hide(WindowInsetsCompat.Type.navigationBars());
        controllerCompat.hide(WindowInsetsCompat.Type.ime());
        controllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        navbar_view.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insetsx) {
                if(insetsx!=null)
                {

//                    WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
//                    WindowInsets windowInsets = windowMetrics.getWindowInsets();
                    Insets insets_temp = insetsx.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());

                    xLog.log(TAG," inset "+insets_temp.left);

//                    Insets insets_temp = insets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());
//                    xLog.log(TAG," inset "+insets_temp.bottom);
//                    xLog.log(TAG," inset "+insets_temp.left);
//                    xLog.log(TAG," inset "+insets_temp.right);
                }
                return insetsx;
            }
        });

        setVisibility(true);
        shown = true;
    }

    private void constructView(String key) {
        String option = PreferenceManager.getDefaultSharedPreferences(context).getString(key, "false");
        Log.i(TAG, "constructView: option loaded: " + option);
        switch (option) {
            case "false":
                return;
            case "home":
                homeicon();
                return;
            case "back":
                backicon();
                return;
            case "back_carlife":
                backCarlifeicon();
                return;
            case "clear":
                clearicon();
                return;
            default:
                appicon(option);
                return;
        }
    }

    private void appicon(String pkg) {
        try {
            ApplicationInfo temp_info = pm.getApplicationInfo(pkg, 0);
            LinearLayout frame = (LinearLayout) View.inflate(context, R.layout.godmode_cell, null);
            frame.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -1, 1));
            ImageView icon = frame.findViewById(R.id.godcellicon);
            icon.setImageDrawable(temp_info.loadIcon(pm));
            ViewGroup.LayoutParams lp2 = icon.getLayoutParams();

            lp2.width = iconsize;
            lp2.height = iconsize;
            icon.setLayoutParams(lp2);
            Common.setBgRadiusWithCutOut(icon, (int) (iconsize * 0.05), (int) (iconsize * 0.35));
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FakeStart.Start(context, pkg);
                }
            });
            frame.setFocusable(false);
            navbar_wrapper.addView(frame);
        } catch (Exception e) {
            Toast.makeText(context, "侧栏助手：无法获取应用", Toast.LENGTH_LONG).show();
        }
    }

    private void backicon() {
        LinearLayout frame = (LinearLayout) View.inflate(context, R.layout.godmode_cell, null);
        frame.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -1, 1));
        ImageView icon = frame.findViewById(R.id.godcellicon);
        icon.setImageResource(R.drawable.reply_fill);
        double padding = iconsize * 0.22;
        icon.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        ViewGroup.LayoutParams lp2 = icon.getLayoutParams();
        lp2.width = iconsize;
        lp2.height = iconsize;
        icon.setLayoutParams(lp2);
        //110 110 208
        icon.setBackgroundColor(Color.argb(255, 255, 255, 255));
        Common.setBgRadiusWithCutOut(icon, (int) (iconsize * 0.05), (int) (iconsize * 0.35));
        icon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (Common.checkAccessibilityPermission(context, MyAccessibilityService.class)) {
//                    EventBus.getDefault().post(MyAccessibilityService.BACK);
                    EventBus.getDefault().post(MyAccessibilityService.BACK);
                } else {
                    Common.requestAccessibilityPermission(context);
                }
            }
        });
        frame.setFocusable(false);
        navbar_wrapper.addView(frame);
    }

    private void backCarlifeicon() {
        LinearLayout frame = (LinearLayout) View.inflate(context, R.layout.godmode_cell, null);
        frame.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -1, 1));
        ImageView icon = frame.findViewById(R.id.godcellicon);
        icon.setImageResource(R.drawable.xiaodu);
        double padding = iconsize * 0.16;
        icon.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        ViewGroup.LayoutParams lp2 = icon.getLayoutParams();
        lp2.width = iconsize;
        lp2.height = iconsize;
        icon.setLayoutParams(lp2);

        icon.setBackgroundColor(Color.argb(255, 255, 255, 255));
        Common.setBgRadiusWithCutOut(icon, (int) (iconsize * 0.05), (int) (iconsize * 0.35));
        icon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                Intent i = new Intent("carlife.intent.action.openpage");
                i.setClassName("com.baidu.carlife", "com.baidu.carlife.CarlifeActivity");
                i.putExtra("pageid", 1);
//                i.putExtra("from", 96);
                i.addFlags(0x10104000);
                context.startActivity(i);
            }
        });
        frame.setFocusable(false);
        navbar_wrapper.addView(frame);
    }

    private void homeicon() {
        LinearLayout frame = (LinearLayout) View.inflate(context, R.layout.godmode_cell, null);
        frame.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -1, 1));
        ImageView icon = frame.findViewById(R.id.godcellicon);
        icon.setImageResource(R.drawable.application_two);
        double padding = iconsize * 0.16;
        icon.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        ViewGroup.LayoutParams lp2 = icon.getLayoutParams();
        lp2.width = iconsize;
        lp2.height = iconsize;
        icon.setLayoutParams(lp2);

        icon.setBackgroundColor(Color.argb(255, 255, 255, 255));
        Common.setBgRadiusWithCutOut(icon, (int) (iconsize * 0.05), (int) (iconsize * 0.35));
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FakeStart.Start(context, context.getPackageName());
            }
        });
        frame.setFocusable(false);
        navbar_wrapper.addView(frame);
    }

    private void clearicon() {
        LinearLayout frame = (LinearLayout) View.inflate(context, R.layout.godmode_cell, null);
        frame.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -1, 1));
        ImageView icon = frame.findViewById(R.id.godcellicon);
        icon.setImageResource(R.drawable.rocket_2_fill);
        double padding = iconsize * 0.16;
        icon.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        ViewGroup.LayoutParams lp2 = icon.getLayoutParams();
        lp2.width = iconsize;
        lp2.height = iconsize;
        icon.setLayoutParams(lp2);

        icon.setBackgroundColor(Color.argb(255, 255, 255, 255));
        Common.setBgRadiusWithCutOut(icon, (int) (iconsize * 0.05), (int) (iconsize * 0.35));
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////////////////////////


                Boolean is_allowed = false;
                try {
                    PackageManager packageManager = context.getPackageManager();
                    ApplicationInfo info = packageManager.getApplicationInfo(context.getPackageName(), 0);
                    AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//                appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName);
                    is_allowed = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName) == AppOpsManager.MODE_ALLOWED;
                } catch (Exception e) {
                    return;
                }
                if (!is_allowed) {
                    Intent it = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    context.startActivity(it);
                    Toast.makeText(context, "请开启权限！", Toast.LENGTH_LONG).show();
                }


                UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                UsageEvents usageEvents = usageStatsManager.queryEvents(time - 60 * 60 * 1000, time);

                UsageEvents.Event out;
                TreeMap<Long, UsageEvents.Event> map = new TreeMap<Long, UsageEvents.Event>();

                if (usageEvents != null) {
                    while (usageEvents.hasNextEvent()) {
                        out = new UsageEvents.Event();
                        if (usageEvents.getNextEvent(out)) {
                            if (out != null) {
                                map.put(out.getTimeStamp(), out);
                            } else {
                                //null
                            }
                        }
                    }
                }
                if (!map.isEmpty()) {
                    NavigableSet<Long> keysets = map.navigableKeySet();
                    Iterator<Long> iterator = keysets.descendingIterator();
                    ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    while (iterator.hasNext()) {
                        UsageEvents.Event event = map.get(iterator.next());
                        if (event != null
                                && event.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED
                                && !event.getPackageName().equals("com.baidu.carlife")
                                && !event.getPackageName().equals("com.samsung.android.carlink")
                        ) {
                            mActivityManager.killBackgroundProcesses(event.getPackageName());
                            Log.i("Tasker", "done: " + event.getPackageName());

                        }
                    }
                    Toast.makeText(context, "清理完毕 :)", Toast.LENGTH_LONG).show();
                }
                ///////////////////////////////////
            }
        });
        frame.setFocusable(false);
        navbar_wrapper.addView(frame);
    }

    private void switchVisibility() {
        if (navbar_wrapper == null) {
            return;
        }
        if (navbar_wrapper.getVisibility() == View.VISIBLE) {
            setVisibility(false);
        } else {

            setVisibility(true);
        }

    }

    private void setVisibility(Boolean visible) {
        if (visible) {

            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) navbar_view.getLayoutParams();
            lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
            wm.updateViewLayout(navbar_view, lp);
            Common.setBgRadius(navbar_view, 0);
            navbar_wrapper.setVisibility(View.VISIBLE);

            if (autohide) {
                if (autohide_timer != null) {
                    autohide_timer.cancel();
                }
                autohide_timer = new Timer();
                autohide_timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setVisibility(false);
                            }
                        });
                    }
                }, autohide_time * 1000L);
            }


        } else {
            if (autohide) {
                if (autohide_timer != null) {
                    autohide_timer.cancel();
                }
            }
            Common.setBgRadius(navbar_view, radius);
            navbar_wrapper.setVisibility(View.GONE);
            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) navbar_view.getLayoutParams();
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            wm.updateViewLayout(navbar_view, lp);
        }
    }


    public class updateTimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (shown && time_View != null) {
                time_View.setText(getSystemTime());
            }
        }
    }

    private String getSystemTime() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("HH:mm");
        long time = System.currentTimeMillis();
        return df.format(time);
    }

    public void onDestroy() {
        if (tiktok != null) {
            context.unregisterReceiver(tiktok);
        }

        if (shown) {
            try {
                wm.removeView(navbar_view);
            } catch (Exception e) {

            }
        }

        //avoid error
        shown = false;
        navBar = null;
    }
}
