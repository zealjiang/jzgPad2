package com.jcpt.jzg.padsystem.widget.nodoubleclick;

import android.view.View;

import java.util.Calendar;

/**
 * Created by wujj on 2017/1/9.
 * 邮箱：wujj@jingzhengu.com
 * 作用：防止重复点击
 */

public abstract class NoDoubleOnclickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
    public abstract void onNoDoubleClick(View v);
}
