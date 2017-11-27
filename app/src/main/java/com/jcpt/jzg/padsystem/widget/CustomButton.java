package com.jcpt.jzg.padsystem.widget;/**
 * Created by voiceofnet on 2016/6/21.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.jcpt.jzg.padsystem.R;


/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/6/21 19:54
 * @desc:
 */
public class CustomButton extends Button {

    private int mWidth, mHeight;
    private int mPressedColor, mUnPressedColor;
    private int mRoundRadius, mBtnRadius;
    private int mShapeType;
    private Paint mPaint;
    private Paint textPaint;
    private RectF mRectF;
    public CustomButton(Context context){
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs){
        if (isInEditMode()){
            return;
        }
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        mPressedColor = typedArray.getColor(R.styleable.CustomButton_pressed_color,
                getResources().getColor(R.color.colorPrimary));
        mUnPressedColor = typedArray.getColor(R.styleable.CustomButton_unpressed_color,
                getResources().getColor(R.color.colorPrimary));
        mShapeType = typedArray.getInt(R.styleable.CustomButton_shape_type, 1);
        mRoundRadius = typedArray.getDimensionPixelSize(R.styleable.CustomButton_round_radius,
                getResources().getDimensionPixelSize(R.dimen.round_radius));
        /*mBtnRadius = typedArray.getDimensionPixelSize(R.styleable.CustomButton_btn_radius,
                getResources().getDimensionPixelSize(R.dimen.btn_radius));*/
        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setColor(mUnPressedColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        this.setWillNotDraw(false);
        this.setDrawingCacheEnabled(true);
        this.setClickable(true);
        this.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mRectF = new RectF();

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mBtnRadius = mWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null){
            return;
        }
        if (mShapeType == 0){// draw cirle button
            canvas.drawCircle(mWidth / 2, mHeight / 2, mBtnRadius, mPaint);
        }else{// draw rectangle button
            mRectF.set(0, 0, mWidth, mHeight);
            canvas.drawRoundRect(mRectF, mRoundRadius, mRoundRadius, mPaint);
        }
        String text = getText().toString();
        int textColor = getCurrentTextColor();
        float textSize = getTextSize();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        float baseline = (mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2;
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, mRectF.centerX(), baseline, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPaint.setColor(mPressedColor);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mPaint.setColor(mUnPressedColor);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }
}

