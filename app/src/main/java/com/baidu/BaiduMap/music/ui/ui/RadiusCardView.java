package com.baidu.BaiduMap.music.ui.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;

import com.baidu.BaiduMap.music.R;

import androidx.cardview.widget.CardView;

/**
 * Created by jingzz on 2020/3/20.
 */
public class RadiusCardView extends CardView {
 
    private float tlRadiu;
    private float trRadiu;
    private float brRadiu;
    private float blRadiu;
 
    public RadiusCardView(Context context) {
        this(context, null);
    }
 
    public RadiusCardView(Context context, AttributeSet attrs) {
        this(context, attrs, com.google.android.material.R.attr.materialCardViewStyle);
    }
 
    public RadiusCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRadius(0);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RadiusCardView);
        tlRadiu = array.getDimension(R.styleable.RadiusCardView_rcv_topLeftRadiu, 0);
        trRadiu = array.getDimension(R.styleable.RadiusCardView_rcv_topRightRadiu, 0);
        brRadiu = array.getDimension(R.styleable.RadiusCardView_rcv_bottomRightRadiu, 0);
        blRadiu = array.getDimension(R.styleable.RadiusCardView_rcv_bottomLeftRadiu, 0);
//        setBackground(new ColorDrawable());
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        RectF rectF = getRectF();
        float[] readius = {tlRadiu,tlRadiu,trRadiu,trRadiu,brRadiu,brRadiu,blRadiu,blRadiu};
        path.addRoundRect(rectF,readius,Path.Direction.CW);
        canvas.clipPath(path,Region.Op.INTERSECT);
        super.onDraw(canvas);
    }
 
    private RectF getRectF() {
        Rect rect = new Rect();
        getDrawingRect(rect);
        RectF rectF = new RectF(rect);
        return rectF;
    }
}