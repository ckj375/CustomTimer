package com.ckj.customtimer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

public class CircleTimerView extends View {

    private int mWhiteColor;
    private static float mStrokeSize = 4;
    private static float mBgStrokeSize = 4;
    private Paint mBgPaint = new Paint();
    private final Paint mPaint = new Paint();
    private final RectF mArcRect = new RectF();
    private float radius;
    private static final float mStep = 0.5f;

    private int bgColor, startColor, endColor;
    private SweepGradient mGradientColor;
    private int xCenter;
    private int yCenter;

    private int degree = 0;

    public CircleTimerView(Context context) {
        this(context, null);
    }

    public CircleTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    private void init(Context c) {
        Resources resources = c.getResources();
        mStrokeSize = resources.getDimension(R.dimen.circlestopwatch_circle_size);
        mBgStrokeSize = resources.getDimension(R.dimen.circlestopwatch_bg_circle_size);
        radius = resources.getDimension(R.dimen.circlestopwatch_circle_radius);

        bgColor = resources.getColor(R.color.gray);
        startColor = resources.getColor(R.color.lightSkyBlu);
        endColor = resources.getColor(R.color.blue);
        mGradientColor = new SweepGradient(xCenter, yCenter,
                new int[]{startColor, endColor, startColor}, null);

        mBgPaint.setShader(mGradientColor);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(mBgStrokeSize);
        mBgPaint.setAntiAlias(true);

        mPaint.setColor(bgColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBgStrokeSize);

        mWhiteColor = resources.getColor(R.color.white);
//        mGrayColor = resources.getColor(R.color.circle_color_grey);
//        mBlueColor = resources.getColor(R.color.circle_color_blue);
//        mFill.setAntiAlias(true);
//        mFill.setStyle(Paint.Style.FILL);
//        mFill.setColor(mBlueColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);

        xCenter = mWidth / 2;
        yCenter = mHeight / 2;

        mArcRect.top = yCenter - radius;
        mArcRect.bottom = yCenter + radius;
        mArcRect.left = xCenter - radius;
        mArcRect.right = xCenter + radius;
//
//        mBackColor = new SweepGradient(xCenter, yCenter,
//                new int[]{startColor, endColor, startColor}, null);
//
//        mBackPaint.setShader(mBackColor);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(-90, xCenter, yCenter);
        canvas.drawArc(mArcRect, 0, 360, false, mBgPaint);
//
//            int degree = (int) ((mCurrentIntervalTime / 1000) * 6);
//            while (degree > 360) {
//                degree -= 360;
//            }

        mPaint.setColor(bgColor);
        if (degree % 6 == 0) {
            canvas.drawArc(mArcRect, degree, 360 - degree, false, mPaint);
        }

        //白色刻度
        mPaint.setColor(mWhiteColor);
        for (float i = 5.7f; i < 360; i += 6f) {
            canvas.drawArc(mArcRect, i, mStep, false, mPaint);
        }

        canvas.restore();
//        }
//        if (mAnimate) {
            invalidate();
//        }
    }

}
