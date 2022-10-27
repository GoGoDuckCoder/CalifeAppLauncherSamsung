package com.banqu.samsung.music.carlifeapplauncher;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.banqu.samsung.music.adapter.ActivityManager;
import com.banqu.samsung.music.deeplink.DeepLinkService;
import com.banqu.samsung.music.log.xLog;
import com.banqu.samsung.music.carlifeapplauncher.adapter.OpenProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.preference.PreferenceManager;

public class MyAccessibilityService extends AccessibilityService {

    public static final int BACK = 1;
    public static final int HOME = 2;
    public static final int TASK = 3;
    private static final String TAG = "MyAccessibilityService";


    private static MyAccessibilityService myservice;

    public static MyAccessibilityService getInstance() {
        return myservice;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myservice = this;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        //LifeCycle
        if (connection) {
            onDisconnected();
        }
        //Accessibility
        EventBus.getDefault().unregister(this);
        myservice = null;
        super.onDestroy();
    }

//    private static Timer autostart_timer;
//    private static String autostart_package;

//    private void autostart() {
//        //switch off
//        if (!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("exp_autostart", false)) {
//            Log.i(TAG, "switch off ");
//            return;
//        }
//        //switch on
//        Log.i(TAG, "switch on ");
//        //check if supported
//        if (!OpenProvider.isSupported(this)) {
//            Log.i(TAG, "not supported");
//            return;
//        }
//        Log.i(TAG, "supported");
//        //disconnected
//        if (!OpenProvider.isConnected(this)) {
//            Log.i(TAG, "not connected");
//            if (autostart_timer != null) {
//                Log.i(TAG, "not connected: cancel timer");
//                autostart_timer.cancel();
//                autostart_timer = null;
//            }
//            return;
//        }
//        //connected
//        else {
//            Log.i(TAG, "connected");
//            //timer task already existed return
//            if (autostart_timer != null) {
//                Log.i(TAG, "timer existed");
//                return;
//            }
//            Log.i(TAG, "timer not existed, init one");
//        }
//
//        //no task existed and need  a new task
//
//
//        //validate pacakgename
//        autostart_package = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("exp_package", "false");
//        //package not set
//        if (autostart_package.equals("false")) {
//            return;
//        }
//        //check if installed
//        if (!Common.getInstance(this).isInstalled(autostart_package)) {
//            SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(this).edit();
//            ed.putString("exp_package", "false");
//        }
//        //deal with delays
//        int delay = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("exp_delay", "2"));
//        //check if delay are >0
//        if (delay < 1) {
//            return;
//        }
//        autostart_timer = new Timer();
//        autostart_timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Log.i(TAG, "run: start from deeplink");
//
//                FakeStart.StartUsingDeepLink(getApplicationContext(), autostart_package);
//                autostart_timer.cancel();
////                autostart_timer = null;
//            }
//        }, delay * 1000L);
//    }

//    private static boolean flag_locked = false;
//
//    private boolean ConnectLockFlag = false;

    private void autolock() {
        if (!OpenProvider.isSupported(this)) {
            return;
        }
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("exp_autolock", false)) {
            return;
        }
//        Log.i(TAG, "autolock: "+OpenProvider.isConnected(this));
        if (!OpenProvider.isConnected(this)) {
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN);
        }
//
//        if (!OpenProvider.isConnected(this)) {
//            Log.i(TAG, "lock !");
//            performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN);
//        }
    }


//    AccessibilityNodeInfo tempnode;

