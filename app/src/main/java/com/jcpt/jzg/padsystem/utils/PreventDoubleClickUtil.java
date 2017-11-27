package com.jcpt.jzg.padsystem.utils;

import java.util.Calendar;

/**
 * Created by zyq on 2017/3/6.
 */

public class PreventDoubleClickUtil {
    private static long lastClickTime = 0;
    public static final int MIN_CLICK_DELAY_TIME = 3000;

    //防双击
    public static boolean noDoubleClick() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return true;
        } else {
            return false;
        }
    }


}
