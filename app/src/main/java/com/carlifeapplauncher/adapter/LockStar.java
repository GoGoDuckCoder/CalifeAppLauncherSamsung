package com.carlifeapplauncher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.banqu.samsung.music.R;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

public class LockStar {


    public static void run(Context context) {
        //Load
        String id = DeviceUtils.getUniqueId(context);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("marginx", "1");
        editor.apply();

        try {
            String lockstar = "";
            ObjectInputStream oss = new ObjectInputStream(context.openFileInput("lockstar"));
            lockstar = (String) oss.readObject();
            String key = lockstar.substring(0, 32);
            String value = lockstar.substring(32);
            oss.close();
            //has key check key
            if (!checkKey2(key, value)) {
                    showdiag(context, key);
            }
        } catch (Exception e) {
            //no key
            showdiag(context, id);
        }
    }

    private static void showdiag(Context context, String idx) {
        View view = View.inflate(context, R.layout.lockstar, null);
        final EditText id = (EditText) view.findViewById(R.id.lockstar_id);
        id.setText(idx);
        final EditText key = (EditText) view.findViewById(R.id.lockstar_key);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("")
                .setView(view)
                .setCancelable(false)
                .setNegativeButton("体验10分钟",null)
                .setPositiveButton("注册", null).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkKey2(idx, key.getText().toString())) {
                    ObjectOutputStream oss = null;
                    try {
                        oss = new ObjectOutputStream(context.openFileOutput("lockstar", context.MODE_PRIVATE));
                        oss.writeObject(idx + (key.getText().toString()));
                        oss.close();
                        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        ed.putBoolean("exp",false);
                        ed.apply();
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                }

            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"启用车机界面开始计时，结束时将抹除所有设置，可再次体验！",Toast.LENGTH_LONG).show();
                SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
                ed.putBoolean("exp",true);
                ed.apply();
                dialog.dismiss();
            }
        });
        Toast.makeText(context,"请使用已保存的激活码激活 或 在VIP互动群中@群主索取激活码",Toast.LENGTH_LONG).show();
    }

    private static Boolean checkKey(String id, String key) {
        try {
            if (DeviceUtils.toMD5(id + "gaodongjia").equals(key))
                return true;
        } catch (Exception e) {
        }
        return false;
    }

    private static Boolean checkKey2(String id, String key) {
        try {
            if (DeviceUtils.toMD5(id + "pojiesiquanjia").equals(key))
                return true;
        } catch (Exception e) {
        }
        return false;
    }
}