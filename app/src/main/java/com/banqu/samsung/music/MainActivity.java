package com.banqu.samsung.music;

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

import com.banqu.samsung.music.databinding.ActivityMainBinding;
import com.carlifeapplauncher.NotificationListener;
import com.carlifeapplauncher.adapter.Common;
import com.carlifeapplauncher.adapter.FakeStart;
import com.carlifeapplauncher.adapter.NavBar;
import com.carlifeapplauncher.adapter.NightMode;
import com.carlifeapplauncher.adapter.TouchAssistant;
import com.carlifeapplauncher.alive.Alive;
import com.carlifeapplauncher.apps.AppsUI;
import com.carlifeapplauncher.music.MediaSessionConnectionOperator;
import com.carlifeapplauncher.music.MusicUI;
import com.carlifeapplauncher.widget.LauncherAppWidgetHost;

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
    //    private TouchAssistant ta;
    //    private NavBar godMode;
//    private NotificationFactory nf;
    private MusicUI musicUI;
    private MediaSessionConnectionOperator musicServiceOperator;
    private boolean emptyview = false;
    private boolean has_widget = false;

    public static MainActivity mainActivity;

//    private BlackScreen blackScreen;


    //plugin
    private boolean phone_godmode;
    private boolean phone_ta;
    private boolean phone_music;

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

//        WindowInsetsController controller = getWindow().getInsetsController();
//        controller.hide(WindowInsets.Type.statusBars());

        jump();


//        CarViewInit = true;


//        NotificationListener.ensureConnection(this);

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
//        appWidgetHost.deleteHost();


        component();

//        ta = new TouchAssistant(this);
//        godMode = new NavBar(this);
//        nf = new NotificationFactory(this);
//        nf.onCreate();

//        call();
//        call_new();

//        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("exp", false)) {
//            Toast.makeText(this, "未注册：体验10分钟！体验结束后将自动退出！", Toast.LENGTH_LONG).show();
//            Timer t = new Timer();
//            t.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    if (appWidgetHost != null) {
//                        appWidgetHost.deleteHost();
//                    }
//                    SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(MainActivityFinal.this).edit();
//                    ed.clear();
//                    ed.commit();
//                    android.os.Process.killProcess(android.os.Process.myPid());
//                }
//            }, 1000 * 11 * 59);
//        }


        ///test

