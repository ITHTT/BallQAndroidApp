package com.tysci.ballq.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tysci.ballq.activitys.BallQBallWarpDetailActivity;
import com.tysci.ballq.activitys.BallQCircleNoteDetailActivity;
import com.tysci.ballq.activitys.BallQFindCircleNoteActivity;
import com.tysci.ballq.activitys.BallQGreatWarGoActivity;
import com.tysci.ballq.activitys.BallQMainActivity;
import com.tysci.ballq.activitys.BallQMainUserRankingListActivity;
import com.tysci.ballq.activitys.BallQMatchDetailActivity;
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.activitys.BallQUserRankingListDetailActivity;
import com.tysci.ballq.activitys.BallQUserRewardRankingDetailActivity;
import com.tysci.ballq.activitys.BallQWebViewActivity;
import com.tysci.ballq.activitys.UserBettingGuessRecordActivity;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpUrls;

/**
 * Created by HTT on 2016/7/15.
 *
 * @author LinDe edit
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
                jumpApp(context, jumpUrl, jpush);
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
    private static void jumpApp(Context context, String jumpUrl, boolean jpush)
    {
        KLog.e("jump_url:" + jumpUrl);
        if (TextUtils.isEmpty(jumpUrl))
            return;

        if (jumpUrl.contains("ballqinapp://event/square"))
        {
            // 跳转→活动广场
            Intent intent = new Intent(context, BallQWebViewActivity.class);
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
            intent = getJPushIntent(intent, jpush);
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
            intent = getJPushIntent(intent, jpush);
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
            intent = getJPushIntent(intent, jpush);
            intent.putExtra(BallQCircleNoteDetailActivity.class.getSimpleName(), Integer.parseInt(id));
            context.startActivity(intent);
        }
        else if (jumpUrl.contains("ballqinapp://ranks"))
        {
            Intent intent = new Intent(context, BallQMainUserRankingListActivity.class);
            intent = getJPushIntent(intent, jpush);
            context.startActivity(intent);
        }
        else if (jumpUrl.contains("ballqinapp://rank/"))
        {
            String[] datas = jumpUrl.split("=");
            String id = datas[datas.length - 1];
            KLog.e("id:" + id);
            int rankType = Integer.parseInt(id);
            KLog.d(rankType);
            gotoUserRank(context, rankType, jpush);
        }
        else if (jumpUrl.contains("ballqinapp://section/"))
        {
            String[] datas = jumpUrl.split("=");
            String id = datas[datas.length - 1];
            KLog.e("id:" + id);
            int section_id = Integer.parseInt(id);
            KLog.e("section", section_id);

            //mainP.gotoBallQCircle(section_id);
            Intent intent = new Intent(context, BallQFindCircleNoteActivity.class);
            intent = getJPushIntent(intent, jpush);
            context.startActivity(intent);
        }
        else if (jumpUrl.contains("ballqinapp://rank/?rtype=1000"))
        {
            // 跳转→千场亚盘榜
            Intent intent = new Intent(context, BallQUserRewardRankingDetailActivity.class);
            intent = getJPushIntent(intent, jpush);
            context.startActivity(intent);
        }
        else if (jumpUrl.contains("ballqinapp://userbetting/?uid"))
        {
            // 跳转→用户投注记录
            String[] datas = jumpUrl.split("=");
            String id = datas[datas.length - 1];
            KLog.e(id);
            Class cls = UserBettingGuessRecordActivity.class;
            Intent intent = new Intent(context, cls);
            intent = getJPushIntent(intent, jpush);
            intent.putExtra(cls.getSimpleName(), id);
            context.startActivity(intent);
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

    public static void gotoUserRank(Context context, int rtype, boolean jpush)
    {
        if (UserInfoUtil.checkLogin(context))
        {
            String rankType = "tearn";
            int dateType = 7;
            String title = "总盈利榜";
            if (rtype == 11)
            {
                /**总盈利周榜*/
                rankType = "tearn";
                dateType = 7;
                title = "总盈利榜";
            }
            else if (rtype == 21)
            {
                /**总盈利月榜*/
                rankType = "tearn";
                dateType = 30;
                title = "总盈利榜";
            }
            else if (rtype == 31)
            {
                /**总盈利总榜*/
                rankType = "tearn";
                dateType = 1;
                title = "总盈利榜";
            }
            else if (rtype == 12)
            {
                /**盈利率周榜*/
                rankType = "ror";
                dateType = 7;
                title = "盈利率榜";
            }
            else if (rtype == 22)
            {
                /**盈利率月榜*/
                rankType = "ror";
                dateType = 30;
                title = "盈利率榜";
            }
            else if (rtype == 32)
            {
                /**盈利率总榜*/
                rankType = "ror";
                dateType = 1;
                title = "盈利率榜";
            }
            else if (rtype == 13)
            {
                /**亚盘率周榜*/
                rankType = "wins";
                dateType = 7;
                title = "亚盘胜率榜";
            }
            else if (rtype == 23)
            {
                rankType = "wins";
                dateType = 30;
                title = "亚盘胜率榜";
            }
            else if (rtype == 33)
            {
                rankType = "wins";
                dateType = 1;
                title = "亚盘胜率榜";
            }
            else if (rtype == 99)
            {
                rankType = "follow";
                dateType = -99;
                title = "人气榜";
            }

            Intent intent = new Intent(context, BallQUserRankingListDetailActivity.class);
            intent = getJPushIntent(intent, jpush);
            intent.putExtra("rank_type", rankType);
            intent.putExtra("date_type", dateType);
            intent.putExtra("title", title);
            context.startActivity(intent);
        }
        else
        {
            UserInfoUtil.userLogin(context);
        }
    }

}
