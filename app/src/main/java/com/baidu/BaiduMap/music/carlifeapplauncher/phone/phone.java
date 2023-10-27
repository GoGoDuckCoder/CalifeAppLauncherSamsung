package com.baidu.BaiduMap.music.carlifeapplauncher.phone;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class phone {


    public static boolean ensurePermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
            Log.i("CHECK", "ensurePermission: 无权限");
            //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
//            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG}, 1);
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return false;
        } else {
            Log.i("CHECK", "ensurePermission: 有权限");
            return true;
        }


//        if (context.checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
//            Log.i("CHECK", "ensurePermission: 无权限");
//            Intent intent = new Intent(Intent.ACTION_ANSWER);
////        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
////        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "activity device");
//            context.startActivity(intent);
//            return false;
//        }
//        else
//        {
//            Log.i("CHECK", "ensurePermission: 有权限");
//            return true;
//        }

    }
}
