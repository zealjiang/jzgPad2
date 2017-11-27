package com.jcpt.jzg.padsystem.global;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.jcpt.jzg.padsystem.app.PadSysApp;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zealjiang on 2017/11/24 11:48.
 * Email: zealjiang@126.com
 */

public class ScreenUnlockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if((intent != null && Intent.ACTION_USER_PRESENT.equals(intent.getAction()))){
            requestPost();
        }
    }

    private void requestPost() {
        new Thread(runnable).start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String baseUrl = "https://www.baidu.com/";

                // 新建一个URL对象
                URL url = new URL(baseUrl);
                // 打开一个HttpURLConnection连接
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                // 设置连接超时时间
                urlConn.setConnectTimeout(5 * 1000);
                //设置从主机读取数据超时
                urlConn.setReadTimeout(5 * 1000);
                // Post请求必须设置允许输出 默认false
                urlConn.setDoOutput(true);
                //设置请求允许输入 默认是true
                urlConn.setDoInput(true);
                // Post请求不能使用缓存
                urlConn.setUseCaches(false);
                // 设置为Post请求
                urlConn.setRequestMethod("GET");
                //设置本次连接是否自动处理重定向
                urlConn.setInstanceFollowRedirects(true);
                // 配置请求Content-Type
                urlConn.setRequestProperty("Content-Type", "application/json");
                // 开始连接
                urlConn.connect();
                // 判断请求是否成功
                if (urlConn.getResponseCode() == 200) {
                    handler.sendEmptyMessage(0);
                }
                // 关闭连接
                urlConn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 获取返回的数据
            PadSysApp.networkAvailable = true;
        }
    };
}
