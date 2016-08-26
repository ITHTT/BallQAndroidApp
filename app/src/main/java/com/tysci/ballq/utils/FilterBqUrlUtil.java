package com.tysci.ballq.utils;

import android.app.Activity;
import android.content.Intent;

import com.tysci.ballq.activitys.BallQWebViewActivity;
import com.tysci.ballq.networks.HttpUrls;

/**
 * Created by LinDe on 2016-08-24 0024.
 * 过滤WebView中的Url
 */
public final class FilterBqUrlUtil
{
    public static boolean filterUrl(Activity activity, String url)
    {
        if (url.contains("ballqinapp://login"))
        {
            UserInfoUtil.userLogin(activity);
            return true;
        }
        else if (url.contains("ballqinapp://event/signing"))
        {
            if (UserInfoUtil.checkLogin(activity))
            {
                String userId = UserInfoUtil.getUserId(activity);
                String token = UserInfoUtil.getUserToken(activity);
                String urlStr = HttpUrls.HOST_URL + "/weixin/events/show_lottery/?user=" + userId + "&token=" + token;

                KLog.e("urlStr:" + urlStr);
                String title = "活动签到";
                Intent intent = new Intent(activity, BallQWebViewActivity.class);
                intent.putExtra("url", urlStr);
                intent.putExtra("title", title);
                activity.startActivity(intent);
            }
            else
            {
                UserInfoUtil.userLogin(activity);
            }
            return true;
        }
        else if (url.contains(HttpUrls.HOST_URL) || url.contains(HttpUrls.CIRCLE_HOST_URL))
        {
            String urlStr = url;
            KLog.e("url:" + url);
            if (UserInfoUtil.checkLogin(activity))
            {
                if (!url.contains("user=") && !url.contains("token="))
                {
                    String userId = UserInfoUtil.getUserId(activity);
                    String token = UserInfoUtil.getUserToken(activity);
                    String[] urls = urlStr.split("\\?");
                    urlStr = urls[0] + "?user=" + userId + "&token=" + token;
                }
            }
            String title = "球商";
            Intent intent = new Intent(activity, BallQWebViewActivity.class);
            KLog.e("strUrl:" + urlStr);
            intent.putExtra("url", urlStr);
            intent.putExtra("title", title);
            intent.putExtra("is_ding_dan", url.contains("ballq/indiana"));
            activity.startActivity(intent);
            return true;
        }
//        else if (!url.contains(HttpUrls.HOST_URL + "/weixin/events/lobby"))
//        {
//            String urlStr = url;
//            KLog.e("url:" + url);
//            if (UserInfoUtil.checkLogin(activity))
//            {
//                String userId = UserInfoUtil.getUserId(activity);
//                String token = UserInfoUtil.getUserToken(activity);
//                String[] urls = urlStr.split("\\?");
//                urlStr = urls[0] + "?user=" + userId + "&token=" + token;
//            }
//            String title = "球商";
//            Intent intent = new Intent(activity, BallQWebViewActivity.class);
//            KLog.e("strUrl:" + urlStr);
//            intent.putExtra("url", urlStr);
//            intent.putExtra("title", title);
//            intent.putExtra("is_ding_dan", url.contains("ballq/indiana"));
//            activity.startActivity(intent);
//            return true;
//        }
        else
        {
            return false;
        }
    }
}
