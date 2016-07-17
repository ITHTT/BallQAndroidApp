package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.utils.UserInfoUtil;

import java.lang.reflect.InvocationTargetException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQWebViewActivity extends BaseActivity{
    @Bind(R.id.pb_web)
    protected ProgressBar progressBar;
    @Bind(R.id.webView)
    protected WebView webView;

    private String url;
    private String title;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_web_view;
    }

    @Override
    protected void initViews() {
        initWebViews();

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        webView.loadUrl(url);
    }

    private void initWebViews() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setLoadWithOverviewMode(true);
        //webView.addJavascriptInterface(new JavascriptInterface(this.getActivity()), "infohandler");
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle outState) {

    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            try {
                webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
                // webView.pauseTimers();
                //webView.stopLoading();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (webView != null) {
            webView.stopLoading();
            webView.clearCache(false);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    private boolean filterUrl(String url) {
        if (url.equals("ballqinapp://login")) {
            UserInfoUtil.userLogin(this);
            return true;
        }
        return false;
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return filterUrl(url) || super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

        }
    }

    private class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressBar != null) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
