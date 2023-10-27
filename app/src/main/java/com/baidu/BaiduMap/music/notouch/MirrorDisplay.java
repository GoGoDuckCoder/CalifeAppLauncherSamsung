package com.baidu.BaiduMap.music.notouch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.widget.Toast;

import com.baidu.BaiduMap.music.R;

public class MirrorDisplay extends AppCompatActivity {

    private static final String TAG = "ScreenCaptureFragment";

    private static final String STATE_RESULT_CODE = "result_code";
    private static final String STATE_RESULT_DATA = "result_data";

    public static final int REQUEST_MEDIA_PROJECTION = 1;

    private static int mScreenDensity;

    public static int mResultCode;
    public static Intent mResultData;

    private static Surface mSurface;
    private static MediaProjection mMediaProjection;
    private static VirtualDisplay mVirtualDisplay;
    public static MediaProjectionManager mMediaProjectionManager;
    private static SurfaceView mSurfaceView;

    public static MirrorDisplay mirrorDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mirrorDisplay = this;
        setContentView(R.layout.activity_test);

//        ActivityManager.getInstance().add(this);



        if (savedInstanceState != null) {
            mResultCode = savedInstanceState.getInt(STATE_RESULT_CODE);
            mResultData = savedInstanceState.getParcelable(STATE_RESULT_DATA);
        }

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView2);
        mSurface = mSurfaceView.getHolder().getSurface();
        DisplayMetrics metrics = new DisplayMetrics();
//        WindowManager wm = activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DisplayManager dm = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        dm.getDisplay(0).getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        //request permission and get mediaprojectionmanager on display 0


    }

    private void start_request_permission() {
        startService(new Intent(this, MediaService.class));
        ActivityOptions options = ActivityOptions.makeBasic();
        options.setLaunchDisplayId(0);
        Bundle optsBundle = options.toBundle();
        Intent i = new Intent(getApplicationContext(), RequestPermissionActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i, optsBundle);
    }

    private void start_request_permission_test()
    {
        startService(new Intent(this, MediaService.class));

        DisplayManager dm = (DisplayManager) getSystemService(DISPLAY_SERVICE);
        Display[] ds = dm.getDisplays();
        int sid = 0;
        if(ds.length>1)
        {
            sid= ds[1].getDisplayId();
        }

        ActivityOptions options = ActivityOptions.makeBasic();
        options.setLaunchDisplayId(sid);
        Bundle optsBundle = options.toBundle();
        Intent i = new Intent(getApplicationContext(), SlaveActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i, optsBundle);


        Intent launchIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.autonavi.amapauto");
//            launchIntent.addFlags(0x10104000);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(launchIntent,optsBundle);

    }
    public void request_permission()
    {
        Toast.makeText(getApplicationContext(),"running",Toast.LENGTH_LONG).show();
        startActivityForResult(
                mMediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_MEDIA_PROJECTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                Log.i(TAG, "User cancelled");
//                Toast.makeText(getActivity(), R.string.user_cancelled, Toast.LENGTH_SHORT).show();
                return;
            }
            Activity activity = this;
            if (activity == null) {
                return;
            }
            Log.i(TAG, "Starting screen capture");
            mResultCode = resultCode;
            mResultData = data;
            setUpMediaProjection();
            setUpVirtualDisplay();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mResultData != null) {
            outState.putInt(STATE_RESULT_CODE, mResultCode);
            outState.putParcelable(STATE_RESULT_DATA, mResultData);
        }
    }

    private void stopScreenCapture() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
        stopService(new Intent(this, MediaService.class));
//        mButtonToggle.setText(R.string.start);
    }

    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    public static void setUpMediaProjection() {
        mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);

    }

    public static void setUpVirtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                mSurfaceView.getWidth(), mSurfaceView.getHeight(), mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mSurface, null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        start_request_permission_test();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScreenCapture();
    }

    @Override
    protected void onDestroy() {
        tearDownMediaProjection();
//        ActivityManager.getInstance().remove(this);
        super.onDestroy();
    }
}