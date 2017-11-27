package com.jcpt.jzg.padsystem.ui.fragment;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.CarConfigSubAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.CarConfigSubModel;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.detection.ConfigureList;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.StringUtils;

public class WebViewFragment extends BaseFragment {

    @BindView(R.id.webCheckReport)
    public WebView webview;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.tvRefresh)
    TextView tvRefresh;

    private String url = "";

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //2
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        ButterKnife.bind(this, view);

        setWebView();
        if(!StringUtils.isEmpty(url)) {
            loadurl(url);
        }
        return view;
    }


    @Override
    protected void setView() {
        //3
    }
    @Override
    protected void initData() {

    }

    /**
     * 设置WebView
     */
    public void setWebView() {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setDomStorageEnabled(true);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);// 使用当前WebView处理跳转
                return false;// true表示此事件在此处被处理，不需要再广播
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }
        });

        //设置加载进度显示
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }else{
                    if(progressBar.getVisibility()==View.GONE){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
                    webview.goBack();
                    return true;
                }
                return false;
            }
        });

    }

    @OnClick({R.id.tvRefresh})
    void onClick(View view){
        switch (view.getId()){
            case R.id.tvRefresh:
                loadurl(url);
                break;
        }
    }

    private void loadurl(String url){
        if(webview != null){
            if(!PadSysApp.networkAvailable){
                MyToast.showShort("没有网络");
                tvRefresh.setVisibility(View.VISIBLE);
                return;
            }
            tvRefresh.setVisibility(View.GONE);
            webview.loadUrl(url);
        }
    }

    /**
     * 设置Url
     * @param url
     */
    public void setUrl(String url){
        if(StringUtils.isEmpty(url)){
            return;
        }
        if(!url.equals(this.url)){
            this.url = url;
            loadurl(url);
        }
    }

}
