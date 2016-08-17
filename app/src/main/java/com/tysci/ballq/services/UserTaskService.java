package com.tysci.ballq.services;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseService;
import com.tysci.ballq.modles.JsonParams;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CToast;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-07-13 0013.
 * 用户任务轮询
 */
public class UserTaskService extends BaseService
{

    private final Handler handler = new Handler();
    private final Runnable connectToGetTaskMsg = new Runnable()
    {
        @Override
        public void run()
        {
            connectToGetTaskMsg();
        }
    };
    private final Runnable notifyShowUserTaskMsg = new Runnable()
    {
        @Override
        public void run()
        {
            notifyShowUserTaskMsg();
        }
    };

    private ArrayList<Integer> delayList;// 所有延迟参数 发起轮循
    private int delayPosition;

    private JSONArray data;// 获取到的任务数据
    private int showTaskPosition;

    private CToast mToast;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (delayList == null)
        {
            delayList = new ArrayList<>();
        }
        delayList.clear();
        delayList.add(1000);
        delayList.add(3000);
        delayList.add(5000);
        delayList.add(8000);
        delayList.add(18000);

        delayPosition = 0;

        connectToGetTaskMsg();
        return super.onStartCommand(intent, flags, startId);
    }

    private void connectToGetTaskMsg()
    {
        if (!UserInfoUtil.checkLogin(this))
        {
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("user", UserInfoUtil.getUserId(this));
        map.put("token", UserInfoUtil.getUserToken(this));

        HttpClientUtil.getHttpClientUtil().sendPostRequest("UserTaskService", HttpUrls.HOST_URL_V5 + "user/notify/usersystem/", map, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {
            }

            @Override
            public void onError(Call call, Exception error)
            {
                error.printStackTrace();
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                try
                {
                    JSONObject object = JSON.parseObject(response);
//                    if (object.getInteger("status") == 0 && object.getString("message").equalsIgnoreCase("ok")) {
                    if (JsonParams.isJsonRight(object))
                    {
                        data = object.getJSONArray("data");
                        if (data == null || data.size() == 0)
                        {
                            notifyNextTask();
                        }
                        else
                        {
                            showTaskPosition = 0;
                            notifyShowUserTaskMsg();
                        }
                    }
                    else
                    {
                        notifyNextTask();
                    }
                }
                catch (Exception e)
                {
                    notifyNextTask();
                }
            }

            @Override
            public void onFinish(Call call)
            {
            }
        });
    }

    private void notifyNextTask()
    {
        final long delay;
        try
        {
            delay = delayList.get(delayPosition++);
        }
        catch (Exception e)
        {
            stopSelf();
            return;
        }

        handler.postDelayed(connectToGetTaskMsg, delay);
    }

    /**
     * 展示用户完成的任务
     */
    private void notifyShowUserTaskMsg()
    {
        JSONObject aData;
        try
        {
            aData = data.getJSONObject(showTaskPosition++);
        }
        catch (Exception e)
        {
            stopSelf();
            return;
        }
        if (aData == null)
        {
            stopSelf();
            return;
        }

        String subtype = aData.getString("subtype");
        switch (subtype)
        {
            case "101":// 等级升级
            case "201":// 获得积分
            case "301":// 获得成就
            case "302":// 取消成就
                String txt = aData.getString("txt");

                int point = aData.getInteger("point");
                String pointResult = point >= 0 ? "+" + point : String.valueOf(point);

                long stime = aData.getLong("stime");

                showUserTaskCompleteNormalToast(txt, pointResult, stime);
                break;
            case "1001001":// 取得不分享红包
                break;
            case "1001002":// 取得要分享红包
                break;
            case "1001201":// 第二重礼提示，获得连红红包
            case "1001301":// 第三重礼提示，取得实物奖品
            case "1001302":// 第三重礼提示，取得第几名奖品为红包
            case "1001303":// 第三重礼提示，取得第几名无奖品
                break;
            case "1001304":// 助力红包
                break;
        }
    }

    private void showUserTaskCompleteNormalToast(String txt, String point, long stime)
    {
        if (mToast != null)
        {
            mToast.hide();
        }
        mToast = new CToast(this);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(this).inflate(R.layout.toast_user_task_show, null);
        TextView content = (TextView) view.findViewById(R.id.tv_content);
        TextView tv_point = (TextView) view.findViewById(R.id.tv_point);

        content.setText(txt);
        tv_point.setText(point);

        mToast.setView(view);
        mToast.setDuration((int) stime);
        mToast.setGravity(Gravity.CENTER, 0, 0);

        mToast.show();

        handler.postDelayed(notifyShowUserTaskMsg, stime);
    }
}
