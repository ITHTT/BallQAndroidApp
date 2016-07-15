package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQEventsPlazaActivity extends BaseActivity{
    @Bind(R.id.swipeRefresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.webView)
    protected WebView webView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_evnets_plaza;
    }

    @Override
    protected void initViews() {
        setTitle("活动广场");
        initWebViews();
        getDatas();
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    private void initWebViews() {
        WebSettings webSettings = webView.getSettings();
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);
        if (Build.VERSION.SDK_INT >= 19) {
            //webSettings.setBlockNetworkImage(false);
        }
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        //webView.addJavascriptInterface(new JavascriptInterface(this.getActivity()), "infohandler");
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
    }

    private void getDatas() {
        setRefreshing();
        String url = HttpUrls.HOST_URL + "/weixin/events/lobby";
        if (UserInfoUtil.checkLogin(this)) {
            String userId=UserInfoUtil.getUserId(this);
            String token=UserInfoUtil.getUserToken(this);
            url=url+"?user="+userId+"&token="+token;
        }
        KLog.e("url:" + url);
        if(!url.contains("?")){
            try {
                //url=url+"?"+BigDataUtil2.getBaseStatisticsParams(getActivity(),99,"2:6",1,"0","0","0","0","0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                //url=url+BigDataUtil2.getBaseStatisticsParams(getActivity(),99,"2:6",1,"0","0","0","0","0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        webView.loadUrl(url);
    }

    private void setRefreshing() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void onRefreshCompelete() {
        swipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        }, 1000);
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

    private boolean filterRequestUrl(String url){
        KLog.e("Url:"+url);
        if(url.contains("ballqinapp://event/signing")){
            if (UserInfoUtil.checkLogin(this)) {
                String userId=UserInfoUtil.getUserId(this);
                String token=UserInfoUtil.getUserToken(this);
                String urlStr= HttpUrls.HOST_URL+"/weixin/events/show_lottery/?user="+userId+"&token="+token;
                try {
                    //urlStr=urlStr+"&"+BigDataUtil2.getBaseStatisticsParams(getActivity(),99,"2:6:1",1,"0","0","0","0","0");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                KLog.e("urlStr:"+urlStr);
                String title="活动签到";
                Intent intent=new Intent(this, BallQWebViewActivity.class);
                intent.putExtra("url",urlStr);
                intent.putExtra("title",title);
                startActivity(intent);
            }else{
                UserInfoUtil.userLogin(this);
            }
            return true;
        }else if(!url.contains(HttpUrls.HOST_URL + "/weixin/events/lobby")){
            String urlStr=url;
            KLog.e("url:"+url);
            if(UserInfoUtil.checkLogin(this)){
                String userId=UserInfoUtil.getUserId(this);
                String token=UserInfoUtil.getUserToken(this);
                String[] urls=urlStr.split("\\?");
                urlStr=urls[0]+"?user="+userId+"&token="+token;
            }
            if(!urlStr.contains("?")){
                try {
                   // urlStr=urlStr+"?"+BigDataUtil2.getBaseStatisticsParams(getActivity(),99,"2:6:1",1,"0","0","0","0","0");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    //urlStr=urlStr+"&"+BigDataUtil2.getBaseStatisticsParams(getActivity(),99,"2:6:1",1,"0","0","0","0","0");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String title="球商";
            Intent intent=new Intent(this, BallQWebViewActivity.class);
            KLog.e("strUrl:" + urlStr);
            intent.putExtra("url",urlStr);
            intent.putExtra("title",title);
            startActivity(intent);
            return true;
        }else{
            return false;
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            KLog.e("Url:" + url);
            return filterRequestUrl(url)||super.shouldOverrideUrlLoading(view, url);
            // return super.shouldOverrideUrlLoading(view,url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(swipeRefresh!=null) {
                onRefreshCompelete();
            }
            //view.loadUrl(getOnClickJs());
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {

    }
}
