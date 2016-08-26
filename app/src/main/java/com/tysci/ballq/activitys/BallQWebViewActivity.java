package com.tysci.ballq.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.dialog.ImageUrlBrowserDialog;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.FilterBqUrlUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;

import java.lang.reflect.InvocationTargetException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HTT on 2016/7/15.
 *
 * @author LinDe edit
 */
public class BallQWebViewActivity extends BaseActivity implements View.OnLongClickListener, DownloadListener
{
    @Bind(R.id.pb_web)
    protected ProgressBar progressBar;
    @Bind(R.id.webView)
    protected WebView webView;

    private String url;
    private String title;

    private boolean isIndex;

    private boolean isDingDan;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_ballq_web_view;
    }

    @Override
    protected void initViews()
    {
        initWebViews();
    }

    @Override
    protected View getLoadingTargetView()
    {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent)
    {
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        isDingDan = intent.getBooleanExtra("is_ding_dan", false);
        KLog.e(title);
        KLog.e(url);

        isIndex = !TextUtils.isEmpty(url) && (url.contains("ballq/indiana/payment/success") || url.contains("ballq/indiana/order"));

        if (!TextUtils.isEmpty(title))
        {
            setTitle(title);
        }
        webView.loadUrl(url);

        if (isIndex)
        {
            titleBar.setTitleBarLeftIcon(R.mipmap.icon_back_gold, new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    notifyIndexFinish();
                }
            });
        }
    }

    private void initWebViews()
    {
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
        webView.setDownloadListener(this);
        webView.setOnLongClickListener(this);
    }

    @Override
    protected boolean isCanceledEventBus()
    {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState)
    {

    }

    @Override
    protected void handleInstanceState(Bundle savedInstanceState)
    {

    }

    @Override
    protected void onViewClick(View view)
    {

    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {
        if (action != null && action.equals("index") && !TextUtils.isEmpty(url) && !url.contains("ballq/indiana/index"))
        {
            finish();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (webView != null)
        {
            try
            {
                webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
                // webView.pauseTimers();
                //webView.stopLoading();
                //webView.onPause();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (webView != null)
        {
            webView.stopLoading();
            webView.clearCache(false);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public void onBackPressed()
    {
        if (webView.canGoBack())
        {
            webView.goBack();
        }
        else
        {
            finish();
        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        WebView wb;
        try
        {
            wb = (WebView) v;
        }
        catch (ClassCastException e)
        {
            return true;
        }
        if (wb == null)
            return true;
        WebView.HitTestResult result = wb.getHitTestResult();
        if (result.getType() == WebView.HitTestResult.IMAGE_TYPE
                || result.getType() == WebView.HitTestResult.IMAGE_ANCHOR_TYPE
                || result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE)
        {
            KLog.e("保存这个图片！" + result.getExtra());
            ImageUrlBrowserDialog dialog = new ImageUrlBrowserDialog(v.getContext());
            dialog.addUrl(result.getExtra());
            dialog.show();
        }
        return true;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
    {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private class CustomWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            KLog.d(url);
            return FilterBqUrlUtil.filterUrl(BallQWebViewActivity.this, url) || super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
        }

    }

    private class CustomWebChromeClient extends WebChromeClient
    {
        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            if (progressBar != null)
            {
                if (newProgress == 100)
                {
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    if (progressBar.getVisibility() == View.GONE)
                    {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title)
        {
            super.onReceivedTitle(view, title);
            titleBar.setTitleBarTitle(title);
            if (!isDingDan && title != null && title.equals("球商夺宝"))
            {
                Context context = BallQWebViewActivity.this;
                TextView tv = titleBar.getRightMenuTextView();
                int _10 = CommonUtils.dip2px(context, 10);
                int _5 = CommonUtils.dip2px(context, 5);
                tv.setPadding(_10, _5, _10, _5);
                //noinspection deprecation
                tv.setTextColor(getResources().getColor(R.color.gold));
                tv.setText("订单");
                tv.setTextSize(12F);
                tv.setBackgroundResource(R.drawable.btn_tra_gold);
                tv.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (!UserInfoUtil.checkLogin(BallQWebViewActivity.this))
                        {
                            UserInfoUtil.userLogin(BallQWebViewActivity.this);
                            return;
                        }
                        String url = HttpUrls.BQ_INDINAN_ORDER_NeedUserToken;
                        url += "?user=";
                        url += UserInfoUtil.getUserId(BallQWebViewActivity.this);
                        url += "&token=";
                        url += UserInfoUtil.getUserToken(BallQWebViewActivity.this);

                        Intent intent = new Intent(v.getContext(), BallQWebViewActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("title", "球商夺宝");
                        intent.putExtra("is_ding_dan", true);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode != KeyEvent.KEYCODE_BACK)
            return super.onKeyDown(keyCode, event);
        if (isIndex)
        {
            notifyIndexFinish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void notifyIndexFinish()
    {
        EventObject eventObject = new EventObject();
        eventObject.getData().putString("index", "index");
        eventObject.addReceiver(BallQWebViewActivity.class);
        EventObject.postEventObject(eventObject, "index");
    }
}
