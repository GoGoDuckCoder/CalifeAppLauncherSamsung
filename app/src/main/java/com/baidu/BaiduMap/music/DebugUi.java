package com.baidu.BaiduMap.music;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.BaiduMap.music.notouch.MouseService;

public class DebugUi extends AppCompatActivity {

    public String auto_ass_list = "";

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_ui);

        auto_ass_list = ":com.baidu.BaiduMap/com.baidu.BaiduMap.music.carlifeapplauncher.MyAccessibilityService";

        if (true) {
            String ass_list = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (!ass_list.contains(auto_ass_list)) {
                Settings.Secure.putString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, ass_list + auto_ass_list);
                Settings.Secure.putInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 1);
            }
        }

        Button cm = this.findViewById(R.id.button3);
        TextView t = this.findViewById(R.id.counterpanel);

        cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setText(""+(Integer.parseInt(t.getText().toString())+1));
            }
        });
        Button mouse = this.findViewById(R.id.mouse);
        mouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DebugUi.this,"clicked",Toast.LENGTH_LONG).show();
                Intent i = new Intent(DebugUi.this, MouseService.class);
                startService(i);
            }
        });
        FrameLayout fl = this.findViewById(R.id.touchpanel);
        fl.setOnTouchListener(new View.OnTouchListener() {
            private float ox1, oy1;
            private float x1, x2;
            private float y1, y2;
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
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
                        int dx=(int)(x2-x1);
                        int dy=(int)(y2-y1);
                        Log.i("MouseTesting", "Current X: " + dx + " Y: " + dy);

                        if(MouseService.getInstance(DebugUi.this)!=null)
                        {
                            MouseService.getInstance(DebugUi.this).moveMouse(dx,dy);
                        }

                        x1 = x2;
                        y1 = y2;

                        break;
                    case MotionEvent.ACTION_UP:

                        x2 = motionEvent.getRawX();
                        y2 = motionEvent.getRawY();
//                        Log.i("i", x1 + ",,," + y1 + ",,," + x2 + ",,," + y2);
                        double distance = Math.sqrt(Math.abs(ox1 - x2) * Math.abs(ox1 - x2) + Math.abs(oy1 - y2) * Math.abs(oy1 - y2));//两点之间的距离
//                        Log.i(TAG, "x1 - x2>>>>>>" + distance);
                        if (distance < 15) { // 距离较小，当作click事件来处理
//                            Log.d(TAG, "onTouch: 点击事件");
                            if(MouseService.getInstance(DebugUi.this)!=null)
                            {
                                MouseService.getInstance(DebugUi.this).click();
                            }
                            return false;
                        }
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (true) {
            String ass_list = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (ass_list.contains(auto_ass_list)) {
                ass_list = ass_list.replace(auto_ass_list,"");
                Settings.Secure.putString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, ass_list);
                Settings.Secure.putInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 1);
            }
        }
        super.onDestroy();
    }
}