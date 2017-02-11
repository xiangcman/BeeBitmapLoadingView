package com.library.beeloadingview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;

/**
 * Created by xiangcheng on 17/1/16.
 */

public class BeeView extends View {

    private static final int DEFAULT_POLYGON = 6;
    private Path mPath;
    private PointF[] mPoint;
    //用最长的宽来表示view的宽度，从第六个点到第三个点
    private float size = 170.0f;
    private Paint mPaint;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    private Bitmap bitmap;

    private int color = Color.parseColor("#E91E63");

    public void setColor(int color) {
        this.color = color;
        mPaint.setColor(color);
        invalidate();
    }

    public BeeView(Context context) {
        super(context);
        init();
    }

    public BeeView(Context context, float size) {
        super(context);
        this.size = size;
        init();
    }

    public BeeView(Context context, float size, Bitmap bitmap) {
        super(context);
        this.size = size;
        this.bitmap = bitmap;
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        mPoint = new PointF[DEFAULT_POLYGON];
        //这里的0.866约等于2分之根号3
        mPoint[0] = new PointF(size / 4, 0);
        mPoint[1] = new PointF(size * 3 / 4, 0);
        mPoint[2] = new PointF(size, size * 0.866f / 2);
        mPoint[3] = new PointF(size * 3 / 4, size * 0.866f);
        mPoint[4] = new PointF(size / 4, size * 0.866f);
        mPoint[5] = new PointF(0, size * 0.866f / 2);
        mPath.moveTo(mPoint[0].x, mPoint[0].y);
        mPath.lineTo(mPoint[1].x, mPoint[1].y);
        mPath.lineTo(mPoint[2].x, mPoint[2].y);
        mPath.lineTo(mPoint[3].x, mPoint[3].y);
        mPath.lineTo(mPoint[4].x, mPoint[4].y);
        mPath.lineTo(mPoint[5].x, mPoint[5].y);
        mPath.lineTo(mPoint[0].x, mPoint[0].y);

        mPath.close();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) {
            canvas.drawPath(mPath, mPaint);
        } else {

            canvas.save();
            canvas.clipPath(mPath);
            /**这里如果不进行*缩放的话，会导致绘制的时候会很卡顿，因此这里做了一个整体缩小的动作*/
            canvas.drawBitmap(scaleBitmap(bitmap), 0, 0, null);
            canvas.restore();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) getItemWidth(), (int) getItemWidth());
    }

    public float getItemWidth() {
        return mPoint[2].x;
    }

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin 原图
     * @return new Bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) getItemWidth()) / width;
        float scaleHeight = ((float) getItemWidth()) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        return newBM;
    }

}
