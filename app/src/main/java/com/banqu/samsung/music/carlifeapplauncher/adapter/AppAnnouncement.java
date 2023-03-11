package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.banqu.samsung.music.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AppAnnouncement {

    public static void run(Context context) {
        if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("FirstRun",true))
        {
            return;
        }
//        View view = View.inflate(context, R.layout.lockstar, null);
//        final EditText id = (EditText) view.findViewById(R.id.lockstar_id);
//        id.setText(idx);
//        final EditText key = (EditText) view.findViewById(R.id.lockstar_key);
        AlertDialog dialog = new AlertDialog.Builder(context)
//                .setIcon(R.mipmap.icon)//设置标题的图片
                .setTitle("使用条款及免责声明")//设置对话框的标题
                .setMessage(R.string.about)
                .setCancelable(false)//不可取消
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //check
//                        if (checkKey(idx, key.getText().toString())) {
//                            ObjectOutputStream oss = null;
//                            try {
//                                oss = new ObjectOutputStream(context.openFileOutput("lockstar", context.MODE_PRIVATE));
//                                oss.writeObject(key.getText().toString());
//                                dialog.dismiss();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            Toast.makeText(context, "密钥错误请重新输入", Toast.LENGTH_SHORT).show();
//                        }
////                        String content = editText.getText().toString();
////                        dialog.dismiss();
//                    }
//                }).create();
                .setPositiveButton("同意并开始使用!", null).create();

//        try {
//            String read = "";
//            ObjectInputStream oss = new ObjectInputStream(context.openFileInput("AppAnnouncement"));
//            read = (String) oss.readObject();
//            oss.close();
//            if(read.equals(context.getString(R.string.about_version)))
//            {
//                return;
//            }
//        }
//        catch (Exception e)
//        {
//
//        }
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ObjectOutputStream oss = null;
//                try {
//                    String read = context.getString(R.string.about_version);
//                    oss = new ObjectOutputStream(context.openFileOutput("AppAnnouncement", context.MODE_PRIVATE));
//                    oss.writeObject(read);
//                    oss.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
                ed.putBoolean("FirstRun",false);
                ed.apply();
                dialog.dismiss();
            }
        });



    }



    public static  void runGuide(Context context)
    {
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


    public static  void runUpdate(Context context,View v)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Looper.prepare();
                try {
                    String url = "https://gogofactory.gitee.io/carlinkhelper/";
                    Connection connect = Jsoup.connect(url);//获取连接对象
                    Document document = connect.get();//获取url页面的内容并解析成document对象
                    String version = document.getElementById("version").text();
                    String updatenote = document.getElementById("updatenote").text().replace("#", "\n");

                    if (!version.equals(context.getString(R.string.about_version))) {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            public void run() {
                                v.setVisibility(View.VISIBLE);
                                v.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showupdate(context,version, updatenote);
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("TTTT", "jsoup:" + e);
                }
//                Looper.loop();
            }
        }).start();
    }

    public static  void runUpdate(Context context, Preference v)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Looper.prepare();
                try {
                    String url = "https://gogofactory.gitee.io/carlinkhelper/";
                    Connection connect = Jsoup.connect(url);//获取连接对象
                    Document document = connect.get();//获取url页面的内容并解析成document对象
                    String version = document.getElementById("version").text();
                    String updatenote = document.getElementById("updatenote").text().replace("#", "\n");

                    if (!version.equals(context.getString(R.string.about_version))) {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            public void run() {
                                v.getParent().setVisible(true);
                                v.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                                    @Override
                                    public boolean onPreferenceClick(@NonNull Preference preference) {
                                        showupdate(context,version, updatenote);
                                        return true;
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("TTTT", "jsoup:" + e);
                }
//                Looper.loop();
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
