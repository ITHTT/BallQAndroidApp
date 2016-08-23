package com.tysci.ballq.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.tysci.ballq.dialog.ImageUrlBrowserDialog;

/**
 * Created by HTT on 2016/6/6.
 */
public class WebViewUtil
{
    public static WebView getHtmlWebView(Context context, String html)
    {
        if (!TextUtils.isEmpty(html))
        {
            final String mimeType = "text/html"; // 超文本标记语言
            final String encoding = "UTF-8";
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            WebView webView = new WebView(context);
            webView.setLayoutParams(layoutParams);
//        webView.setBackgroundColor(Color.parseColor("#00000000"));
            WebSettings webSettings = webView.getSettings();
            //noinspection deprecation
            webSettings.setPluginState(WebSettings.PluginState.ON);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setSupportZoom(false);
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webSettings.setDefaultTextEncodingName(encoding);
            webSettings.setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            webSettings.setBlockNetworkImage(false);
//            webView.loadData(html, mimeType, encoding);
            webView.loadDataWithBaseURL("fake://not/needed", html, mimeType, encoding, null);
//            webView.loadUrl(html);
            webView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    KLog.e("长按点击");
                    WebView wb;
                    try
                    {
                        wb = (WebView) v;
                    }
                    catch (Exception e)
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
            });
            return webView;
        }
        return null;
    }

    private static String getHtmlData(String bodyHTML)
    {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "<style>" +
                "@font-face {" +
                "font-family: \"MyFont\";" +
                "src: url('file:///android_asset/FZLTKH.TTF');" +
                "}" +
                "p { font-family:\"MyFont\"; font-size:18px; line-height:1.5;}" +
                "</style>" +
                "</head>";
        return "<html>" + head + "<body style=\"text-align: justify;word-break: break-all;\">" + bodyHTML + "</body></html>";
    }
}
