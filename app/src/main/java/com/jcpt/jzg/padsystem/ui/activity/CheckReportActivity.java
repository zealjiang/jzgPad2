package com.jcpt.jzg.padsystem.ui.activity;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckReportActivity extends BaseActivity {

    @BindView(R.id.webCheckReport)
    public WebView webview;

    @BindView(R.id.title)
    public TextView title;

    private String url = "";
    private String stitle = "";

    @Override
    protected void initViews(Bundle savedInstanceState) {

        url = getIntent().getStringExtra("url");
        stitle = getIntent().getStringExtra("title");

        setContentView(R.layout.activity_check_report);
        ButterKnife.bind(this);
    }

    @Override
    protected void setData() {
        init();
    }

    public void init() {

        if (!TextUtils.isEmpty(stitle)) {
            title.setText(stitle);
        }
        setWebView();

        if(url.indexOf(":")==4){
            url = url.replaceFirst("http","https");
        }
        loadurl(url);
//        loadurl("http://192.168.0.140:8081/Report/pad2.aspx?userId=10&taskId=26960");

    }

    /**
     * 设置WebView
     */
    public void setWebView() {
        WebSettings webSettings = webview.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本

        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
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
            public void onPageFinished(WebView view, String url) {
                webview.loadUrl("javascript:MyApp.resize(document.body.getBoundingClientRect().height)");
                dismissDialog();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //super.onReceivedSslError(view, handler, error);
                handler.proceed();
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

    @JavascriptInterface
    public void resize(final float height) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getResources()
                        .getDisplayMetrics().widthPixels, (int) (height * getResources()
                        .getDisplayMetrics().density));
                params.setMargins(15, 0, 10, 0);
                webview.setLayoutParams(params);
            }
        });
    }

    public void loadurl(String url) {
        if (webview != null) {
            webview.loadUrl(url);
            showDialog();
            webview.reload();
        }
    }


    @OnClick({R.id.rLayoutCheckReportBack})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rLayoutCheckReportBack:
                finish();
                break;
        }
    }

}

