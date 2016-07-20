package com.tysci.ballq.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tysci.ballq.activitys.BallQBallWarpDetailActivity;
import com.tysci.ballq.activitys.BallQCircleNoteDetailActivity;
import com.tysci.ballq.activitys.BallQEventsPlazaActivity;
import com.tysci.ballq.activitys.BallQFindCircleNoteActivity;
import com.tysci.ballq.activitys.BallQGreatWarGoActivity;
import com.tysci.ballq.activitys.BallQMainUserRankingListActivity;
import com.tysci.ballq.activitys.BallQMatchDetailActivity;
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.activitys.BallQWebViewActivity;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.networks.HttpUrls;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQBusinessControler {

    public static void businessControler(Context context,int jumpType,String jumpUrl){
            if (jumpType==1) {
                KLog.e("jump_url:" + jumpUrl);
                if (jumpUrl.equals("ballqinapp://event/square")) {
                    /**跳转活动广场*/
                    // ToastUtil2.show(getActivity(),"点击活动广场图片");
                    Intent intent=new Intent(context, BallQEventsPlazaActivity.class);
                    context.startActivity(intent);
                } else if (jumpUrl.equals("ballqinapp://event/signing")) {
                    if (UserInfoUtil.checkLogin(context)) {
                        String userId = UserInfoUtil.getUserId(context);
                        String token = UserInfoUtil.getUserToken(context);
                        String urlStr = HttpUrls.HOST_URL + "/weixin/events/show_lottery/?user=" + userId + "&token=" + token;
                        KLog.e("urlStr:" + urlStr);
                        String title = "活动签到";
                        Intent intent = new Intent(context, BallQWebViewActivity.class);
                        intent.putExtra("url", urlStr);
                        intent.putExtra("title", title);
                        context.startActivity(intent);
                    } else {
                        UserInfoUtil.userLogin(context);
                    }
                } else if (jumpUrl.equals("ballqinapp://tips")) {
                    /**跳转至爆料大厅*/
                    //mainP.gotoBallQHomeInfomation(0);
                } else if (jumpUrl.contains("ballqinapp://tip/")) {
                    /**跳转到爆料详情*/
                    String datas[] = jumpUrl.split("=");
                    if (datas != null) {
                        String id = datas[datas.length - 1];
                        BallQTipOffEntity tipOffEntity=new BallQTipOffEntity();
                        tipOffEntity.setId(Integer.parseInt(id));
                        tipOffEntity.setEid(0);
                        Intent intent=new Intent(context, BallQTipOffDetailActivity.class);
                        intent.putExtra(BallQTipOffDetailActivity.class.getSimpleName(), tipOffEntity);
                        context.startActivity(intent);
                    }
                } else if (jumpUrl.equals("ballqinapp://articles")) {
                    //mainP.gotoBallQHomeInfomation(1);
                } else if (jumpUrl.contains("ballqinapp://article/")) {
                    String[] datas = jumpUrl.split("=");
                    if (datas != null) {
                        try {
                            BallQBallWarpInfoEntity info=new BallQBallWarpInfoEntity();
                            String id = datas[datas.length - 1];
                            info.setId(Integer.parseInt(id));
                            Intent intent=new Intent(context, BallQBallWarpDetailActivity.class);
                            intent.putExtra(BallQBallWarpDetailActivity.class.getSimpleName(),info);
                            context.startActivity(intent);
                        } catch (Exception ex) {

                        }
                    }
                } else if (jumpUrl.equals("ballqinapp://bbs")) {
                    Intent intent=new Intent(context, BallQFindCircleNoteActivity.class);
                    context.startActivity(intent);
                } else if (jumpUrl.equals("ballqinapp://fmatchs")) {
                    //mainP.gotoBallQMatchList(0);
                } else if (jumpUrl.contains("ballqinapp://fmatch/")) {
                    /**跳转到足球比赛详情*/
                    String[] datas = jumpUrl.split("=");
                    if (datas != null) {
                        String id = datas[datas.length - 1];
                        BallQMatchEntity info=new BallQMatchEntity();
                        KLog.e("id:" + id);
                        info.setEid(Integer.getInteger("id"));
                        info.setEtype(0);
                        Intent i = new Intent(context, BallQMatchDetailActivity.class);
                        i.putExtra(BallQMatchDetailActivity.class.getSimpleName(),info);
                        context.startActivity(i);
                    }
                } else if (jumpUrl.contains("ballqinapp://bmatch/")) {
                    /**跳转到篮球比赛详情*/
                    /**跳转到足球比赛详情*/
                    String[] datas = jumpUrl.split("=");
                    if (datas != null) {
                        String id = datas[datas.length - 1];
                        BallQMatchEntity info=new BallQMatchEntity();
                        KLog.e("id:" + id);
                        info.setEid(Integer.getInteger("id"));
                        info.setEtype(1);
                        Intent i = new Intent(context, BallQMatchDetailActivity.class);
                        i.putExtra(BallQMatchDetailActivity.class.getSimpleName(),info);
                        context.startActivity(i);
                    }
                } else if (jumpUrl.equals("ballqinapp://bmatchs")) {
                    //mainP.gotoBallQMatchList(1);
                } else if (jumpUrl.equals("ballqinapp://go")) {
                    //mainP.gotoBallQGo();
                    Intent intent=new Intent(context, BallQGreatWarGoActivity.class);
                    context.startActivity(intent);
                } else if (jumpUrl.contains("ballqinapp://userinfo/")) {
                    String[] datas = jumpUrl.split("=");
                    if (datas != null) {
                        String id = datas[datas.length - 1];
                        UserInfoUtil.lookUserInfo(context,Integer.parseInt(id));
                    }
                } else if (jumpUrl.contains("ballqinapp://topic/")) {
                    String[] datas = jumpUrl.split("=");
                    if (datas != null) {
                        String id = datas[datas.length - 1];
                        KLog.e("id:" + id);
                        Intent intent=new Intent(context, BallQCircleNoteDetailActivity.class);
                        intent.putExtra(BallQCircleNoteDetailActivity.class.getSimpleName(),Integer.parseInt(id));
                        context.startActivity(intent);
                    }
                } else if (jumpUrl.equals("ballqinapp://ranks")) {
                    //mainP.gotoBallQUserRank();
                    Intent intent=new Intent(context, BallQMainUserRankingListActivity.class);
                    context.startActivity(intent);
                } else if (jumpUrl.contains("ballqinapp://rank/")) {
                    String[] datas = jumpUrl.split("=");
                    if (datas != null) {
                        try {
                            String id = datas[datas.length - 1];
                            KLog.e("id:" + id);
                            int rankType = Integer.parseInt(id);
                            //gotoUserRank(rankType);
                        } catch (Exception ex) {

                        }
                    }
                } else if (jumpUrl.contains("ballqinapp://section/")) {
                    String[] datas = jumpUrl.split("=");
                    if (datas != null) {
                        String id = datas[datas.length - 1];
                        KLog.e("id:" + id);
                        try {
                            int section_id = Integer.parseInt(id);
                            //mainP.gotoBallQCircle(section_id);
                        } catch (Exception e) {

                        }
                    }
                }
            } else if (jumpType==0) {
                if (!TextUtils.isEmpty(jumpUrl)) {
                    Intent intent = new Intent(context, BallQWebViewActivity.class);
                    intent.putExtra("url", jumpUrl);
                    intent.putExtra("title", "球商");
                    context.startActivity(intent);
                }
            } else if (jumpType==2) {
                if (UserInfoUtil.checkLogin(context)) {
                    String userId = UserInfoUtil.getUserId(context);
                    String token = UserInfoUtil.getUserToken(context);
                    String urlStr = jumpUrl + "?user=" + userId + "&token=" + token;
                    String title = "活动竞猜";
                    Intent intent = new Intent(context, BallQWebViewActivity.class);
                    intent.putExtra("url", urlStr);
                    intent.putExtra("title", title);
                    context.startActivity(intent);
                } else {
                    UserInfoUtil.userLogin(context);
                }
            }
        }
}
