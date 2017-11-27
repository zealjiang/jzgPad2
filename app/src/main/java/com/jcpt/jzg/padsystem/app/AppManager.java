/**
 * Project Name:JZGPingGuShi
 * File Name:AppManager.java
 * Package Name:com.gc.jzgpinggushi.app
 * Date:2014-9-1上午10:18:48
 * Copyright (c) 2014, wangyd523@gmail.com All Rights Reserved.
 */

package com.jcpt.jzg.padsystem.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.jcpt.jzg.padsystem.ui.activity.DialogActivity;
import com.jcpt.jzg.padsystem.ui.activity.LoginActivity;

import java.util.Stack;

/**
 * ClassName:AppManager <br/>
 * Date: 2014-9-1 上午10:18:48 <br/>
 *
 * @author 汪渝栋
 * @version
 * @since JDK 1.6
 * @see
 */
public class AppManager {
    private static Stack<Activity> activityStack;

    private static AppManager instance;

    private AppManager() {
    }

    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    public synchronized void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public void clearActivitys(){
        if(activityStack!=null){
            activityStack.clear();
        }
    }


    //显示全局Dialog
    public static void showGlobalDialog(String content){
        Activity activity = AppManager.getAppManager().currentActivity();
        if(!activity.getClass().getName().equals(LoginActivity.class.getName())) {
            Intent intent = new Intent(activity, DialogActivity.class);
            intent.putExtra("content", content);
            activity.startActivity(intent);
        }
    }
}
