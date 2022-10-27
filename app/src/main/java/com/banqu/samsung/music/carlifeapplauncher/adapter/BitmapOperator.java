package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import androidx.core.content.ContextCompat;

public class BitmapOperator {
    public static byte[] Bitmap2Bytes(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray();

    }

    // 如果是drawable，可以先转成bitmap，下面是代码：

    public static byte[] drawable2Bytes(Drawable drawable) {

        if (drawable == null) {

            return null;

        }

        Bitmap bitmap = drawableToBitmap(drawable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray();

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap

                .createBitmap(

                        drawable.getIntrinsicWidth(),

                        drawable.getIntrinsicHeight(),

                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                                : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

// canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }
    public static Bitmap decodeBitmapFromResource(Context context, int drawableId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static  boolean saveBackground(Context context, Bitmap bg)
    {
        try {
            String path2 = context.getFilesDir() + "/bg.png";//获取本地目录
            File file2 = new File(path2);
            OutputStream outputStream2 = new FileOutputStream(file2);
            bg.compress(Bitmap.CompressFormat.PNG, 100, outputStream2);
            outputStream2.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
