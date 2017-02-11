package com.library.beeloadingview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

/**
 * Created by xiangcheng on 17/1/16.
 */

public class BeeLoadingView extends FrameLayout {
    private static final String TAG = BeeLoadingView.class.getSimpleName();

    private Bitmap[] bitmaps;
    private float loadSize;
    private int animateStyle;
    private int startValue;
    private ValueAnimator colorEvaluator;
    private RotateAnimation ra;
    private int[] colorArray = new int[7];
    private boolean bitmapStyle;
    /**
     * 旋转的速度
     */
    private int rotateDuration;
    /**
     * 切换颜色的速度
     */
    private int changeDuration;


    public BeeLoadingView(Context context) {
        this(context, null);
    }

    public BeeLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initArgus(context, attrs);
        initView(getContext());
        loadStyle();
    }

    private void initArgus(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BeeLoadingView);
        loadSize = array.getFloat(R.styleable.BeeLoadingView_load_size, 0);
        //默认是颜色改变的loading
        animateStyle = array.getInt(R.styleable.BeeLoadingView_animate, 1);
        rotateDuration = array.getInt(R.styleable.BeeLoadingView_rotate_duration, 2000);
        changeDuration = array.getInt(R.styleable.BeeLoadingView_change_duration, 2000);
        bitmapStyle = array.getBoolean(R.styleable.BeeLoadingView_bitmapStyle, false);
    }

    private void loadStyle() {
        if (animateStyle == 1) {
            //以颜色改变的方式来loading
            if (!bitmapStyle) {
                initColor();
                refreshColor();
            }
            initColorAnimation();
            startColorLoading();
        } else if (animateStyle == 2) {
            //以旋转的方式来loading
            if (!bitmapStyle) {
                initColor();
                refreshColor();
            }
            initRotateAnimation();
            ra.start();
        } else {
            //既改变颜色而且带有旋转的动画
            if (!bitmapStyle) {
                initColor();
                refreshColor();
            }
            initColorAnimation();
            initRotateAnimation();
            startColorLoading();
            ra.start();
        }
    }

    private void initRotateAnimation() {
        ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(rotateDuration);
        ra.setFillAfter(true);
        ra.setRepeatCount(Integer.MAX_VALUE);
        ra.setRepeatMode(ValueAnimator.RESTART);
        ra.setInterpolator(new LinearInterpolator());
        setAnimation(ra);
    }

    private void initColor() {
        colorArray[0] = Color.parseColor("#FFFFCC");
        colorArray[1] = Color.parseColor("#FFCC00");
        colorArray[2] = Color.parseColor("#CC9909");
        colorArray[3] = Color.parseColor("#CC9999");
        colorArray[4] = Color.parseColor("#FFFF33");
        colorArray[5] = Color.parseColor("#FF6666");
        colorArray[6] = Color.parseColor("#FF0000");
    }

    private void initColorAnimation() {
        colorEvaluator = ValueAnimator.ofInt(0, 6);
        colorEvaluator.setDuration(changeDuration);
        colorEvaluator.setRepeatCount(Integer.MAX_VALUE);
        colorEvaluator.setRepeatMode(ValueAnimator.RESTART);
        colorEvaluator.setInterpolator(new LinearInterpolator());
        colorEvaluator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.d(TAG, "valueAnimator:" + valueAnimator.getAnimatedValue());
                int value = (int) valueAnimator.getAnimatedValue();
                //其实这里判断不严谨，因为这里的值变化可能不是很匀称地变化了,所以会导致有一闪一闪的错觉
                if (startValue != value) {
                    if (!bitmapStyle) {
                        int length = colorArray.length;
                        int temp = 0;
                        temp = colorArray[length - 1];
                        for (int j = length - 2; j >= 0; j--) {
                            colorArray[j + 1] = colorArray[j];
                        }
                        colorArray[0] = temp;
                        refreshColor();
                    } else {
                        int length = bitmaps.length;
                        Bitmap temp;
                        temp = bitmaps[length - 1];
                        for (int j = length - 2; j >= 0; j--) {
                            bitmaps[j + 1] = bitmaps[j];
                        }
                        bitmaps[0] = temp;
                        refreshBitmap();
                    }
                    startValue = value;
                }
            }
        });
    }

    /**
     * 刷新图片
     *
     * @param bitmaps
     */
    public void setBitmaps(Bitmap[] bitmaps) {
        if (bitmaps != null && bitmapStyle) {
            if (bitmaps.length != 7) {
                throw new IllegalArgumentException("bitmaps length must be 7");
            }
            this.bitmaps = bitmaps;
            for (int i = 0; i < getChildCount(); i++) {
                ((BeeView) getChildAt(i)).setBitmap(bitmaps[i]);
            }
        } else {
            throw new IllegalArgumentException("bitmaps must be not null and bitmapStyle must be true");
        }
    }

    private void refreshColor() {
        for (int i = 0; i < getChildCount(); i++) {
            BeeView bv = (BeeView) getChildAt(i);
            Log.d(TAG, "colorArray---" + colorArray[i]);
            bv.setColor(colorArray[i]);
        }
    }

    private void refreshBitmap() {
        for (int i = 0; i < getChildCount(); i++) {
            BeeView bv = (BeeView) getChildAt(i);
            bv.setBitmap(bitmaps[i]);
        }
    }

    private void initView(final Context context) {
        for (int i = 0; i < 7; i++) {
            BeeView iv;
            if (loadSize == 0) {
                iv = new BeeView(context);
            } else {
                if (bitmaps != null && bitmaps.length > 0) {
                    iv = new BeeView(context, loadSize, bitmaps[i]);
                } else {
                    iv = new BeeView(context, loadSize);
                }

            }
            addView(iv);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed)
            layoutChildren(left, top, right, bottom);
    }

    void layoutChildren(int left, int top, int right, int bottom) {
        final int count = getChildCount();

        if (count == 7) {
            for (int i = 0; i < count - 1; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    final int width = child.getMeasuredWidth();
                    final int height = child.getMeasuredHeight();

                    int childLeft;
                    int childTop;

                    double angle = i * 60 * Math.PI / 180;
                    int r = width * 17 / 18;
//                    int r = (int) (width * 0.866025);
                    Log.d(TAG, "width:" + width);
                    Log.d(TAG, "r:" + r);
                    //如果是r越小的话，此时得到的childLeft会导致i从0到3的时候会越大，4到5的时候会越小

                    childLeft = getWidth() / 2 - (int) (r * Math.sin(angle)) - r / 2;
                    childTop = getWidth() / 2 - (int) (r - r * Math.cos(angle)) + r / 2;
                    Log.d(TAG, "第" + i + "个childLeft:" + childLeft + ",childTop:" + childTop);

                    child.layout(childLeft, childTop, childLeft + width, childTop + height);
                }
            }
            final View child = getChildAt(6);
            if (child.getVisibility() != GONE) {
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                int childLeft;
                int childTop;

                int r = width * 17 / 18;
//                int r = (int) (width * 0.866025);
                childLeft = getWidth() / 2 - r / 2;
                childTop = getWidth() / 2 - r / 2;

                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

    /**
     * 通过改变颜色来实现loading
     */
    private void startColorLoading() {
        colorEvaluator.start();
    }

    public void stop() {
        if (colorEvaluator != null && colorEvaluator.isRunning()) {
            colorEvaluator.cancel();
        }
        if (ra != null) {
            ra.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int paddingHorizontal = getPaddingLeft() + getPaddingRight();
        int paddingVertical = getPaddingTop() + getPaddingBottom();
        float childMax = ((BeeView) getChildAt(0)).getItemWidth() * 0.866f * 3;
        float childMin = (float) (((BeeView) getChildAt(0)).getItemWidth() * 1.0 / 2);
        //计算孩子的最大的边
        float childMaxSide = (float) Math.sqrt(childMax * childMax + childMin * childMin);
        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            if (widthSize < childMaxSide + paddingHorizontal) {
                widthSize = (int) (childMaxSide + paddingHorizontal);
            }
        } else {
            widthSize = (int) (childMaxSide + paddingHorizontal);
        }

        if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            if (heightSize < childMaxSide + paddingVertical) {
                heightSize = (int) (childMaxSide + paddingVertical);
            }
        } else {
            heightSize = (int) (childMaxSide + paddingVertical);
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
    }
}
