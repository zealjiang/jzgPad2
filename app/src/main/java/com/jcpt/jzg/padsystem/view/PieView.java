package com.jcpt.jzg.padsystem.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jcpt.jzg.padsystem.utils.LogUtil;


/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/12/11 15:26
 * @desc:
 */
public class PieView extends View {
    private static final String TAG = "PieView";
    private int COLORS[] = {
            Color.RED,Color.BLUE,Color.YELLOW,Color.GREEN,Color.MAGENTA,Color.YELLOW,Color.DKGRAY
    };
    private int mWidth = 0;
    private int mHeight = 0;
    private float startAngle = 0f;
    private int [] mData;
    private int r;
    private int total;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public void setmData(int[] mData) {
        this.mData = mData;
        if(mData!=null){
            for(int data:mData){
                total = data+total;
            }
        }
        invalidate();
    }

    public PieView(Context context) {
        this(context,null);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        LogUtil.e(TAG,"mWidth="+mWidth+",mHeight="+mHeight);
        r = Math.min(w,h)/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        canvas.drawCircle(0,0,r,mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(-r,-r,r,r);
        if(mData!=null){
            for(int i=0;i<mData.length;i++){
                mPaint.setColor(COLORS[i]);
                float sweepAngle = mData[i]*1.0f/total;
                sweepAngle = sweepAngle*360;
                canvas.drawArc(rectF,startAngle,sweepAngle,true,mPaint);
                startAngle+=sweepAngle;
                LogUtil.e(TAG,"startAngle="+startAngle+",sweepAngle="+sweepAngle);
            }
        }

    }
}