//    Timer endCallTimer;

    @SuppressLint({"WrongConstant", "MissingPermission"})
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {


        String pkgName = accessibilityEvent.getPackageName().toString();
        int eventType = accessibilityEvent.getEventType();
        Log.i(TAG, "eventType: " + AccessibilityEvent.eventTypeToString(eventType) + " pkgName: " + pkgName);

        if (pkgName.equals("com.baidu.carlife")) {
            lifeCycle();
            autolock();
//            autostart();
            //phone


        }


//        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("exp_autolockbyphone", false)) {
//            if (pkgName.equals("com.samsung.android.incallui")) {
//                if (accessibilityEvent.getSource() == null) {
//                    return;
//                }
//                String tel = PreferenceManager.getDefaultSharedPreferences(this).getString("exp_autolockbyphone_number", "10086");
//
//                List<AccessibilityNodeInfo> list = accessibilityEvent.getSource().findAccessibilityNodeInfosByViewId("com.samsung.android.incallui:id/phone_number");
//                if (list.size() > 0) {
//                    String call = list.get(0).getText().toString();
//                    call = call.replace(" ", "");
//                    if (tel.equals(call)) {
//                        Log.i(TAG, "onAccessibilityEvent: find numeber");
////
//                        List<AccessibilityNodeInfo> but = accessibilityEvent.getSource().findAccessibilityNodeInfosByViewId("com.samsung.android.incallui:id/voice_disconnect_button");
//                        if (but.size() > 0) {
//                            performGlobalAction(AccessibilityService.GESTURE_DOUBLE_TAP);
//                            endCallTimer.schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    but.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                                }
//                            }, 0, 200);
//                        }
//                    }
//
//                }
//
//                List<AccessibilityNodeInfo> end = accessibilityEvent.getSource().findAccessibilityNodeInfosByViewId("com.samsung.android.incallui:id/call_state_label");
//                if (end.size() > 0) {
//                    if (end.get(0).getText().equals("通话已结束")) {
//                        endCallTimer.cancel();
//                        remove_CallPackage();
//                    }
//                }
////            switch (eventType) {
////                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
////
////                    break;
////                case AccessibilityEvent.TYPE_VIEW_CLICKED:
////
////                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
////                case AccessibilityEvent.TYPE_VIEW_FOCUSED:
////                    break;
////                case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
////                case AccessibilityEvent.TYPE_VIEW_SCROLLED:
////                    break;
////                case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
////                    break;
////            }
//            }
//        }
//

    }

    @Subscribe
    public void onReceive(Integer action) {
        switch (action) {
            case BACK:
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                break;
            case HOME:
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                break;
            case TASK:
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
                break;
        }
    }


    //helpers

//    private void Tap(int x, int y, long delay) {
//
//        Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Tap(x, y);
//                t.cancel();
//            }
//        }, delay);
//    }
//
//    private void Tap(int x, int y) {
//        Log.i(TAG, "模拟点击事件");
//
//        GestureDescription.Builder builder = new GestureDescription.Builder();
//        Path p = new Path();
//        p.moveTo(x, y);
//        builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 500L));
//        GestureDescription gesture = builder.build();
//        dispatchGesture(gesture, new GestureResultCallback() {
//            @Override
//            public void onCompleted(GestureDescription gestureDescription) {
//                super.onCompleted(gestureDescription);
//                tempnode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                Log.i(TAG, "onCompleted: 完成..........");
//            }
//
//            @Override
//            public void onCancelled(GestureDescription gestureDescription) {
//                super.onCancelled(gestureDescription);
//                Log.i(TAG, "onCancelled: 取消..........");
//            }
//        }, null);
//
//    }
//

//    public void remove_CallPackage() {
//        AccessibilityServiceInfo serviceInfo = getServiceInfo();
//        serviceInfo.packageNames = new String[]{"com.baidu.carlife"};
//        setServiceInfo(serviceInfo);
//    }

    public void add_CallPackage() {
        AccessibilityServiceInfo serviceInfo = getServiceInfo();
        serviceInfo.packageNames = new String[]{"com.baidu.carlife", "com.samsung.android.incallui"};
        setServiceInfo(serviceInfo);
        Log.i(TAG, "add_CallPackage: ");
    }





    /*  lifeCycle Service
     *  Establish Service When Connected to carlife
     */

    private boolean connection = false;

    private void lifeCycle() {
        if (OpenProvider.isConnected(this)) {
            xLog.log(TAG, "LifeCycle Check: Carlife Service On");
            if (!connection) {
                onConnected();
            }
        } else {
            xLog.log(TAG, "LifeCycle Check: Carlife Service Off");
            if (connection) {
                onDisconnected();
            }
        }
    }

    //    GodMode godMode;
    @SuppressLint("WrongConstant")
    private void onConnected() {
        xLog.log(TAG, "LifeCycle onConnected");
        connection = true;

        //clear activity on phone to avoid force stop
        ActivityManager.getInstance().clearOnScreen();

        //Start DeeplinkServiceContextHolder to init service
        String uri = "banqumusic://deeplinkservice/";
        Uri parse = Uri.parse("samsungcarlink://link/----" + uri);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(parse);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(0x10104000);
        startActivity(intent);

    }

    private void onDisconnected() {
        xLog.log(TAG, "LifeCycle Disconnected");

        if(DeepLinkService.getInstance()!=null)
        {
            DeepLinkService.getInstance().destroyAllService();
        }
        connection = false;
    }

    public void setConnection(boolean status) {
        connection = status;
    }
}
