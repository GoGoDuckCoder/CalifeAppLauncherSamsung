package com.baidu.BaiduMap.music.notouch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.baidu.BaiduMap.music.MainActivity;
import com.baidu.BaiduMap.music.R;
import com.baidu.BaiduMap.music.carlifeapplauncher.MyAccessibilityService;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.Common;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

public class MouseService {


    private Context context;

    public MouseService(Context context) {
        this.context = context;
        onStart();
    }

    public static MouseService createInstance(Context context) {

        if (mouse == null) {
            mouse = new MouseService(context);
        }
        return mouse;
    }

    public static MouseService getInstance(Context context) {
        return mouse;
    }

    public void onStart() {
        startMouse();
        Log.i("MouseTesting", "sc");
    }


    public void onDestroy() {
        endMouse();
        mouse = null;
        Log.i("MouseTesting", "d");
    }

    private static MouseService mouse;
    private WindowManager wm;
    private View view_mouse;

    private WindowManager.LayoutParams lp;

    private mouseReciever mouseReciever;
    private Timer timer;
    private int delay;
    private int acc;

    private void startMouse() {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int x = wm.getCurrentWindowMetrics().getBounds().centerX();
        int y = wm.getCurrentWindowMetrics().getBounds().centerY();
        view_mouse = View.inflate(context, R.layout.mouse_view, null);
        lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                2038,
                262184,
                PixelFormat.TRANSLUCENT);
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        ;
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.setTitle("Mouse");
        lp.x = x;
        lp.y = y;
        int wide = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("xc_mouse_size", "80"));
        lp.width = wide;
        lp.height = wide;
        wm.addView(view_mouse, lp);

        acc = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("xc_mouse_speed", "0"));

        mouseReciever = new mouseReciever();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Action_click);
        intentFilter.addAction(Action_point);
        intentFilter.addAction(Action_home_carlife);
        intentFilter.addAction(Action_home_zs);
        intentFilter.addAction(Action_Back);
        ((Activity) context).registerReceiver(mouseReciever, intentFilter);

        resetTimer();
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }
        show();
        timer = new Timer();
        delay = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("xc_mouse_timeout", "5")) * 1000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hide();
                    }
                });
            }
        }, delay);
    }

    private void hide() {
        if (view_mouse != null) {
            view_mouse.setVisibility(View.GONE);
        }
    }

    private void show() {
        if (view_mouse != null) {
            view_mouse.setVisibility(View.VISIBLE);
        }
    }

    public void moveMouse(int deltaX, int deltaY) {
        double acc_d = ((double) acc) / 10;
        lp.x = (int) (lp.x + deltaX * (1 + acc_d));
        lp.y = (int) (lp.y + deltaY * (1 + acc_d));
        wm.updateViewLayout(view_mouse, lp);
        resetTimer();
        Log.i("MouseTesting", "update xy: X:" + lp.x + " Y: " + lp.y);

    }

    public static int displayId = 0;

    public void setDisplayId(int in_displayId) {
        displayId = in_displayId;
    }

    public void click() {
        resetTimer();
        MyAccessibilityService ins = MyAccessibilityService.getInstance();
        if (ins != null) {
            int[] location = new int[2];
            view_mouse.getLocationOnScreen(location);
            float y = location[1];
            float x = location[0];
            Log.i("MouseTesting", "cl xy: X:" + x + " Y: " + y);
            ins.dispatchClick(displayId, x, y);
        }
        return;
    }

    private void endMouse() {
        if (mouse != null) {
            timer.cancel();
            wm.removeViewImmediate(view_mouse);
            ((Activity) context).unregisterReceiver(mouseReciever);
        }
    }

    public static final String Action_point = "carlife.point.simulation_point";
    public static final String Action_click = "carlife.point.simulation_click";
    public static final String Action_Back = "carlife.point.simulation_back";
    public static final String Action_home_zs = "carlife.point.simulation_home_zs";
    public static final String Action_home_carlife = "carlife.point.simulation_home_carlife";

    public class mouseReciever extends BroadcastReceiver {
        @SuppressLint("WrongConstant")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("MouseTesting", "bc :" + action);
            if (Action_point.equals(action)) {
                int delta_x = intent.getIntExtra("delta_x", 0);
                int delta_y = intent.getIntExtra("delta_y", 0);
                moveMouse(delta_x, delta_y);
            }
            if (Action_click.equals(action)) {
                click();
            }
            if (Action_home_zs.equals(action)) {
                Intent i = new Intent();
                i.setClass(context, MainActivity.class);
                i.addFlags(0x10104000);
                context.startActivity(i);
            }
            if (Action_home_carlife.equals(action)) {
                Intent i = new Intent("carlife.intent.action.openpage");
                i.setClassName("com.baidu.carlife", "com.baidu.carlife.CarlifeActivity");
                i.putExtra("pageid", 1);
                i.addFlags(0x10104000);
                context.startActivity(i);
            }
            if (Action_Back.equals(action)) {
                if (Common.checkAccessibilityPermission(context, MyAccessibilityService.class)) {
                    EventBus.getDefault().post(MyAccessibilityService.BACK);
                } else {
                    Common.requestAccessibilityPermission(context);
                }
            }
        }
    }
}
