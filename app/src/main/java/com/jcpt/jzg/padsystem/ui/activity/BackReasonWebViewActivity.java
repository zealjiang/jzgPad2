package com.jcpt.jzg.padsystem.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackReasonWebViewActivity extends BaseActivity {

    @BindView(R.id.webview)
    public WebView webview;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.rl_rootView)
    RelativeLayout rlRootView;
    @BindView(R.id.refresh)
    RelativeLayout refresh;
    @BindView(R.id.rLayoutCheckReportBack)
    RelativeLayout rLayoutCheckReportBack;

    private String url = "";
    private String stitle = "";

    @Override
    protected void initViews(Bundle savedInstanceState) {

        url = getIntent().getStringExtra("url");
        stitle = getIntent().getStringExtra("title");

        setContentView(R.layout.activity_back_reason_webview);
        ButterKnife.bind(this);
    }

    @Override
    protected void setData() {
        init();
    }

    public void init() {
        rLayoutCheckReportBack.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(stitle)) {
            title.setText(stitle);
        }
        setWebView();
        loadurl(url);
    }

    /**
     * 设置WebView
     */
    @SuppressLint("JavascriptInterface")
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

/*            @Override
            public void onPageFinished(WebView view, String url) {
                //webview.loadUrl("javascript:MyApp.resize(document.body.getBoundingClientRect().height)");
                super.onPageFinished(view, url);
            }*/

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
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
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
        webview.addJavascriptInterface(this, "jzg");

    }

    @JavascriptInterface
    public void closeWebView() {
        Intent intent = new Intent();
        intent.putExtra("isBackSucceed", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void loadurl(String url) {
        if (webview != null) {
            if (!PadSysApp.networkAvailable) {
                MyToast.showShort("没有网络");
                finish();
                return;
            }
            webview.loadUrl(url);
        }
    }


    @OnClick({R.id.refresh})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                loadurl(url);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
