package com.jcpt.jzg.padsystem.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.blankj.utilcode.utils.NetworkUtils;
import com.jcpt.jzg.padsystem.app.PadSysApp;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author: voiceofnet
 * email: 郑有权
 * @time: 2017/2/18 14:50
 * @desc:
 */
public class NetWorkTool {




    public static boolean isConnect(){
        if (NetworkUtils.isConnected(PadSysApp.getAppContext())){
            return true;
        }else{
            MyToast.showLong("没有网络");
            return false;
        }

    }
    public static boolean isConnect(String msg){
        if (NetworkUtils.isConnected(PadSysApp.getAppContext())){
            return true;
        }else{
            MyToast.showLong(msg);
            return false;
        }

    }


}
