package com.baidu.BaiduMap.music.carlifeapplauncher.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFileByUri {
    public static boolean getPath(Context context, Uri srcUri) {
        try {
//            String path = context.getFilesDir() + "/bg";//获取本地目录
//            File file = new File(path);
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);//context的方法获取URI文件输入流
            if (inputStream == null)
                return false;
//            OutputStream outputStream = new FileOutputStream(file);
//            copyStream(inputStream, outputStream);//调用下面的方法存储
//            inputStream.close();
//            outputStream.close();

            //
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            String path2 = context.getFilesDir() + "/bg.png";//获取本地目录
            File file2 = new File(path2);
            OutputStream outputStream2 = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, outputStream2);
            outputStream2.close();
            return true;//成功返回路径
        } catch (Exception e) {
           return false;
        }


    }

    private static void copyStream(InputStream input, OutputStream output) {//文件存储
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}