package com.tysci.ballq.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tysci.ballq.activitys.BallQBallWarpDetailActivity;
import com.tysci.ballq.activitys.BallQCircleNoteDetailActivity;
import com.tysci.ballq.activitys.BallQEventsPlazaActivity;
import com.tysci.ballq.activitys.BallQFindCircleNoteActivity;
import com.tysci.ballq.activitys.BallQGreatWarGoActivity;
import com.tysci.ballq.activitys.BallQMainActivity;
import com.tysci.ballq.activitys.BallQMainUserRankingListActivity;
import com.tysci.ballq.activitys.BallQMatchDetailActivity;
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.activitys.BallQWebViewActivity;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpUrls;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQBusinessControler
{
    private static Intent getJPushIntent(Intent intent, boolean jpush)
    {
        if (jpush)
        {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        return intent;
    }

    public static void businessControler(Context context, int jumpType, String jumpUrl)
    {
        businessControler(context, jumpType, jumpUrl, false);
    }

    public static void businessControler(Context context, int jumpType, String jumpUrl, boolean jpush)
    {
        if (jumpType == 1)
        {
            try
            {
                jumpApp(context, jumpUrl,jpush);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (jumpType == 0)
        {
            if (!TextUtils.isEmpty(jumpUrl))
            {
                Intent intent = new Intent(context, BallQWebViewActivity.class);
                intent = getJPushIntent(intent, jpush);
                intent.putExtra("url", jumpUrl);
                intent.putExtra("title", "球商");
                context.startActivity(intent);
            }
        }
        else if (jumpType == 2)
        {
            if (UserInfoUtil.checkLogin(context))
            {
                String userId = UserInfoUtil.getUserId(context);
                String token = UserInfoUtil.getUserToken(context);
                String urlStr = jumpUrl + "?user=" + userId + "&token=" + token;
                String title = "活动竞猜";
                Intent intent = new Intent(context, BallQWebViewActivity.class);
                intent = getJPushIntent(intent, jpush);
                intent.putExtra("url", urlStr);
                intent.putExtra("title", title);
                context.startActivity(intent);
            }
            else
            {
                UserInfoUtil.userLogin(context);
            }
        }
    }

    /**
     * 跳转→应用内
     */
    private static void jumpApp(Context context, String jumpUrl,boolean jpush)
    {
        KLog.e("jump_url:" + jumpUrl);
        if (TextUtils.isEmpty(jumpUrl))
            return;

        if (jumpUrl.contains("ballqinapp://event/square"))
        {
            // 跳转→活动广场
            Intent intent = new Intent(context, BallQEventsPlazaActivity.class);
            intent = getJPushIntent(intent, jpush);
            context.startActivity(intent);
        }
        else if (jumpUrl.contains("ballqinapp://event/signing"))
        {
            if (UserInfoUtil.checkLogin(context))
            {
                String userId = UserInfoUtil.getUserId(context);
                String token = UserInfoUtil.getUserToken(context);
                String urlStr = HttpUrls.HOST_URL + "/weixin/events/show_lottery/?user=" + userId + "&token=" + token;
                KLog.e("urlStr:" + urlStr);
                String title = "活动签到";
                Intent intent = new Intent(context, BallQWebViewActivity.class);
                intent = getJPushIntent(intent, jpush);
                intent.putExtra("url", urlStr);
                intent.putExtra("title", title);
                context.startActivity(intent);
            }
            else
            {
                UserInfoUtil.userLogin(context);
            }
        }
        else if (jumpUrl.contains("ballqinapp://tips"))
        {
            // 跳转→爆料大厅
            jumpMainActivity("ballqinapp://tips");
        }
        else if (jumpUrl.contains("ballqinapp://tip/"))
        {
            // 跳转→爆料详情
            String datas[] = jumpUrl.split("=");
            String id = datas[datas.length - 1];
            BallQTipOffEntity tipOffEntity = new BallQTipOffEntity();
            tipOffEntity.setId(Integer.parseInt(id));
            tipOffEntity.setEid(0);
            Intent intent = new Intent(context, BallQTipOffDetailActivity.class);
            intent = getJPushIntent(intent, jpush);
            intent.putExtra(BallQTipOffDetailActivity.class.getSimpleName(), tipOffEntity);
            context.startActivity(intent);
        }
        else if (jumpUrl.contains("ballqinapp://articles"))
        {
            // 跳转→球茎列表
            jumpMainActivity("ballqinapp://articles");
        }
        else if (jumpUrl.contains("ballqinapp://article/"))
        {
            String[] datas = jumpUrl.split("=");
            BallQBallWarpInfoEntity info = new BallQBallWarpInfoEntity();
            String id = datas[datas.length - 1];
            info.setId(Integer.parseInt(id));
            Intent intent = new Intent(context, BallQBallWarpDetailActivity.class);
            intent = getJPushIntent(intent, jpush);
            intent.putExtra(BallQBallWarpDetailActivity.class.getSimpleName(), info);
            context.startActivity(intent);
        }
        else if (jumpUrl.contains("ballqinapp://bbs"))
        {
            Intent intent = new Intent(context, BallQFindCircleNoteActivity.class);
            context.startActivity(intent);
        }
        else if (jumpUrl.contains("ballqinapp://fmatchs"))
        {
            // 跳转→足球比赛列表
            jumpMainActivity("ballqinapp://fmatchs");
        }
        else if (jumpUrl.contains("ballqinapp://fmatch/"))
        {
            // 跳转到足球比赛详情
            String[] datas = jumpUrl.split("=");
            String id = datas[datas.length - 1];
            BallQMatchEntity info = new BallQMatchEntity();
            KLog.e("id:" + id);
            info.setEid(Integer.getInteger("id"));
            info.setEtype(0);
            Intent i = new Intent(context, BallQMatchDetailActivity.class);
            i = getJPushIntent(i, jpush);
            i.putExtra(BallQMatchDetailActivity.class.getSimpleName(), info);
            context.startActivity(i);
        }
        else if (jumpUrl.contains("ballqinapp://bmatchs"))
        {
            // 跳转→篮球比赛列表
            jumpMainActivity("ballqinapp://bmatchs");
        }
        else if (jumpUrl.contains("ballqinapp://bmatch/"))
        {
            // 跳转→篮球比赛详情
            String[] datas = jumpUrl.split("=");
            String id = datas[datas.length - 1];
            BallQMatchEntity info = new BallQMatchEntity();
            KLog.e("id:" + id);
            info.setEid(Integer.getInteger("id"));
            info.setEtype(1);
            Intent i = new Intent(context, BallQMatchDetailActivity.class);
            i = getJPushIntent(i, jpush);
            i.putExtra(BallQMatchDetailActivity.class.getSimpleName(), info);
            context.startActivity(i);
        }
        else if (jumpUrl.contains("ballqinapp://go"))
        {
            Intent intent = new Intent(context, BallQGreatWarGoActivity.class);
            context.startActivity(intent);
        }
        else if (jumpUrl.contains("ballqinapp://userinfo/"))
        {
            String[] datas = jumpUrl.split("=");
            String id = datas[datas.length - 1];
            UserInfoUtil.lookUserInfo(context, Integer.parseInt(id));
        }
        else if (jumpUrl.contains("ballqinapp://topic/"))
        {
            String[] datas = jumpUrl.split("=");
            String id = datas[datas.length - 1];
            KLog.e("id:" + id);
            Intent intent = new Intent(context, BallQCircleNoteDetailActivity.class);
            intent.putExtra(BallQCircleNoteDetailActivity.class.getSimpleName(), Integer.parseInt(id));
            context.startActivity(intent);
        }
        else if (jumpUrl.contains("ballqinapp://ranks"))
        {
            Intent intent = new Intent(context, BallQMainUserRankingListActivity.class);
            context.startActivity(intent);
        }
        else if (jumpUrl.contains("ballqinapp://rank/"))
        {
            String[] datas = jumpUrl.split("=");
            String id = datas[datas.length - 1];
            KLog.e("id:" + id);
            int rankType = Integer.parseInt(id);
            KLog.d(rankType);

            //gotoUserRank(rankType);
            // TODO: 2016-07-21 0021
        }
        else if (jumpUrl.contains("ballqinapp://section/"))
        {
            String[] datas = jumpUrl.split("=");
            String id = datas[datas.length - 1];
            KLog.e("id:" + id);
            int section_id = Integer.parseInt(id);
            KLog.e("section", section_id);

            //mainP.gotoBallQCircle(section_id);
            // TODO: 2016-07-21 0021
        }
        else if (jumpUrl.contains("ballqinapp://rank/?rtype=1000"))
        {
            // TODO 跳转→千场亚盘榜
        }
        else if (jumpUrl.contains("ballqinapp://userbetting/?uid"))
        {
            // TODO 跳转→用户投注记录
            String[] datas = jumpUrl.split("=");
            String id = datas[datas.length - 1];
            KLog.e(id);
        }
        else if (jumpUrl.contains("ballqinapp://homepage"))
        {
            // 跳转→首页
            jumpMainActivity("ballqinapp://homepage");
        }
        else if (jumpUrl.contains("ballqinapp://tips_focus"))
        {
            // 跳转→爆料我的关注
            jumpMainActivity("ballqinapp://tips_focus");
        }

//        temp = "ballqinapp://event/square";
//        if (jumpUrl.contains(temp))
//        {
//
//            return;
//        }
//        temp = "ballqinapp://event/signing";
//        if (jumpUrl.contains(temp))
//        {
//            if (UserInfoUtil.checkLogin(context))
//            {
//                String userId = UserInfoUtil.getUserId(context);
//                String token = UserInfoUtil.getUserToken(context);
//                String urlStr = HttpUrls.HOST_URL + "/weixin/events/show_lottery/?user=" + userId + "&token=" + token;
//                KLog.e("urlStr:" + urlStr);
//                String title = "活动签到";
//                Intent intent = new Intent(context, BallQWebViewActivity.class);
//                intent.putExtra("url", urlStr);
//                intent.putExtra("title", title);
//                context.startActivity(intent);
//            }
//            else
//            {
//                UserInfoUtil.userLogin(context);
//            }
//            return;
//        }
//        temp = "ballqinapp://tips";
//        if (jumpUrl.contains(temp))
//        {
//            // 跳转→爆料大厅
//            jumpMainActivity(temp);
//            return;
//        }
//        temp = "ballqinapp://tip/";
//        if (jumpUrl.contains(temp))
//        {
//            // 跳转→爆料详情
//            String datas[] = jumpUrl.split("=");
//            String id = datas[datas.length - 1];
//            BallQTipOffEntity tipOffEntity = new BallQTipOffEntity();
//            tipOffEntity.setId(Integer.parseInt(id));
//            tipOffEntity.setEid(0);
//            Intent intent = new Intent(context, BallQTipOffDetailActivity.class);
//            intent.putExtra(BallQTipOffDetailActivity.class.getSimpleName(), tipOffEntity);
//            context.startActivity(intent);
//            return;
//        }
//        temp = "ballqinapp://articles";
//        if (jumpUrl.contains(temp))
//        {
//            // 跳转→球茎列表
//            jumpMainActivity(temp);
//            return;
//        }
//        temp = "ballqinapp://article/";
//        if (jumpUrl.contains(temp))
//        {
//            String[] datas = jumpUrl.split("=");
//            try
//            {
//                BallQBallWarpInfoEntity info = new BallQBallWarpInfoEntity();
//                String id = datas[datas.length - 1];
//                info.setId(Integer.parseInt(id));
//                Intent intent = new Intent(context, BallQBallWarpDetailActivity.class);
//                intent.putExtra(BallQBallWarpDetailActivity.class.getSimpleName(), info);
//                context.startActivity(intent);
//            }
//            catch (Exception ignored)
//            {
//            }
//            return;
//        }
//        temp = "ballqinapp://bbs";
//        if (jumpUrl.contains(temp))
//        {
//            Intent intent = new Intent(context, BallQFindCircleNoteActivity.class);
//            context.startActivity(intent);
//            return;
//        }
//        temp = "ballqinapp://fmatchs";
//        if (jumpUrl.contains(temp))
//        {
//            // 跳转→足球比赛列表
//            jumpMainActivity(temp);
//            return;
//        }
//        temp = "ballqinapp://fmatch/";
//        if (jumpUrl.contains(temp))
//        {
//            // 跳转到足球比赛详情
//            String[] datas = jumpUrl.split("=");
//            String id = datas[datas.length - 1];
//            BallQMatchEntity info = new BallQMatchEntity();
//            KLog.e("id:" + id);
//            info.setEid(Integer.getInteger("id"));
//            info.setEtype(0);
//            Intent i = new Intent(context, BallQMatchDetailActivity.class);
//            i.putExtra(BallQMatchDetailActivity.class.getSimpleName(), info);
//            context.startActivity(i);
//            return;
//        }
//        temp = "ballqinapp://bmatch/";
//        if (jumpUrl.contains(temp))
//        {
//            // 跳转→篮球比赛详情
//            String[] datas = jumpUrl.split("=");
//            String id = datas[datas.length - 1];
//            BallQMatchEntity info = new BallQMatchEntity();
//            KLog.e("id:" + id);
//            info.setEid(Integer.getInteger("id"));
//            info.setEtype(1);
//            Intent i = new Intent(context, BallQMatchDetailActivity.class);
//            i.putExtra(BallQMatchDetailActivity.class.getSimpleName(), info);
//            context.startActivity(i);
//            return;
//        }
//        temp = "ballqinapp://bmatchs";
//        if (jumpUrl.contains(temp))
//        {
//            // 跳转→篮球比赛列表
//            jumpMainActivity(temp);
//            return;
//        }
//        temp = "ballqinapp://go";
//        if (jumpUrl.contains(temp))
//        {
//            Intent intent = new Intent(context, BallQGreatWarGoActivity.class);
//            context.startActivity(intent);
//            return;
//        }
//        temp = "ballqinapp://userinfo/";
//        if (jumpUrl.contains(temp))
//        {
//            String[] datas = jumpUrl.split("=");
//            String id = datas[datas.length - 1];
//            UserInfoUtil.lookUserInfo(context, Integer.parseInt(id));
//            return;
//        }
//        temp = "ballqinapp://topic/";
//        if (jumpUrl.contains(temp))
//        {
//            String[] datas = jumpUrl.split("=");
//            String id = datas[datas.length - 1];
//            KLog.e("id:" + id);
//            Intent intent = new Intent(context, BallQCircleNoteDetailActivity.class);
//            intent.putExtra(BallQCircleNoteDetailActivity.class.getSimpleName(), Integer.parseInt(id));
//            context.startActivity(intent);
//            return;
//        }
//        temp = "ballqinapp://ranks";
//        if (jumpUrl.contains(temp))
//        {
//            Intent intent = new Intent(context, BallQMainUserRankingListActivity.class);
//            context.startActivity(intent);
//            return;
//        }
//        temp = "ballqinapp://rank/";
//        if (jumpUrl.contains(temp))
//        {
//            String[] datas = jumpUrl.split("=");
//            String id = datas[datas.length - 1];
//            KLog.e("id:" + id);
//            int rankType = Integer.parseInt(id);
//            //gotoUserRank(rankType);
//            //  2016-07-21 0021
//            return;
//        }
//        temp = "ballqinapp://section/";
//        if (jumpUrl.contains(temp))
//        {
//            String[] datas = jumpUrl.split("=");
//            String id = datas[datas.length - 1];
//            KLog.e("id:" + id);
//            int section_id = Integer.parseInt(id);
//            //mainP.gotoBallQCircle(section_id);
//            //  2016-07-21 0021
//            return;
//        }
//        temp = "ballqinapp://rank/?rtype=1000";
//        if (jumpUrl.contains(temp))
//        {
//            return;
//        }
//        temp = "ballqinapp://userbetting/?uid";
//        if (jumpUrl.contains(temp))
//        {
//            // 跳转→用户投注记录
//            String[] datas = jumpUrl.split("=");
//            String id = datas[datas.length - 1];
//            KLog.e(id);
//            return;
//        }
//        temp = "ballqinapp://homepage";
//        if (jumpUrl.contains(temp))
//        {
//            // 跳转→首页
//            jumpMainActivity(temp);
//            return;
//        }
//        temp = "ballqinapp://tips_focus";
//        if (jumpUrl.contains(temp))
//        {
//            // 跳转→爆料我的关注
//            jumpMainActivity(temp);
//            return;
//        }
    }

    private static void jumpMainActivity(String temp)
    {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"jump_in_app\":\"");
        sb.append(temp);
        sb.append("\"}");

        EventObject eventObject = new EventObject();
        eventObject.getData().putString("jump_in_app", sb.toString());
        eventObject.addReceiver(BallQMainActivity.class);
        EventObject.postEventObject(eventObject, "user_withdrawals");
    }
}
