package com.baidu.BaiduMap.music.notouch;


import static android.content.ContentValues.TAG;
import static com.baidu.BaiduMap.music.notouch.MirrorDisplay.REQUEST_MEDIA_PROJECTION;
import static com.baidu.BaiduMap.music.notouch.MirrorDisplay.mMediaProjectionManager;
import static com.baidu.BaiduMap.music.notouch.MirrorDisplay.mResultCode;
import static com.baidu.BaiduMap.music.notouch.MirrorDisplay.mResultData;
import static com.baidu.BaiduMap.music.notouch.MirrorDisplay.setUpMediaProjection;
import static com.baidu.BaiduMap.music.notouch.MirrorDisplay.setUpVirtualDisplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.Log;

public class RequestPermissionActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (mMediaProjectionManager == null) {
            mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }
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
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

}