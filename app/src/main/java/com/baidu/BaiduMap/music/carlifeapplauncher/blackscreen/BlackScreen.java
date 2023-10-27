package com.baidu.BaiduMap.music.carlifeapplauncher.blackscreen;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.baidu.BaiduMap.music.R;

import androidx.preference.PreferenceManager;

public class BlackScreen {

    private static boolean isShown = false;
    private WindowManager wm;
    private View blackScreenButton;
    private Context context;
    private int click = 0;

    public BlackScreen(Context context) {
        this.context = context;
        wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        blackScreenButton = View.inflate(context, R.layout.black_screen_button, null);
    }

    public void show() {
        if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("exp_autoblackscreen",false))
        {
            return;
        }
        if (!isShown) {
            blackScreenButton.setFocusable(false);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, PixelFormat.TRANSPARENT);
            lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            lp.x = 50;
            lp.y = 50;
            wm.addView(blackScreenButton, lp);

            blackScreenButton.setOnTouchListener(new View.OnTouchListener() {
                private float ox1, oy1;
                private float x1, x2;
                private float y1, y2;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x1 = motionEvent.getRawX();
                            y1 = motionEvent.getRawY();
                            ox1 = motionEvent.getRawX();
                            oy1 = motionEvent.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            x2 = motionEvent.getRawX();
                            y2 = motionEvent.getRawY();
                            lp.x -= x2 - x1;
                            lp.y -= y2 - y1;

                            wm.updateViewLayout(blackScreenButton, lp);
                            x1 = x2;
                            y1 = y2;
                            break;
                        case MotionEvent.ACTION_UP:

                            x2 = motionEvent.getRawX();
                            y2 = motionEvent.getRawY();
                            double distance = Math.sqrt(Math.abs(ox1 - x2) * Math.abs(ox1 - x2) + Math.abs(oy1 - y2) * Math.abs(oy1 - y2));//两点之间的距离
                            if (distance < 15) { // 距离较小，当作click事件来处理
                                click++;

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(500);
                                            click =0;
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                                if(click==2)
                                {
                                    Intent i = new Intent();
                                    i.setClassName(context,BlackScreenActivity.class.getName());
                                    ActivityOptions makeBasics = ActivityOptions.makeBasic();
                                    makeBasics.setLaunchDisplayId(0);
                                    context.startActivity(i,makeBasics.toBundle());
                                }
                                return false;
                            }

                    }
                    return true;
                }
            });
            isShown = true;
        }
    }


    public void hide() {
        if (isShown) {
            wm.removeView(blackScreenButton);
            isShown = false;
        }
    }

}
