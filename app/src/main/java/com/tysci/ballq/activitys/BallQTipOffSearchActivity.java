package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.views.widgets.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQTipOffSearchActivity extends BaseActivity
{
    @Bind(R.id.et_search_content)
    protected EditText etSearchContent;
    @Bind(R.id.flowlayout)
    FlowLayout flowLayout;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_tip_off_search;
    }

    @Override
    protected void initViews()
    {
        setTitle("查找爆料人");
        getSearchTags();
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
    protected void handleInstanceState(Bundle savedInstanceState)
    {

    }

    @Override
    protected void onViewClick(View view)
    {
        onClickSearchTag(view);

    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {

    }

    private void getSearchTags()
    {
        String url = HttpUrls.HOST_URL + "/api/ares/tags/";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {

            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty())
                    {
                        int status = obj.getIntValue("status");
                        if (status == 0)
                        {
                            com.alibaba.fastjson.JSONArray datas = obj.getJSONArray("data");
                            if (datas != null && !datas.isEmpty())
                            {
                                List<String> tags = new ArrayList<String>(datas.size());
                                CommonUtils.getJSONListObject(datas, tags, String.class);
                                addTags(tags);
                            }
                        }
                    }
                }

            }

            @Override
            public void onFinish(Call call)
            {

            }
        });
    }

    private void addTags(List<String> tags)
    {
        if (tags != null && !tags.isEmpty())
        {
            TextView tvTag;
            for (String tag : tags)
            {
                tvTag = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_search_tag_item, null);
                tvTag.setText(tag);
                tvTag.setOnClickListener(this);
                flowLayout.addView(tvTag);
            }
        }
    }

    @OnClick(R.id.iv_search)
    protected void onClickSearch(View view)
    {
        String searchContent = etSearchContent.getText().toString();
        if (TextUtils.isEmpty(searchContent))
        {
            ToastUtil.show(this, "请输入搜索内容");
            return;
        }
        search(searchContent);
    }

    private void onClickSearchTag(View view)
    {
        if (view instanceof TextView)
        {
            String tag = ((TextView) view).getText().toString();
            if (!TextUtils.isEmpty(tag))
            {
                search(tag);
            }
        }
    }

    private void search(String tag)
    {
        Intent intent = new Intent(this, BallQTipOffUserListActivity.class);
        intent.putExtra("search_tag", tag);
        startActivity(intent);
    }
}
