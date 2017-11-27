package com.jcpt.jzg.padsystem.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.http.ApiServer;
import com.jcpt.jzg.padsystem.image.util.FileUtils;
import com.jcpt.jzg.padsystem.mvpview.ILogin;
import com.jcpt.jzg.padsystem.presenter.LoginPresenter;
import com.jcpt.jzg.padsystem.utils.ACache;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.User;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;

import java.io.File;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseActivity implements ILogin {

    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    CustomRippleButton btnLogin;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.rlLogin)
    RelativeLayout rlLogin;
    @BindView(R.id.cBoxAutoLogin)
    CheckBox cBoxAutoLogin;


    private LoginPresenter presenter;
    private String userName;
    private String password;
    private Unbinder unbinder;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        initListener();//--Created by wujj on 2016/12/12.
    }

    /**
     * --Created by wujj on 2016/12/12.
     * 点击空白部分隐藏软键盘
     */
    private void initListener() {
        rlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.hideInputMethodManager(LoginActivity.this);
            }
        });

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                btnLogin.performClick();
                return false;
            }
        });
    }

    @Override
    protected void setData() {
        init();
    }


    public void init() {

        String versionName = AppUtils.getAppInfo(this.getApplicationContext()).getVersionName();
        tvVersionName.setText(versionName);

        //获取帐号
        userName = ACache.get(this.getApplicationContext()).getAsString("loginId");
        //获取上次登录成功的时间
        Object objectT = ACache.get(this.getApplicationContext()).getAsObject("loginTime");
        if (objectT != null) {
            long lastLoginTime = (long) objectT;
            //如果当前时间大于上次登录时间超过3天，清空登录密码
            long day = (System.currentTimeMillis() - lastLoginTime) / 1000 / (24 * 60 * 60);
            if (day > 3) {
                ACache.get(this.getApplicationContext()).put("pwd", "");
            } else {
                password = ACache.get(this.getApplicationContext()).getAsString("pwd");
            }
        }

        if (TextUtils.isEmpty(userName)) {
            userName = "";
            //弹出软键盘
            KeyboardUtils.showSoftInput(this.getApplicationContext(), etAccount);
        } else if (TextUtils.isEmpty(password)) {
            password = "";
            KeyboardUtils.showSoftInput(this.getApplicationContext(), etPassword);
        } else {
            btnLogin.setFocusable(true);
            btnLogin.setFocusableInTouchMode(true);
            btnLogin.requestFocus();
            btnLogin.findFocus();
        }

        etAccount.setText(userName);
        etPassword.setText(password);
        presenter = new LoginPresenter(this);
    }

    @OnClick({R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                login();
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        if (!check(true)) {
            return;
        }
        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (!PadSysApp.networkAvailable) {
            MyToast.showShort("没有网络");
            return;
        }
        presenter.login(account, password, true);
    }

    /**
     * 检验用户名和密码是否为空
     *
     * @return 都不为空返回true，反之返回false
     */
    private boolean check(boolean showError) {
        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            if (showError)
                etAccount.setError(getString(R.string.username_cannot_be_null));
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            if (showError)
                etPassword.setError(getString(R.string.pwd_cannot_be_null));
            return false;
        }
        return true;
    }

    @Override
    public void succeed(User user) {
        //清空临时图片文件
        FileUtils.deleteDirectory(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.TEMP_TAKE_PHOTO_DIR));
        //保存用户对象到JzgApp
        PadSysApp.setUser(user);
        //保存用户对象到ACache中
        ACache.get(PadSysApp.getAppContext()).put(Constants.KEY_ACACHE_USER, user);
        //保存登录帐号和密码到ACache对象中
        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        ACache.get(this.getApplicationContext()).put("loginId", account);
        ACache.get(this.getApplicationContext()).put("pwd", password);
        ACache.get(this.getApplicationContext()).put("loginTime", System.currentTimeMillis());
        jump(WelcomeActivity.class, true);

        //极光推送，设置别名和标签
        HashSet<String> tagSet = new HashSet<>();
        //线上地址只添加线上标签
        if(ApiServer.BASE_URL.equals("http://jiancepadtwo.guchewang.com")){
            tagSet.add(user.getUserId()+"");
        }else{
            tagSet.add("testnew"+user.getUserId()+"");
            tagSet.add(etAccount.getText().toString().trim());
        }

        JPushInterface.setAlias(this.getApplicationContext(),100,user.getUserId()+"");
        JPushInterface.setTags(this.getApplicationContext(),100,tagSet);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
