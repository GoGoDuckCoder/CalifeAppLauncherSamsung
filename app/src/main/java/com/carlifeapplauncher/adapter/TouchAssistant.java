package com.carlifeapplauncher.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.banqu.samsung.music.R;
import com.carlifeapplauncher.MyAccessibilityService;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class TouchAssistant {
    private static String TAG = "TouchAssistant";
    private View ta_view;
    private WindowManager wm;

    private Context context;
    private boolean isShown;
    private Timer timer;
    private Timer sideHideTimer;

    private Boolean reverse;

    //Element
    private RecyclerView ta_drawer;
    private ImageButton ta_switch;
    private ImageButton ta_back;
    private ImageButton ta_home;
    private ImageButton ta_app;
    private ImageButton ta_move;
    private ImageButton ta_kill;
    //elements;
    //private Boolean horizontal;
    private Boolean dot_mode;
    private Boolean horizontal;


    public static TouchAssistant touchAssistant;

    public static TouchAssistant getInstance() {
        return touchAssistant;
    }

    public static TouchAssistant createInstance(Context context) {
        touchAssistant = new TouchAssistant(context);
        return touchAssistant;
    }


    public TouchAssistant(Context context) {
        this.isShown = false;
        this.wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.ta_view = View.inflate(context, R.layout.touchassistant, null);
        this.ta_view.setFocusable(false);
        this.context = context;
        this.reverse = false;
    }


    @SuppressLint({"ClickableViewAccessibility", "WrongConstant", "ResourceAsColor"})
    public void onStart() {
        //Check if Open TA
//        if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("touchassistant", false)) {
//            onDestroy();
//            return;
//        }
        //Check if Has Permission
        if (!Common.checkFloatPermission(context)) {
            Common.requestFloatPermission(context);
            return;
        }

        //Mode

        //Check if Already Shown
        if (isShown) {
            return;
        }
        //LOAD NEW CONFIG
        horizontal = true;
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("ta_vertical", false)) {
            horizontal = false;
        }
        if (!horizontal) {
            ((LinearLayout) ta_view).setOrientation(LinearLayout.VERTICAL);
        } else {
            ((LinearLayout) ta_view).setOrientation(LinearLayout.HORIZONTAL);
        }

        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("ta_reverse", false)) {
            if (!reverse) {
                Common.setup_rlf_mode_from_linerlayout((LinearLayout) ta_view);
                reverse = true;
            }
        } else {
            if (reverse) {
                Common.setup_rlf_mode_from_linerlayout((LinearLayout) ta_view);
                reverse = false;
            }
        }
        //View
        WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
        WindowInsets windowInsets = windowMetrics.getWindowInsets();
        Insets insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());


        int opacity = (int) (Double.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("ta_opacity", "100")) / 100 * 255);
        String rgb_string = PreferenceManager.getDefaultSharedPreferences(context).getString("ta_rgb", "0#0#0");
        String[] rgb_array = rgb_string.split("#");
        int trans = Color.argb(opacity, Integer.parseInt(rgb_array[0]), Integer.parseInt(rgb_array[1]), Integer.parseInt(rgb_array[2]));
        ta_view.setBackgroundColor(trans);

        int length = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("ta_icon_size", "0"));
        if (length == 0) {
            length = (int) (insets.left * 0.55);
            SharedPreferences.Editor ed  = PreferenceManager.getDefaultSharedPreferences(context).edit();
            if(length==0)
            {
                ed.putString("ta_icon_size","100");
            }else
            {
                ed.putString("ta_icon_size",length+"");
            }
            ed.apply();
        }

        int radius = (int) (length * 0.35);

        Common.setBgRadius(ta_view, radius);


        //Element
        ta_drawer = ta_view.findViewById(R.id.ta_applist);

        TAAppListAdapter favadapter = new TAAppListAdapter(context, this,length);
        ta_drawer.setAdapter(favadapter);

        if (horizontal) {
            ta_drawer.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        } else {
            ta_drawer.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        }


        ta_switch = (ImageButton) ta_view.findViewById(R.id.ta_switch);
        ta_back = (ImageButton) ta_view.findViewById(R.id.ta_back);
        ta_home = (ImageButton) ta_view.findViewById(R.id.ta_home);
        ta_app = (ImageButton) ta_view.findViewById(R.id.ta_app);
        ta_move = (ImageButton) ta_view.findViewById(R.id.ta_move);
        ta_kill = (ImageButton) ta_view.findViewById(R.id.ta_kill);


        //Mode
        dot_mode = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("ta_dot_mode", false);
        if (dot_mode) {
            ta_move.setVisibility(View.VISIBLE);
            ta_move.setImageResource(R.drawable.circle_fill);

            ta_drawer.setVisibility(View.GONE);
            ta_switch.setVisibility(View.GONE);
            ta_back.setVisibility(View.GONE);
            ta_home.setVisibility(View.GONE);
            ta_app.setVisibility(View.GONE);
            ta_kill.setVisibility(View.GONE);

        } else {
            ta_move.setImageResource(R.drawable.dot);
            //Buttons
            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("ta_drawer", true)) {
                ta_drawer.setVisibility(View.GONE);
                ta_switch.setVisibility(View.VISIBLE);
            } else {
                ta_drawer.setVisibility(View.GONE);
                ta_switch.setVisibility(View.GONE);
            }

            if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("show_back", false)) {
                ta_back.setVisibility(View.GONE);
            } else {
                ta_back.setVisibility(View.VISIBLE);
            }

            if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("show_home", true)) {
                ta_home.setVisibility(View.GONE);
            } else {
                ta_home.setVisibility(View.VISIBLE);
            }

            if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("show_app", false)) {
                ta_app.setVisibility(View.GONE);
            } else {
                ta_app.setVisibility(View.VISIBLE);
            }

            if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("show_move", true)) {
                ta_move.setVisibility(View.GONE);
            } else {
                ta_move.setVisibility(View.VISIBLE);
            }
            if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("show_kill", false)) {
                ta_kill.setVisibility(View.GONE);
            } else {
                ta_kill.setVisibility(View.VISIBLE);
            }
        }

        LinearLayout.LayoutParams lp_btn = (LinearLayout.LayoutParams) ta_back.getLayoutParams();





        lp_btn.width = length;
        lp_btn.height = length;
        //override
        ta_app.setLayoutParams(lp_btn);
        ta_back.setLayoutParams(lp_btn);
        ta_home.setLayoutParams(lp_btn);
        ta_kill.setLayoutParams(lp_btn);
        ta_move.setLayoutParams(lp_btn);
        ta_switch.setLayoutParams(lp_btn);

        //Float

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, PixelFormat.TRANSPARENT);
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.type = 2038;
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            lp.setFitInsetsTypes(0);
        }

        lp.x = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("ta_x", "300"));
        lp.y = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("ta_y", "300"));

        //Click
        ta_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jump_pkg = PreferenceManager.getDefaultSharedPreferences(context).getString("ta_favo_app", "false");
                if (jump_pkg != null && !jump_pkg.equals("false") && !jump_pkg.equals("1")) {
                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(jump_pkg);
                    context.startActivity(launchIntent);
                } else {
                    Toast.makeText(context, "请设置 设置->小白点助手->最爱应用", Toast.LENGTH_LONG).show();

                }
            }
        });
        ta_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.checkAccessibilityPermission(context, MyAccessibilityService.class)) {
                    EventBus.getDefault().post(MyAccessibilityService.BACK);
                } else {
                    Common.requestAccessibilityPermission(context);
                }
            }
        });
        ta_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FakeStart.StartSamsung(context, context.getPackageName());
            }
        });

        ta_move.setOnTouchListener(new View.OnTouchListener() {
            private float ox1, oy1;
            private float x1, x2;
            private float y1, y2;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
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

                        //cal
                        lp.x += x2 - x1;
                        lp.y += y2 - y1;

                        //renew
                        x1 = x2;
                        y1 = y2;

                        wm.updateViewLayout(ta_view, lp);
                        Log.i(TAG, "Current X: " + lp.x + " Y: " + lp.y);

                        break;
                    case MotionEvent.ACTION_UP:

                        x2 = motionEvent.getRawX();
                        y2 = motionEvent.getRawY();

                        editor.putString("ta_x", lp.x + "");
                        editor.putString("ta_y", lp.y + "");
                        editor.apply();

                        double distance = Math.sqrt(Math.abs(ox1 - x2) * Math.abs(ox1 - x2) + Math.abs(oy1 - y2) * Math.abs(oy1 - y2));//两点之间的距离
                        Log.i(TAG, "x1 - x2>>>>>>" + distance);
                        if (distance < 15) { // 距离较小，当作click事件来处理
                            Log.d(TAG, "onTouch: 点击事件");
                            if (dot_mode) {
                                //Buttons
                                if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("ta_drawer", true)) {
                                    ta_switch.setVisibility(View.VISIBLE);
                                }

                                if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("show_back", false)) {
                                    ta_back.setVisibility(View.VISIBLE);
                                }

                                if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("show_home", true)) {
                                    ta_home.setVisibility(View.VISIBLE);
                                }

                                if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("show_app", false)) {
                                    ta_app.setVisibility(View.VISIBLE);
                                }

                                if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("show_kill", false)) {
                                    ta_kill.setVisibility(View.VISIBLE);
                                }
                                ta_move.setVisibility(View.GONE);
                                resetSideHideTimer();

                            }

                            return false;
                        }

                }
                return true;
            }
        });
        ta_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int isExpand = ta_drawer.getVisibility();
                //hide
                if (isExpand == View.VISIBLE) {
                    ta_drawer.setVisibility(View.GONE);
                    if (dot_mode) {
                        resetSideHideTimer();
                    }
//                    ta_switch.setImageResource(R.drawable.chevron_left);
                    cancelTimer();
                }
                //show
                if (isExpand == View.GONE) {
                    if (dot_mode) {
                        cancelSideHideTimer();
                    }
                    ta_drawer.setVisibility(View.VISIBLE);
//                    ta_switch.setImageResource(R.drawable.chevron_right);
                    resetTimer();
                }
            }
        });
        ta_kill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean is_allowed = false;
                try {
                    PackageManager packageManager = context.getPackageManager();
                    ApplicationInfo info = packageManager.getApplicationInfo(context.getPackageName(), 0);
                    AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
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
                UsageEvents usageEvents = usageStatsManager.queryEvents(time - 24 * 60 * 1000, time);

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
                    while (iterator.hasNext()) {
                        UsageEvents.Event event = map.get(iterator.next());
                        if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                            mActivityManager.killBackgroundProcesses(event.getPackageName());
                            Log.i("Tasker", "done: " + event.getPackageName());

                        }
                    }
                    Toast.makeText(context, "清理完毕 :)", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Add View
        wm.addView(ta_view, lp);
        isShown = true;
    }

    public void onResume() {

    }

    public void onDestroy() {
        if (isShown) {
            try {
                wm.removeView(ta_view);
                isShown = false;
                touchAssistant=null;
            } catch (Exception e) {

            }
        }
    }


    //hide and show

    // Timer

    public void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }
        //timer operation
        boolean autohide = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("ta_drawer_autohide", true);

        if (autohide) {
            int s = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("ta_drawer_autohide_time", "5"));
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ta_drawer != null && timer != null) {
                                ta_drawer.setVisibility(View.GONE);
                                timer.cancel();
                                if (dot_mode) {
                                    resetSideHideTimer();
                                }
                            }
                        }
                    });
                }
            }, s * 1000);
        }
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }


    public void resetSideHideTimer() {
        if (timer != null) {
            timer.cancel();
        }
        int s = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("ta_dot_autohide_time", "3"));
        sideHideTimer = new Timer();
        sideHideTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout ll = ta_view.findViewById(R.id.ta_container);
                        for (int x = 0; x < ll.getChildCount(); x++) {
                            if (ll.getChildAt(x).getId() != R.id.ta_move) {
                                ll.getChildAt(x).setVisibility(View.GONE);
                            }
                        }

                        sideHideTimer.cancel();
                        ta_move.setImageResource(R.drawable.circle_fill);
                        ta_move.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, s * 1000);
    }

    private void cancelSideHideTimer() {
        if (sideHideTimer != null) {
            sideHideTimer.cancel();
        }
        sideHideTimer = null;
    }
}
