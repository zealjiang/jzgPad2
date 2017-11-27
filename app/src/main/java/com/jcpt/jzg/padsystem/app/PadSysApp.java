package com.jcpt.jzg.padsystem.app;

import android.app.Application;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

import com.blankj.utilcode.utils.ScreenUtils;
import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.http.ApiServer;
import com.jcpt.jzg.padsystem.http.CustomerOkHttpClient;
import com.jcpt.jzg.padsystem.utils.ACache;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.vo.User;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jzg.lib.crash.JzgCrashHandler;
import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.eventbus.EventBus;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wangyd on 16/7/21.
 */
public class PadSysApp extends Application {
    private static int mMainThreadId = -1;
    private static Thread mMainThread;
    private static Handler mMainThreadHandler;
    private static Looper mMainLooper;
    private static PadSysApp app;
    private static ApiServer apiServer;
    //登录的用户对象，用get方法获取，如果返回为空，从Acache中获取
    private static User user;
    public static DetectionWrapper wrapper;  ///当前的配置方案
    public static String picDirPath;//当前任务下保存图片的地址 值：Constants.ROOT_DIR+ File.separator+taskId+File.separator

    public static boolean isRefresh = false;  //是否需要刷新列表

    public static Map<String, String> planPhotoIds = new HashMap<>();

    public static String taskId;

    /**
     * 判断是否有网络
     */
    public static boolean networkAvailable = true;
    /**
     * 判断是否是wifi还是移动网络
     */
    public static String netTypeName = "NONE";//   WIFI/MOBILE/NONE

    /**
     * 网络状态 分别代表wifi、wifi无网络、运营商网络
     */
    public enum NetStatus {
        WIFI,
        WIFI_NO_INTERNET,
        MOBILE_INTERNET
    }

    private NetworkInfo.State networkState;//因特网络状态

    @Override
    public void onCreate() {
        super.onCreate();
        //内存泄露
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        //LeakCanary.install(this);

        app = this;
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        getScreenSize();

        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        FrescoImageLoader.getSingleton().frescoInit();
        initApiServer();
        initNetworkStatusDetector();
        JzgCrashHandler.getInstance().init(this);//崩溃日志
        //初始化Logger日志
        Logger.init("PadSysApp").methodCount(3).logTool(new AndroidLogTool()); // custom log tool, optional
    }

    /**
     * 初始化网络连接
     */
    public void initApiServer() {
        OkHttpClient client = CustomerOkHttpClient.getClient();
        Retrofit retrofit = null;
        if (apiServer == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiServer.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiServer = retrofit.create(ApiServer.class);

        }
    }

    public static ApiServer getApiServer() {
        return apiServer;
    }


    /**
     * 初始化网络监听
     */
    public void initNetworkStatusDetector() {

        ReactiveNetwork.observeNetworkConnectivity(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Connectivity>() {
                    @Override
                    public void call(Connectivity connectivity) {
                        // do something with connectivity
                        // you can call connectivity.getState();
                        // connectivity.getType(); or connectivity.toString();
                        networkState = connectivity.getState();
                        netTypeName = connectivity.getTypeName();

                        LogUtil.e("network","网络监听: "+netTypeName+"   connectivity: "+networkState);

                        if (networkState == NetworkInfo.State.CONNECTED){
                            networkAvailable = true;
                        }else{
                            networkAvailable = false;
                        }
                    }
                });
    }


    public static PadSysApp getAppContext() {
        return app;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    /**
     * 获取屏幕宽高，返回 0 表示获取失败
     *
     * @return
     */
    public static int getScreenSize() {
        if (Constants.ScreenWidth == 0 || Constants.ScreenHeight == 0) {
            Constants.ScreenHeight = ScreenUtils.getScreenHeight(app);
            Constants.ScreenWidth = ScreenUtils.getScreenWidth(app);
        }

        //注意：横竖屏切换会导致ScreenWidth < ScreenHeight,因为是横屏显示，所以要用最大值做为宽度
        if (Constants.ScreenHeight > Constants.ScreenWidth) {
            int max = Constants.ScreenHeight;
            Constants.ScreenHeight = Constants.ScreenWidth;
            Constants.ScreenWidth = max;
        }

        // 获取屏幕密度（方法2）
        DisplayMetrics dm = new DisplayMetrics();
        dm = app.getResources().getDisplayMetrics();

        float density = Constants.density = dm.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）


        Log.i("PM", "W:  " + Constants.ScreenWidth + " H: " + Constants.ScreenHeight + "  density: " +
                "" + density + "   densityDPI:  " + densityDPI);

        return Constants.ScreenWidth * Constants.ScreenHeight;
    }

    /**
     * 获取用户对象
     *
     * @author zealjiang
     * @time 2016/6/28 11:05
     */
    public static User getUser() {
        if (user == null) {
            user = (User) ACache.get(app).getAsObject(Constants.KEY_ACACHE_USER);
        }
        return user;
    }

    public static void setUser(User user) {
        PadSysApp.user = user;
    }


}
