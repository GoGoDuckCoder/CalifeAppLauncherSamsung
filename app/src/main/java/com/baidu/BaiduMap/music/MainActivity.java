package com.baidu.BaiduMap.music;

import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.MsgBoxUI;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NotificationFactory;
import com.baidu.BaiduMap.music.databinding.ActivityMainBinding;
import com.baidu.BaiduMap.music.carlifeapplauncher.NotificationListener;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.Common;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.FakeStart;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NavBar;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.NightMode;
import com.baidu.BaiduMap.music.carlifeapplauncher.adapter.TouchAssistant;
import com.baidu.BaiduMap.music.carlifeapplauncher.alive.Alive;
import com.baidu.BaiduMap.music.carlifeapplauncher.apps.AppsUI;
import com.baidu.BaiduMap.music.carlifeapplauncher.music.MediaSessionConnectionOperator;
import com.baidu.BaiduMap.music.carlifeapplauncher.music.MusicUI;
import com.baidu.BaiduMap.music.carlifeapplauncher.widget.LauncherAppWidgetHost;
import com.baidu.mobstat.StatService;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private MusicUI musicUI;
    private MsgBoxUI msgBoxUI;
    private boolean has_widget = false;

    public static MainActivity mainActivity;

    public static NotificationFactory notificationFactory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        Log.i("APPUI", "onCreate: ");
        NightMode.setCustomNightModeSetting(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        background();

        opacity();

        radius();

        //Init Widget
        has_widget = has_weidget(this);
        if (has_widget) {
            appWidgetHost = new LauncherAppWidgetHost(getApplicationContext(), APPWIDGET_HOST_ID);
            hostinit();
            loadWidgetConfig();
        }
        //Init components
        component();
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("APPUI", "onStart: ");
        if (has_widget) {
            appWidgetHost.startListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onStop() {
        if (has_widget) {
            appWidgetHost.stopListening();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        Log.i("APPUI", "onDestroy: ");

        if (notificationFactory != null) {
            notificationFactory.onDestroy();
        }
        if (musicUI != null) {
            musicUI.onDestroy();
        }
        if (msgBoxUI != null) {
            msgBoxUI.onDestroy();
        }
        super.onDestroy();
    }

    private void background() {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("cusbackground", false)) {
            try {
                String path = this.getFilesDir() + "/bg.png";//获取本地目录
                File a = new File(path);
                if (a.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(path);
                    BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
                    binding.backgroundimage.setImageDrawable(bd);
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "背景图片出错，请用PNG格式，请换一张图片尝试！", Toast.LENGTH_LONG).show();
            }
            binding.backgroundimage.setImageDrawable(null);
        }
    }

    private void opacity() {
        int opacity = (int) (Double.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("cardopacity", "60")) / 100 * 255);
        int color = ContextCompat.getColor(this, R.color.maincard_background_color);
        color = ColorUtils.setAlphaComponent(color, opacity);
        binding.containerA.setBackgroundColor(color);
        binding.containerB.setBackgroundColor(color);
        binding.containerC.setBackgroundColor(color);
    }

    private void radius() {
        int radius = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("radius", "50"));
        Common.setBgRadius(binding.containerA, radius);
        Common.setBgRadius(binding.containerB, radius);
        Common.setBgRadius(binding.containerC, radius);
    }


    public static boolean is_empty_A(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("areaA", "false").equals("false");
    }

    public static boolean is_empty_B(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("areaB", "app").equals("false");
    }

    public static boolean is_empty_C(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("areaC", "false").equals("false");
    }

    public static boolean has_weidget(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getString("areaA", "false").equals("widget") ||
                PreferenceManager.getDefaultSharedPreferences(context).getString("areaB", "app").equals("widget") ||
                PreferenceManager.getDefaultSharedPreferences(context).getString("areaC", "false").equals("widget")) {
            return true;
        } else {
            return false;
        }

    }

    private void component() {


        binding.containerA.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,
                Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("weightA", "6"))));
        binding.right.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,
                Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("weightRight", "4"))));
        binding.containerB.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,
                Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("weightB", "4"))));
        binding.containerC.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,
                Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("weightC", "6"))));


        binding.containerA.setFocusable(false);
        binding.containerB.setFocusable(false);
        binding.containerC.setFocusable(false);

        ArrayList<String> component = new ArrayList<String>();
        component.add("areaA");
        component.add("areaB");
        component.add("areaC");

        for (String co : component) {
            ViewGroup location_vg = null;
            switch (co) {
                case "areaA":
                    location_vg = binding.containerA;
                    break;
                case "areaB":
                    location_vg = binding.containerB;
                    break;
                case "areaC":
                    location_vg = binding.containerC;
                    break;
            }
            if (location_vg == null) {
                continue;
            }

            String plugin = PreferenceManager.getDefaultSharedPreferences(this).getString(co, "false");

            if (co.equals("areaA")) {
                plugin = PreferenceManager.getDefaultSharedPreferences(this).getString(co, "false");
            }

            if (co.equals("areaB")) {
                plugin = PreferenceManager.getDefaultSharedPreferences(this).getString(co, "app");
            }
            Log.i("temptest", "component: " + co + " " + plugin);

            switch (plugin) {
                case "app":
                    AppsUI appsUI = new AppsUI(this, getLayoutInflater(), location_vg, false);
                    break;
                case "music":
                    musicUI = new MusicUI(this, getLayoutInflater(), location_vg);
                    break;
                case "widget":
                    setupWidgetUI(location_vg);
                    break;
                case "msgbox":
                    if (NotificationListener.isEnabled(this)) {
                        notificationFactory = NotificationFactory.getInstance(this, getComponentName());
                        msgBoxUI = new MsgBoxUI(this, getLayoutInflater(), location_vg);
                    } else {
                        Toast.makeText(this, "请重新开启消息盒子开关,并开启读取通知权限.", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }

        boolean s1 = false;
        boolean s2 = false;
        boolean s3 = false;
        if (is_empty_A(this)) {
            binding.containerA.setVisibility(View.GONE);
            s1 = true;
        }
        if (is_empty_B(this)) {
            binding.containerB.setVisibility(View.GONE);
            s2 = true;
        }
        if (is_empty_C(this)) {
            binding.containerC.setVisibility(View.GONE);
            s3 = true;
        }
        if (is_empty_B(this) && is_empty_C(this)) {
            binding.right.setVisibility(View.GONE);
        }

//        if (s1 && s2 && s3) {
////            finish();;
//            emptyview = true;
//        }
//        if (emptyview && !PreferenceManager.getDefaultSharedPreferences(this).getBoolean("jump", false)) {
//            Toast.makeText(getBaseContext(), "请先通过手机打开助手设置在主界面后，再使用。界面参数以车机显示为准！", Toast.LENGTH_LONG).show();
//        }
//        if (emptyview && PreferenceManager.getDefaultSharedPreferences(this).getBoolean("jump", false)) {
//            if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("close_after_jump", false)) {
//                Toast.makeText(getBaseContext(), "请先通过手机打开助手设置在主界面后，再使用。界面参数以车机显示为准！", Toast.LENGTH_LONG).show();
//            }
//        }

        int margin = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("margin", "15"));
        Common.setMargin(binding.containerA, margin, margin, margin, margin);
        Common.setMargin(binding.containerB, margin, margin, margin, margin);
        Common.setMargin(binding.containerC, margin, margin, margin, margin);

        if (!is_empty_A(this) && (!is_empty_B(this) || !is_empty_C(this))) {
            Common.setMargin(binding.containerA, margin, margin, 0, margin);
        }
        if (!is_empty_B(this) && !is_empty_C(this)) {
            Common.setMargin(binding.containerB, margin, margin, margin, 0);
        }
    }


    /*
     * Functions Interact With Widgets
     *
     *
     *
     */
    public static final int APPWIDGET_HOST_ID = 8888;
    private LauncherAppWidgetHost appWidgetHost = null;

    private static final int REQUEST_WIDGET = 0;
    private static final int REQUEST_CONFIGURE = 1;
    private String TS = "WIDGETTEST";
    private ViewGroup viewAwaiting = null;
    private int idAwaiting = AppWidgetManager.INVALID_APPWIDGET_ID;
    private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

    private void setupWidgetUI(ViewGroup root) {

        if (map.containsKey(root.getId())) {
            //has config
            Log.i(TS, "有配置文件");
            int old_id = map.get(root.getId());
            AppWidgetProviderInfo info = AppWidgetManager.getInstance(getBaseContext()).getAppWidgetInfo(old_id);
            if (info != null) {
                Log.i(TS, "配置有效");
                finishSetAppWidgetFromLoading(root, old_id);
                return;
            } else {
                Log.i(TS, "配置失效");
                appWidgetHost.deleteAppWidgetId(old_id);
                map.remove(root.getId());
                saveWidgetConfig();
            }
        } else {
            Log.i(TS, "无配置文件");
        }
        //no config
        View widget = View.inflate(this, R.layout.widget, root);
        ImageButton addWidgetButton = widget.findViewById(R.id.link);
        addWidgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idAwaiting = appWidgetHost.allocateAppWidgetId();
                viewAwaiting = root;
                startChooseActivity();
            }
        });
    }


    private void startChooseActivity() {
        if (idAwaiting != AppWidgetManager.INVALID_APPWIDGET_ID) {
            Intent selectIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
            selectIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, idAwaiting);
            startActivityForResult(selectIntent, REQUEST_WIDGET);
        }
    }

    private void setAppWidget(int appWidgetId) {
        /* Check for configuration */
        AppWidgetProviderInfo providerInfo =
                AppWidgetManager.getInstance(getBaseContext()).getAppWidgetInfo(appWidgetId);


        if (providerInfo.configure != null) {
            Intent configureIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            configureIntent.setComponent(providerInfo.configure);
            configureIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            if (configureIntent != null) {
                try {
                    Log.i(TS, "有配置");
                    appWidgetHost.startAppWidgetConfigureActivityForResult(this, appWidgetId, 0, REQUEST_CONFIGURE, null);
                } catch (Exception e) {
                    Log.i(TS, "有配置，发生错误");
                }
            }
        } else {
            finishSetAppWidget(appWidgetId);
        }
    }

    private void finishSetAppWidget(int appWidgetId) {
        AppWidgetProviderInfo providerInfo =
                AppWidgetManager.getInstance(getBaseContext()).getAppWidgetInfo(appWidgetId);
        if (providerInfo != null) {
            AppWidgetHostView mAppWidgetView =
                    appWidgetHost.createView(getBaseContext(), appWidgetId, providerInfo);

            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mAppWidgetView.setLayoutParams(p);
            mAppWidgetView.setPadding(0, 0, 0, 0);
            mAppWidgetView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(getApplicationContext(), "已移除小组件", Toast.LENGTH_LONG).show();
                    appWidgetHost.deleteAppWidgetId(appWidgetId);
                    ViewGroup parent = (ViewGroup) view.getParent();
                    map.remove(parent.getId());
                    saveWidgetConfig();
                    parent.removeAllViews();
                    setupWidgetUI(parent);
                    return true;
                }
            });
            if (viewAwaiting != null) {
                viewAwaiting.removeAllViews();
                viewAwaiting.addView(mAppWidgetView);
                saveWidgetConfig(viewAwaiting.getId(), appWidgetId);
            } else {
                widgetFail();
            }
        }
    }

    private void finishSetAppWidgetFromLoading(ViewGroup v, int appWidgetId) {
        AppWidgetProviderInfo providerInfo =
                AppWidgetManager.getInstance(getBaseContext()).getAppWidgetInfo(appWidgetId);
        if (providerInfo != null) {
            AppWidgetHostView mAppWidgetView =
                    appWidgetHost.createView(getBaseContext(), appWidgetId, providerInfo);

            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mAppWidgetView.setLayoutParams(p);
            mAppWidgetView.setPadding(0, 0, 0, 0);
            mAppWidgetView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(getApplicationContext(), "已移除小组件", Toast.LENGTH_LONG).show();
                    appWidgetHost.deleteAppWidgetId(appWidgetId);
                    map.remove(v.getId());
                    saveWidgetConfig();
                    v.removeAllViews();
                    setupWidgetUI(v);
                    return true;
                }
            });
            v.removeAllViews();
            v.addView(mAppWidgetView);
            if (providerInfo.provider.getPackageName().equals("com.autonavi.amapauto")) {
                View mask = this.getLayoutInflater().inflate(R.layout.empty_mask, null);

                mask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("testapp", "finishSetAppWidgetFromLoading: click");
                        FakeStart.Start(getApplicationContext(), "com.autonavi.amapauto");
                    }
                });
                v.addView(mask);
            }
        }
    }


    private void saveWidgetConfig(int parentId, int id) {
        map.put(parentId, id);
        try {
            ObjectOutputStream oss = new ObjectOutputStream(this.openFileOutput("WidgetConfig", this.MODE_PRIVATE));
            oss.writeObject(map);
            oss.close();
            Log.i(TS, "已保存插件设置");
        } catch (Exception e) {
        }
    }

    private void saveWidgetConfig() {
        try {
            ObjectOutputStream oss = new ObjectOutputStream(this.openFileOutput("WidgetConfig", this.MODE_PRIVATE));
            oss.writeObject(map);
            oss.close();
            Log.i(TS, "已保存插件设置");
        } catch (Exception e) {
        }
    }

    private void loadWidgetConfig() {
        try {
            ObjectInputStream oss = new ObjectInputStream(this.openFileInput("WidgetConfig"));
            map.clear();
            map.putAll((HashMap<Integer, Integer>) oss.readObject());
            oss.close();
            Log.i(TS, "已加载插件设置");
        } catch (Exception e) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_WIDGET:
                if (data != null) {
                    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
                    if (data.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
                        appWidgetId = data.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
                    }
                    if (resultCode == RESULT_OK) {
                        Log.i(TS, "选取插件：成功");
                        setAppWidget(appWidgetId);
                        return;
                    } else {
                        Log.i(TS, "选取插件：错误：取消或错误" + appWidgetId);
                    }
                } else {
                    Log.i(TS, "选取插件：错误：无数据");
                }
                widgetFail();
                break;

            case REQUEST_CONFIGURE:
                if (data != null) {
                    int appWidgetId = data.getExtras().getInt(
                            AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                    if (resultCode == RESULT_OK) {
                        Log.i(TS, "配置插件：成功");
                        finishSetAppWidget(appWidgetId);
                        return;
                    } else {
                        Log.i(TS, "配置插件：失败");
                    }
                }
                widgetFail();
                break;
        }
    }

    private void widgetFail() {
        appWidgetHost.deleteAppWidgetId(idAwaiting);
        idAwaiting = AppWidgetManager.INVALID_APPWIDGET_ID;
        viewAwaiting = null;
    }

    private void hostinit() {
        boolean is_hostinit = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("hostinit", false);
        if (!is_hostinit) {
            Log.i(TS, "NO INIT");
            appWidgetHost.deleteHost();
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putBoolean("hostinit", true);
            editor.apply();
        } else {
            Log.i(TS, "INIT ED");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}