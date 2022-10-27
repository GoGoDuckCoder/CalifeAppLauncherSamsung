package com.banqu.samsung.music.carlifeapplauncher.blackscreen;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.banqu.samsung.music.R;

import androidx.appcompat.app.AppCompatActivity;

public class BlackScreenActivity extends AppCompatActivity {

    private int click = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_black_screen);


        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        String tag = "myapp:mywakelocktag";
        PowerManager.WakeLock wl = pm.newWakeLock(0x1000001a, tag);
        if (wl != null && !wl.isHeld()) {
            wl.acquire();
        }
        View v = findViewById(R.id.bg);
        v.setBackgroundColor(Color.BLACK);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click++;
                if (click == 2) {
                    finish();
                    wl.release();
                }
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
            }
        });
    }
}