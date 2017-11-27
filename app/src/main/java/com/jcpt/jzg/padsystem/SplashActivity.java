package com.jcpt.jzg.padsystem;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.FileUtils;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.ui.activity.LoginActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SplashActivity extends BaseActivity {
    private final String TAG = "SplashActivity";
    private Subscription subscribe;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏应用程序的标题栏，即当前activity的label   
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏  
        setContentView(R.layout.activity_splash);
        mySharedPreferences  = getSharedPreferences("APP", Activity.MODE_PRIVATE);


        //editor  = mySharedPreferences.edit();

        //editor.remove("isFirst");//删除上一个版本的isFirst
        //editor.commit();
        editor  = mySharedPreferences.edit();
        boolean isFirstOpenAPP = mySharedPreferences.getBoolean("isFirstVersionCode5",true);
        if (isFirstOpenAPP) {
            if (Build.VERSION.SDK_INT >= 23) {
                RxPermissions rxPermissions = new RxPermissions(this);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            //deletePlans(editor);
                            //deleteJzgPad2Dir(editor);
                            wait2s();
                        } else {
                            MyToast.showShort("需要获取SD卡读取权限来保存图片");
                            wait2s();
                        }
                    }
                });
            } else {
                //deletePlans(editor);
                //deleteJzgPad2Dir(editor);
                wait2s();
            }

        }else {
            wait2s();
        }
    }

    @Override
    protected void setData() {

    }

    /**
     * Created by 李波 on 2016/12/20.
     * 如果是APP是第一次打开就删除一次JzgPad2目录，相当于变向实现卸载时删除目录。
     */
    private void deleteJzgPad2Dir(SharedPreferences.Editor editor) {
        FileUtils.deleteDir(Constants.ROOT_DIR);
        editor.putBoolean("isFirstVersionCode5",false);
        editor.commit();
    }

    /**
     * 由于版本升级带的方案变更，在安装新版本后要删除旧的方案
     */
    private void deletePlans(SharedPreferences.Editor editor){
        //获取版本信息
        AppUtils.AppInfo appInfo = AppUtils.getAppInfo(this.getApplicationContext());
        if(appInfo!=null){
            String versionName = appInfo.getVersionName();
            int versionCode = appInfo.getVersionCode();

            if("1.0.2".equals(versionName)&&5==versionCode){
                DBManager.getInstance().delete(Constants.DATA_TYPE_PLAN);

                editor.putBoolean("isFirstVersionCode5",false);
                editor.commit();
            }
        }
    }

    private void wait2s(){
        subscribe = Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }

    /**
     * Created by wujj on 2016/12/12.
     * 按返回键解除订阅，解决在2S内按返回键依然会跳到登录页面的问题
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(subscribe==null){
            return;
        }
        subscribe.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscribe==null){
            return;
        }
        subscribe.unsubscribe();
    }

}
