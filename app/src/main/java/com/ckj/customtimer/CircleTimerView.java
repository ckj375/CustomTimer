package com.ckj.customtimer;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CircleTimerView extends View {

    private int mWhiteColor;
    private static float mStrokeSize = 4;
    private static float mBgStrokeSize = 4;
    /**
     * 圆圈半径
     */
    private float radius;
    private final RectF mArcRect = new RectF();
    private Paint mBgPaint = new Paint();
    private final Paint mPaint = new Paint();
    private Bitmap progressMark;

    private int angle = 0;
    private boolean IS_PRESSED = false;

    private static final float mStep = 0.5f;

    private int bgColor, startColor, endColor;
    private SweepGradient mGradientColor;
    private int xCenter;
    private int yCenter;

    /**
     * Listener
     */
    public interface OnScrollListener {
        public void onScroll(int angle);
    }

    public OnScrollListener mScrollListener;

    public void setScrollListener(OnScrollListener mListener) {
        this.mScrollListener = mListener;
    }

    public CircleTimerView(Context context) {
        this(context, null);
    }

    public CircleTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.custom_timer_view);
        radius = array.getDimension(R.styleable.custom_timer_view_radius, 20);
        init(context);
    }

    private void init(Context c) {
        Resources resources = c.getResources();
        mStrokeSize = resources.getDimension(R.dimen.circlestopwatch_circle_size);
        mBgStrokeSize = resources.getDimension(R.dimen.circlestopwatch_bg_circle_size);

        bgColor = resources.getColor(R.color.gray);
        startColor = resources.getColor(R.color.lightSkyBlu);
        endColor = resources.getColor(R.color.red);
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

        progressMark = BitmapFactory.decodeResource(c.getResources(),
                R.drawable.mark);
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
        if (angle % 6 == 0) {
            canvas.drawArc(mArcRect, angle, 360 - angle, false, mPaint);
        }

        //白色刻度
        mPaint.setColor(mWhiteColor);
        for (float i = 5.7f; i < 360; i += 6f) {
            canvas.drawArc(mArcRect, i, mStep, false, mPaint);
        }

        canvas.restore();

        canvas.save();
        canvas.rotate(angle, canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.drawBitmap(progressMark, canvas.getWidth() / 2 - progressMark.getWidth() / 2,
                canvas.getHeight() / 2 - radius - progressMark.getHeight() / 2, null);
        canvas.restore();
//        }
//        if (mAnimate) {
        invalidate();
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        boolean up = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_MOVE:
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_UP:
                up = true;
                moved(x, y, up);
                break;
        }
        return true;
    }

    /**
     * Moved.
     *
     * @param x  the x
     * @param y  the y
     * @param up the up
     */
    private void moved(float x, float y, boolean up) {
        float distance = (float) Math.sqrt(Math.pow((x - xCenter), 2)
                + Math.pow((y - yCenter), 2));
        if (/*distance < outerRadius + adjustmentFactor
                && distance > innerRadius - adjustmentFactor &&*/ !up) {
            IS_PRESSED = true;

            float degrees = (float) ((float) ((Math.toDegrees(Math.atan2(
                    x - xCenter, yCenter - y)) + 360.0)) % 360.0);
            // and to make it count 0-360
            if (degrees < 0) {
                degrees += 2 * Math.PI;
            }

//            markPointX = (float) (cx + outerRadius
//                    * Math.sin(getAngle() * Math.PI / 180));
//            markPointY = (float) (cy - outerRadius
//                    * Math.cos(getAngle() * Math.PI / 180));

            int mAngle = Math.round(degrees);
            int step = mAngle / 6;
            if (mAngle - 6 * step > 0) {
                step++;
            }
            mAngle = 6 * step;

            setAngle(mAngle);
            mScrollListener.onScroll(mAngle);
            invalidate();
        } else {
            IS_PRESSED = false;
        }
    }

    public void setAngle(int angle) {
        this.angle = angle;
//        float donePercent = (((float) this.angle) / 360) * 100;
//        float progress = (donePercent / 100) * getMaxProgress();
//        setProgressPercent(Math.round(donePercent));
//        CALLED_FROM_ANGLE = true;
//        setProgress(Math.round(progress));
    }

}
