package com.jcpt.jzg.padsystem.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wujj on 2017/2/20.
 * 邮箱：wujj@jingzhengu.com
 * 作用：//屏蔽recyclerView的滑动事件
 */

public class CustomRecyclerView extends RecyclerView {
    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }
}
