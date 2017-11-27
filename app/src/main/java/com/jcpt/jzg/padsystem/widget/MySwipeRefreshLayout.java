package com.jcpt.jzg.padsystem.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by libo on 2016/12/28.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 刷新时拦截事件 不传递 给 recyclerView 以免引起刷新过程中 滑动recyclerView引起的角标越界异常
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (super.isRefreshing()) {//如果是刷新状态拦截事件
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
