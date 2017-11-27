package com.jcpt.jzg.padsystem.ui.activity;


import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.update.UpdateManager;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.PermissionHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 郑有权 on 2016/10/31.
 * 欢迎界面
 */
public class WelcomeActivity extends BaseActivity{

    @BindView(R.id.tvTaskClaim)
    TextView tvTaskClaim;

    private UpdateManager updateManager;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏应用程序的标题栏，即当前activity的label   
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏  
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        LogUtil.e(TAG,Constants.ROOT_DIR);
        //软件升级
        updateManager = new UpdateManager();

        if (Build.VERSION.SDK_INT >= 23) {
            applyPermission();
        } else {
            FileUtils.createOrExistsDir(Constants.ROOT_DIR);
            if (PadSysApp.networkAvailable) {
                updateManager.checkAppUpdate(this,false);
            }
        }

        //如果是超级用户就隐藏任务认领安钮
        if(PadSysApp.getUser().getIsSuperAppraiser()==1){
            tvTaskClaim.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setData() {
        init();
    }


    public void init(){

    }


    @OnClick({R.id.tvTaskClaim,R.id.tvMyTask})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tvTaskClaim:
              jump(TaskClaimActivity.class,true);
            break;
            case R.id.tvMyTask:
              jump(MainActivity.class,true);
            break;
        }
    }

    private void applyPermission() {
        PermissionHelper.getInstance().requestPermission(this,new PermissionHelper.RequestResultListener() {
            @Override
            public void onResult(boolean result) {
                if(result){
                    if (PadSysApp.networkAvailable) {
                       // updateManager.checkAppUpdate(WelcomeActivity.this,false);
                        FileUtils.createOrExistsDir(Constants.ROOT_DIR);
                    }
                }
            }
        },Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


}
