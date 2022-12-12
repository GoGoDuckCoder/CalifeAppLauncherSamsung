package com.banqu.samsung.music.ui.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class RadiusImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Path path;
 
    public RadiusImageView(Context context) {
        super(context);
        init(context);
    }
 
    public RadiusImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
 
    public RadiusImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
 
    private void init(Context context) {
        path = new Path();
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        float rx = width * 0.5f;    // 半径为宽度二分之一
        float ry = height * 0.5f;   // 半径为高度二分之一
 
        // 正方形经处理后变为圆形
 
        path.reset();
 
        // 四个点位，如果半径小，则点位还需另行计算
        path.moveTo(0.0f, height * 0.5f);
        path.lineTo(width * 0.5f, 0.0f);
        path.lineTo(width, height * 0.5f);
        path.lineTo(width * 0.5f, height);
        path.lineTo(0.0f, height * 0.5f);
 
        path.addRoundRect(0.0f, 0.0f, width, height, rx, ry, Path.Direction.CW);
 
        canvas.clipPath(path);
 
        super.onDraw(canvas);
    }
}