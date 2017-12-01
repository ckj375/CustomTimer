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
import android.view.MotionEvent;
import android.view.View;

public class CircleTimerView extends View {

    private static float mStrokeSize = 4;
    private static final float mStep = 0.5f;

    private int xCenter;
    private int yCenter;
    private float radius;// 圆环半径
    private RectF mArcRect = new RectF();
    private Paint mFirstPaint = new Paint();
    private Paint mSecondPaint = new Paint();
    private Paint mThirdPaint = new Paint();
    private Bitmap progressMark;
    private int angle = 0;
    private int bgColor, startColor, endColor, mWhiteColor;
    private SweepGradient mGradientColor;

    /**
     * Listener
     */
    public interface OnScrollListener {
        void onScroll(int angle);
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
        mStrokeSize = resources.getDimension(R.dimen.circlestopwatch_bg_circle_size);

        startColor = resources.getColor(R.color.lightSkyBlu);
        endColor = resources.getColor(R.color.red);
        mGradientColor = new SweepGradient(xCenter, yCenter,
                new int[]{startColor, endColor, startColor}, null);
        bgColor = resources.getColor(R.color.gray);
        mWhiteColor = resources.getColor(R.color.white);

        mFirstPaint.setShader(mGradientColor);
        mFirstPaint.setStyle(Paint.Style.STROKE);
        mFirstPaint.setStrokeWidth(mStrokeSize);
        mFirstPaint.setAntiAlias(true);

        mSecondPaint.setColor(bgColor);
        mSecondPaint.setStyle(Paint.Style.STROKE);
        mSecondPaint.setStrokeWidth(mStrokeSize);
        mSecondPaint.setAntiAlias(true);

        mThirdPaint.setColor(mWhiteColor);
        mThirdPaint.setStyle(Paint.Style.STROKE);
        mThirdPaint.setStrokeWidth(mStrokeSize);
        mThirdPaint.setAntiAlias(true);

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
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(-90, xCenter, yCenter);
        // 渐变色圆环
        canvas.drawArc(mArcRect, 0, 360, false, mFirstPaint);

        // 灰色圆弧
        if (angle % 6 == 0) {
            canvas.drawArc(mArcRect, angle, 360 - angle, false, mSecondPaint);
        }

        //白色刻度
        for (float i = 5.7f; i < 360; i += 6f) {
            canvas.drawArc(mArcRect, i, mStep, false, mThirdPaint);
        }

        canvas.restore();

        canvas.save();
        canvas.rotate(angle, canvas.getWidth() / 2, canvas.getHeight() / 2);
        // 转盘箭头
        canvas.drawBitmap(progressMark, canvas.getWidth() / 2 - progressMark.getWidth() / 2,
                canvas.getHeight() / 2 - radius - progressMark.getHeight() / 2, null);
        canvas.restore();

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moved(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                moved(x, y);
                break;
            case MotionEvent.ACTION_UP:
                moved(x, y);
                break;
        }
        return true;
    }

    /**
     * Moved.
     *
     * @param x  the x
     * @param y  the y
     */
    private void moved(float x, float y) {
        float degrees = (float) ((float) ((Math.toDegrees(Math.atan2(
                x - xCenter, yCenter - y)) + 360.0)) % 360.0);
        // and to make it count 0-360
        if (degrees < 0) {
            degrees += 2 * Math.PI;
        }

        int mAngle = Math.round(degrees);
        int step = mAngle / 6;
        if (mAngle - 6 * step > 0) {
            step++;
        }
        mAngle = 6 * step;

        setAngle(mAngle);
        mScrollListener.onScroll(mAngle);
        invalidate();
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

}
