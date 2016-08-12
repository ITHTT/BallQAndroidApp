package com.tysci.ballq.modles.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tysci.ballq.activitys.BallQMainActivity;
import com.tysci.ballq.utils.BallQBusinessControler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by LinDe on 2016-02-16 0016.
 * 极光推送 接收广播
 */
public class JPushReceiver extends BroadcastReceiver
{
    private final static String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
        {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        }
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
        {
            processCustomMessage(context, bundle);
        }
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
        {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notificationId);

        }
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
        {
            // 极光推送  type url jump_type
            userOpenNotification(context, bundle);
        }
        else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))
        {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        }
        else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))
        {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        }
        else
        {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void userOpenNotification(Context context, Bundle bundle)
    {
        Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

        String extraExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (TextUtils.isEmpty(extraExtra))
        {
            openDefaultActivity(context, bundle);
        }
        else
        {
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(extraExtra);
            // {"type":601,"url":"example_url","jump_type":0}
            if (object == null)
            {
                openDefaultActivity(context, bundle);
            }
            else
            {
                int type = object.getInteger("type");
                String url = object.getString("url");
                int jump_type = object.getInteger("jump_type");
                switch (type)
                {
                    case 601:
                        BallQBusinessControler.businessControler(context, jump_type, url, true);
                        break;
                }
            }
        }
    }

    private void openDefaultActivity(Context context, Bundle bundle)
    {//打开默认的Activity
        Intent i = new Intent(context, BallQMainActivity.class);
        i.putExtras(bundle);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle)
    {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet())
        {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID))
            {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getInt(key));
            }
            else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE))
            {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getBoolean(key));
            }
            else if (key.equals(JPushInterface.EXTRA_EXTRA))
            {
                //noinspection ConstantConditions
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty())
                {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try
                {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext())
                    {
                        String myKey = it.next();
                        sb.append("\nkey:").append(key).append(", value: [").append(myKey).append(" - ").append(json.optString(myKey)).append("]");
                    }
                }
                catch (JSONException e)
                {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            }
            else
            {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to
    private void processCustomMessage(Context context, Bundle bundle)
    {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Intent msgIntent = new Intent("com.example.jpushdemo.MESSAGE_RECEIVED_ACTION");
        msgIntent.putExtra("message", message);
        if (!isEmpty(extras))
        {
            try
            {
                JSONObject extraJson = new JSONObject(extras);
                if (extraJson.length() > 0)
                {
                    msgIntent.putExtra("extras", extras);
                }
            }
            catch (JSONException ignored)
            {
            }
        }
        context.sendBroadcast(msgIntent);
//        }
    }

    public static boolean isEmpty(String s)
    {
        return null == s || s.length() == 0 || s.trim().length() == 0;
    }
}
