package com.baidu.BaiduMap.music.carlifeapplauncher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.baidu.BaiduMap.music.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AppAnnouncement {

    public static void run(Context context) {
        if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("FirstRun", true)) {
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("使用条款及免责声明")//设置对话框的标题
                .setMessage(R.string.about)
                .setCancelable(false)//不可取消
                .setPositiveButton("同意并开始使用!", null).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
                ed.putBoolean("FirstRun", false);
                ed.apply();
                dialog.dismiss();
            }
        });


    }


    public static void runGuide(Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    String url = "https://gogofactory.gitee.io/carlinkhelper/";
                    Connection connect = Jsoup.connect(url);//获取连接对象
                    Document document = connect.get();//获取url页面的内容并解析成document对象
                    String notification = document.getElementById("notification").text().replace("#", "\n");

                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("使用说明")//设置对话框的标题
                            .setMessage(notification)
                            .setCancelable(true)
                            .setPositiveButton("好的", null).create();
                    dialog.show();
                } catch (Exception e) {
                    Log.i("TTTT", "jsoup:" + e);
                }
                Looper.loop();
            }
        }).start();
    }

    public static void runUpdate(Context context, Preference v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "https://gogofactory.gitee.io/carlinkhelper/";
                    Connection connect = Jsoup.connect(url);//获取连接对象
                    Document document = connect.get();//获取url页面的内容并解析成document对象
                    String version = document.getElementById("version_samsung_xc").text();
                    String updatenote = document.getElementById("updatenote_samsung_xc").text().replace("#", "\n");

                    if (!version.equals(context.getString(R.string.about_version))) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                v.getParent().setVisible(true);
                                v.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                                    @Override
                                    public boolean onPreferenceClick(@NonNull Preference preference) {
                                        showupdate(context, version, updatenote);
                                        return true;
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("TTTT", "jsoup:" + e);
                }
            }
        }).start();
    }

    private static void showupdate(Context context, String version, String note) {
        Log.i("TTTT", "shownote:");
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("版本更新：" + version)//设置对话框的标题
                .setMessage(note)
                .setCancelable(true)//不可取消
                .setPositiveButton("更新", null)
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://pan.baidu.com/s/1dcSRUyaK7LwVvEObSxWTPQ?pwd=8888"));
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
