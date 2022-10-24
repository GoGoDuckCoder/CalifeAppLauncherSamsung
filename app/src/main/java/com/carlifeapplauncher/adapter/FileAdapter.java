package com.carlifeapplauncher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileAdapter {

    /*
     * Command
     * Album-0
     * Art-1
     */
    public static boolean saveAlbum(Context context, Bitmap bitmap) {
        return save(context, 0, bitmap);
    }
    public static boolean saveArt(Context context, Bitmap bitmap) {
        return save(context, 1, bitmap);
    }

    public static boolean saveFN(Context context, Bitmap bitmap) {
        return save(context, 9, bitmap);
    }

    public static Bitmap loadFN(Context context) {
        return load(context, 9);
    }
    public static Bitmap loadAlbum(Context context) {
        return load(context, 0);
    }

    public static Bitmap loadArt(Context context) {
        return load(context, 1);
    }

    private static boolean save(Context context, int command, Bitmap bitmap) {
        String path = context.getFilesDir() + "/" + command + ".png";//获取本地目录
        File file = new File(path);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Bitmap load(Context context, int command) {
        String path = context.getFilesDir() + "/" + command + ".png";//获取本地目录
        File a = new File(path);
        if (a.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        }
        return null;

    }
}
