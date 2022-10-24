package com.carlifeapplauncher.adapter;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Insets;
import android.graphics.PixelFormat;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.banqu.samsung.music.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;


public class NotificationFactory {

    private Context context;
    private NotificationReceiver notificationReceiver;
    //UI
    private int left;
    private int width;
    private int notification_background_color;
    private int notification_text_color;

    //Setting
    private int notification_display_seconds;


    private static NotificationFactory notificationFactory;

    public static NotificationFactory getInstance() {
        return notificationFactory;
    }

    //require activity context
    public static void createInstance(Context context) {
        notificationFactory = new NotificationFactory(context);
    }

    public NotificationFactory(Context context) {
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
        WindowInsets windowInsets = windowMetrics.getWindowInsets();
        Insets insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());

        left = insets.left;
        width = 0;
        notificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("GAODONGJIA.GetNotifications");
        LocalBroadcastManager.getInstance(context).registerReceiver(notificationReceiver, filter);
        notification_display_seconds = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("notification_display_seconds", "5"));
        int opacity = (int) (Double.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("notification_opacity", "100")) / 100 * 255);
        notification_background_color = ContextCompat.getColor(context, R.color.cus_notification_background_color);
        notification_background_color = ColorUtils.setAlphaComponent(notification_background_color, opacity);
        notification_text_color = ContextCompat.getColor(context, R.color.cus_notification_text_color);
    }

    public void onDestroy() {
        if (notificationReceiver != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(notificationReceiver);
        }
        notificationFactory = null;
    }


    public class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            shownotification(context, intent);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void shownotification(Context context, Intent intent) {
        //dealwithdata

        Bundle bundle = intent.getExtras();
        Log.i("TestNotification", bundle.toString());
        if (bundle == null) {
            return;
        }
        //Customization

        //Package
//        String packagename = bundle.get("packagename").toString();
//        if (!whitelist.contains(packagename)) {
//            return;
//        }

        String label = bundle.get("label").toString();
        String title = bundle.get("title").toString();
        String text = bundle.get("text").toString();


        //Init UI
        WindowManager.LayoutParams lp;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);


        if (width == 0) {
            width = WindowManager.LayoutParams.WRAP_CONTENT;
        }

        lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, PixelFormat.TRANSPARENT);
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        lp.x = 0;
        lp.y = 0;
        lp.windowAnimations = R.style.anim_notification;

        View panel = View.inflate(context, R.layout.notificationspanel, null);

        LinearLayout v_notification_panel = panel.findViewById(R.id.notification_panel);
        v_notification_panel.setBackgroundColor(notification_background_color);


//        int opacity = (int) (Double.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("notification_opacity", "100")) / 100 * 255);
//        int text_color = ContextCompat.getColor(context, R.color.cus_notification_text_color);
//        notification_background_color = ColorUtils.setAlphaComponent(notification_background_color, opacity);


        TextView v_label = panel.findViewById(R.id.msg_label);
        TextView v_title = panel.findViewById(R.id.msg_title);
        TextView v_text = panel.findViewById(R.id.msg_text);
        v_title.setText(title);
        v_text.setText(text);
        v_label.setText(label);

        v_title.setTextColor(notification_text_color);
        v_text.setTextColor(notification_text_color);
        v_label.setTextColor(notification_text_color);


        Timer noti_timer = new Timer();
        noti_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    wm.removeView(panel);
                    noti_timer.cancel();
                } catch (Exception e) {

                }
            }
        }, notification_display_seconds * 1000);


        ImageButton close_btn = panel.findViewById(R.id.msg_close);
        close_btn.setImageResource(R.drawable.x_lg);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    wm.removeView(panel);
                    noti_timer.cancel();
                } catch (Exception e) {

                }
            }
        });

        Common.setBgRadius(v_notification_panel, 50);
        //add
        Common.setMargin(v_notification_panel, left, 0, 0, 0);
        wm.addView(panel, lp);
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("play_ringtone", false)) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            float volume = (float) (Double.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("play_ringtone_volume", "100")) / 100);
            r.setVolume(volume);
            Log.i("Volume", Float.toString(volume));
            r.setAudioAttributes(new AudioAttributes.Builder()
                    .setFlags(AudioAttributes.USAGE_UNKNOWN | AudioAttributes.USAGE_ALARM | AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setFlags(AudioAttributes.USAGE_MEDIA)
                    .build());
            r.play();
        }


    }
}

