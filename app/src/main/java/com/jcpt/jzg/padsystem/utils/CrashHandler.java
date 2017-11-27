package com.jcpt.jzg.padsystem.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告. 需要在Application中注册，为了要在程序启动器就监控整个程序。
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    /** TAG */
    public static final String              TAG       = "CrashHandler";
    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /** CrashHandler实例 */
    private static CrashHandler             mCrashHandler;
    /** 程序的Context对象 */
    private Context mContext;
    /** 用来存储设备信息和异常信息 */
    private Map<String, String> infos     = new HashMap<String, String>();
    /** 用于格式化日期,作为日志文件名的一部分 */
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 私有构造函数
     */
    private CrashHandler() {

    }

    /**
     * 获取CrashHandler实例 ,单例模式
     *
     * @return
     * @since V1.0
     */
    public static CrashHandler getInstance() {
        if (mCrashHandler == null)
            mCrashHandler = new CrashHandler();
        return mCrashHandler;
    }

    /**
     * 初始化
     *
     * @param context
     * @since V1.0
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     * @since V1.0
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        // 收集设备参数信息
        collectDeviceInfo(mContext);

        // 保存日志
        String error = saveCatchInfo(ex);

        //友盟
        MobclickAgent.reportError(mContext,error);
        MobclickAgent.reportError(mContext,ex);

        killCurrentProcess();
        return true;
    }

    private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     * @since V1.0
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(TAG, "collectDeviceInfo() an error occured when collect package info NameNotFoundException:"+e.getMessage());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                LogUtil.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                LogUtil.e(TAG, "collectDeviceInfo() an error occured when collect crash info Exception:"+e.getMessage());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     */
    private String saveCatchInfo(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        sb.append(ex.getMessage());
        return sb.toString();
    }


}
