package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;

import java.lang.reflect.InvocationTargetException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQWebViewActivity extends BaseActivity
{
    @Bind(R.id.pb_web)
    protected ProgressBar progressBar;
    @Bind(R.id.webView)
    protected WebView webView;

    private String url;
    private String title;

    private boolean isIndex;

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
        KLog.e(title);
        KLog.e(url);

        isIndex = !TextUtils.isEmpty(url) && url.contains("index?");

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
    protected void handleInstanceState(Bundle outState)
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
        if (action != null && action.equals("index"))
        {
            finish();
        }
    }

    @Override
    protected void onResume() {
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

    private boolean filterUrl(String url)
    {
        if (url.equals("ballqinapp://login"))
        {
            UserInfoUtil.userLogin(this);
            return true;
        } else if (url.contains("ballqinapp://event/signing")){
            if (UserInfoUtil.checkLogin(this))
            {
                String userId = UserInfoUtil.getUserId(this);
                String token = UserInfoUtil.getUserToken(this);
                String urlStr = HttpUrls.HOST_URL + "/weixin/events/show_lottery/?user=" + userId + "&token=" + token;
                try
                {
                    //urlStr=urlStr+"&"+BigDataUtil2.getBaseStatisticsParams(getActivity(),99,"2:6:1",1,"0","0","0","0","0");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                KLog.e("urlStr:" + urlStr);
                String title = "活动签到";
                Intent intent = new Intent(this, BallQWebViewActivity.class);
                intent.putExtra("url", urlStr);
                intent.putExtra("title", title);
                startActivity(intent);
            }
            else
            {
                UserInfoUtil.userLogin(this);
            }
            return true;
        }else if (!url.contains(HttpUrls.HOST_URL + "/weixin/events/lobby")) {
            String urlStr = url;
            KLog.e("url:" + url);
            if (UserInfoUtil.checkLogin(this))
            {
                String userId = UserInfoUtil.getUserId(this);
                String token = UserInfoUtil.getUserToken(this);
                String[] urls = urlStr.split("\\?");
                urlStr = urls[0] + "?user=" + userId + "&token=" + token;
            }
            if (!urlStr.contains("?"))
            {
                try
                {
                    // urlStr=urlStr+"?"+BigDataUtil2.getBaseStatisticsParams(getActivity(),99,"2:6:1",1,"0","0","0","0","0");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    //urlStr=urlStr+"&"+BigDataUtil2.getBaseStatisticsParams(getActivity(),99,"2:6:1",1,"0","0","0","0","0");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            String title = "球商";
            Intent intent = new Intent(this, BallQWebViewActivity.class);
            KLog.e("strUrl:" + urlStr);
            intent.putExtra("url", urlStr);
            intent.putExtra("title", title);
            startActivity(intent);
            return true;
        }
        else
        {
            return false;
        }
    }

    private class CustomWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return filterUrl(url) || super.shouldOverrideUrlLoading(view, url);
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
