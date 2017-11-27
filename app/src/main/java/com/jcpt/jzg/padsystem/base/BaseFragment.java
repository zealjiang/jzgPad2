package com.jcpt.jzg.padsystem.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.ShowDialogTool;
import com.umeng.analytics.MobclickAgent;


public abstract class BaseFragment extends Fragment implements IBaseView{
    protected String TAG = BaseFragment.this.getClass().getSimpleName();
    protected BaseActivity context;
    private ShowDialogTool showDialogTool;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (BaseActivity) getActivity();
        showDialogTool = new ShowDialogTool();
        initData();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initViews(inflater, container, savedInstanceState);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
    }

    /***
     * run in onActivityCreated
     */
    protected abstract void setView();

    /***
     * run in onCreateView
     */
    protected abstract View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /***
     * run in onCreate
     */
    protected abstract void initData();

    protected void jump(Class dest) {
        Intent intent = new Intent(getActivity(), dest);
        startActivity(intent);
    }

    protected void jump(Intent intent) {
        startActivity(intent);
    }

    public void showLoading() {
        showDialogTool.showLoadingDialog(context);
    }

    public void hideLoading() {
        showDialogTool.dismissLoadingDialog();
    }

    /**
     * 显示错误信息
     *
     * @param error 错误信息
     */
    @Override
    public void showError(String error) {
        LogUtil.e(TAG,error);
        if (!TextUtils.isEmpty(error))
            MyToast.showLong(error);
    }

    /**
     * 显示加载
     */
    @Override
    public void showDialog() {
        showDialogTool.showLoadingDialog(context);
    }

    /**
     * 关闭加载
     */
    @Override
    public void dismissDialog() {
        showDialogTool.dismissLoadingDialog();
    }

}
