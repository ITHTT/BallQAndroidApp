package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.SharedPreferencesUtil;
import com.tysci.ballq.utils.ToastUtil;

import butterknife.Bind;

/**
 * Created by Administrator on 2016-08-12 0012.
 * 改变服务器
 */
public class BqServerCustomActivity extends BaseActivity
{
    public static final String KEY_SERVER = "BqServerCustomActivity/KEY_SERVER";
    public static final String KEY_CIRCLE_SERVER = "BqServerCustomActivity/KEY_CIRCLE_SERVER";
    public static final String KEY_SERVER_CHANGE_TIME_MILLIS_FLAG = "BqServerCustomActivity/KEY_SERVER_CHANGE_TIME_MILLIS_FLAG";

    @Bind(R.id.edit_server)
    EditText edit_server;
    @Bind(R.id.edit_circle_server)
    EditText edit_circle_server;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_server_custom;
    }

    @Override
    protected void initViews()
    {
        TextView tv = titleBar.getRightMenuTextView();
        tv.setText("提交");
        tv.setBackgroundResource(R.drawable.shape_edit_text_bg_gold);
        //noinspection deprecation
        tv.setTextColor(getResources().getColor(R.color.gold));
        tv.setPadding(10, 5, 5, 10);

        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String server = edit_server.getText().toString();
                String circleServer = edit_circle_server.getText().toString();

                if (!TextUtils.isEmpty(server) && !server.startsWith("http://") && !server.startsWith("https://"))
                {
                    server = "http://" + server;
                }
                if (!TextUtils.isEmpty(server) && server.endsWith("/"))
                {
                    server = server.substring(0, server.length() - 1);
                }
                SharedPreferencesUtil.setValue(BqServerCustomActivity.this, KEY_SERVER, server);

                if (!TextUtils.isEmpty(circleServer) && circleServer.startsWith("http://") && !circleServer.startsWith("https://"))
                {
                    circleServer = "http://" + server;
                }
                if (!TextUtils.isEmpty(circleServer) && !circleServer.endsWith("/"))
                {
                    circleServer = circleServer + "/";
                }
                SharedPreferencesUtil.setValue(BqServerCustomActivity.this, KEY_CIRCLE_SERVER, circleServer);

                SharedPreferencesUtil.setValue(BqServerCustomActivity.this, KEY_SERVER_CHANGE_TIME_MILLIS_FLAG, System.currentTimeMillis());

                ToastUtil.show(BqServerCustomActivity.this, "修改成功");
                HttpUrls.initialized(BqServerCustomActivity.this);
                finish();
            }
        });
    }

    @Override
    protected View getLoadingTargetView()
    {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent)
    {
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

    }
}