//        Detect.run(this);
//        Intent service = new Intent(this, Detect.class);
//        startForegroundService(service);
//        blackScreen = new BlackScreen(this);
//        blackScreen.show();
        phone_godmode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("godmode", false);
        phone_ta = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("touchassistant", false);

        if (phone_godmode) {
            if (NavBar.getInstance() == null) {
                NavBar.createInstance(this).onStart();
            }
        }

        if (phone_ta) {
            if (TouchAssistant.getInstance() == null) {
                TouchAssistant.createInstance(this).onStart();
            }
        }

       phone_music= PreferenceManager.getDefaultSharedPreferences(this).getBoolean("music_mirror", false);
        if (phone_music) {
            musicServiceOperator = MediaSessionConnectionOperator.getInstance(getApplicationContext());
//            musicServiceOperator = MediaSessionConnectionOperator.getInstance(this);
            musicServiceOperator.connect();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("APPUI", "onStart: ");
//        if (!emptyview) {

        if (has_widget) {
            appWidgetHost.startListening();
        }

//        ta.onStart();
//        godMode.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("APPUI", "onResume: ");
    }

    @Override
    public void onStop() {
        Log.i("APPUI", "onStop: ");
        if (has_widget) {
            appWidgetHost.stopListening();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {


        Log.i("APPUI", "onDestroy: ");
        if (musicUI != null) {
            musicUI.onDestroy();
        }
//        ta.onDestroy();
//        godMode.onDestroy();
//        nf.onDestroy();

        if (NotificationListener.isReady()) {
            NotificationListener.getInstance().killConnection(this);
        }

        Alive a = new Alive(this);
        a.onDestroy();

//        CarViewInit = false;
//        stopService(aliveService);


//        android.os.Process.killProcess(Process.myPid());
//        blackScreen.hide();


        if (phone_godmode) {
            if (NavBar.getInstance() != null) {
                NavBar.getInstance().onDestroy();
            }
        }

        if (phone_ta) {
            if (TouchAssistant.getInstance() != null) {
                TouchAssistant.getInstance().onDestroy();
            }
        }

        if (musicServiceOperator != null) {
            musicServiceOperator.disconnect();
        }


        mainActivity = null;

        super.onDestroy();
    }

//    @Override
//    public void onWindowFocusChanged(boolean has_focus) {
//        super.onWindowFocusChanged(has_focus);
//        Rect tt = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(tt);
////        nf.setLeft(tt.left);
////        nf.setWidth(tt.width());
////        godMode.setGodParameter(tt);
//    }


    private void jump() {
        boolean jump = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("jump", false);
        if (jump) {
            String jump_pkg = PreferenceManager.getDefaultSharedPreferences(this).getString("jump_pkg_lp", "false");
            boolean close = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("close_after_jump", false);
            if (close) {
                startJump(jump_pkg);
                finish();
            } else {
                int delay = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("jump_delay", "2"));
                if (delay == 0) {
                    startJump(jump_pkg);
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(delay * 1000);
                                startJump(jump_pkg);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }


        }
    }

    private void startJump(String jump_pkg) {
        if (jump_pkg != null && !jump_pkg.equals("false") && !jump_pkg.equals("null")) {
            try {
                FakeStart.Start(this, jump_pkg);
            } catch (Exception e) {
                Toast.makeText(this, "启动失败请重新设置跨越启动相关设置", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putBoolean("jump", false);
                editor.putString("jump_pkg_lp", "false");
                editor.apply();
            }
        }
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
        return PreferenceManager.getDefaultSharedPreferences(context).getString("areaA", "music").equals("false");
    }

    public static boolean is_empty_B(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("areaB", "app").equals("false");
    }

    public static boolean is_empty_C(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("areaC", "false").equals("false");
    }

    public static boolean has_weidget(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getString("areaA", "music").equals("widget") ||
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
                plugin = PreferenceManager.getDefaultSharedPreferences(this).getString(co, "music");
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

        if (s1 && s2 && s3) {
//            finish();;
            emptyview = true;
        }
        if (emptyview && !PreferenceManager.getDefaultSharedPreferences(this).getBoolean("jump", false)) {
            Toast.makeText(getBaseContext(), "请先通过手机打开助手设置在主界面后，再使用。界面参数以车机显示为准！", Toast.LENGTH_LONG).show();
        }
        if (emptyview && PreferenceManager.getDefaultSharedPreferences(this).getBoolean("jump", false)) {
            if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("close_after_jump", false)) {
                Toast.makeText(getBaseContext(), "请先通过手机打开助手设置在主界面后，再使用。界面参数以车机显示为准！", Toast.LENGTH_LONG).show();
            }
        }

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
            Log.i("testapp", "finishSetAppWidgetFromLoading: running " + providerInfo.getActivityInfo().packageName);
            v.removeAllViews();
            v.addView(mAppWidgetView);
            if (providerInfo.getActivityInfo().packageName.equals("com.autonavi.amapauto")) {
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

//    private PhoneCallReceiver phoneCallReceiver;
//
//    private void call() {
//        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("godmode", false) || !PreferenceManager.getDefaultSharedPreferences(this).getBoolean("godmodephone", false) || !phone.ensurePermission(this)) {
//            return;
//        }
//        if (this.getDisplay().getDisplayId() == 0) {
//            return;
//        }
//        if (this.getDisplay().getState() == Display.STATE_UNKNOWN) {
//            return;
//        }
//        phoneCallReceiver = new PhoneCallReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
//        this.registerReceiver(phoneCallReceiver, filter);
//        Intent intent = new Intent(Intent.ACTION_CALL);
//        String number = PreferenceManager.getDefaultSharedPreferences(this).getString("godmodephonenumber", "10086");
//        Uri data = Uri.parse("tel:" + number);
//        intent.setData(data);
//        startActivity(intent);
//    }

//    public class PhoneCallReceiver extends BroadcastReceiver {
//        @Override
//        @SuppressLint("MissingPermission")
//        public void onReceive(Context context, Intent intent) {
//            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//            Log.i("PhoneCallReceiver", state);
//            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//                Log.i("PhoneCallReceiver", "call kill");
//                if (phoneCallReceiver != null) {
//                    Log.i("PhoneCallReceiver", "unregisterReceiver: kill");
//                    context.unregisterReceiver(phoneCallReceiver);
//                }
//                Timer killer = new Timer();
//                killer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        TelecomManager telecom = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
//                        telecom.endCall();
//                        telecom.
//                        FakeStart.Start(MainActivityFinal.this, MainActivityFinal.this.getPackageName());
//                    }
//                }, Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(MainActivityFinal.this).getString("godmodephonenumbertictok", "2")));
//
//            }
//        }
//    }


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

//TEST CODE
//        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        ComponentName cn = new ComponentName(this, AdminManageReceiver.class);
//        if (devicePolicyManager.isAdminActive(cn))
//        {
//            devicePolicyManager.lockNow();
//        }else
//        {
//            showAdminManagement(cn);
//        }

//    private void showAdminManagement(ComponentName mAdminName) {
//        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
//        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "activity device");
//        startActivity(intent);
//    }

    public void call_new() {
//        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("exp_autolockbyphone", false)) {
//            if (MyAccessibilityService.isServiceRunning()) {
//                if (OpenProvider.isConnected(this) && getDisplay().getDisplayId() != 0) {
//                    MyAccessibilityService.getInstance().add_CallPackage();
//                    Log.i("MyAccessibilityService", "call_new: ");
//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    Uri data = Uri.parse("tel:" + PreferenceManager.getDefaultSharedPreferences(this).getString("exp_autolockbyphone_number", "10086"));
//                    intent.setData(data);
//                    startActivity(intent);
//                }
//            } else {
//                Toast.makeText(this, "无障碍权限丢失", Toast.LENGTH_LONG).show();
//            }
//        }
    }
}