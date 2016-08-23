package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.dialog.SpinKitProgressDialog;
import com.tysci.ballq.interfaces.ITabCheck;
import com.tysci.ballq.modles.JsonParams;
import com.tysci.ballq.modles.UserAchievementEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ScreenUtil;
import com.tysci.ballq.utils.SwipeUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.UserAchievementHeaderView;
import com.tysci.ballq.views.adapters.UserAchievementAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/20.
 *
 * @author Edit by Linde
 */
public class UserAchievementActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ITabCheck
{
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler_view)
    AutoLoadMoreRecyclerView recycler_view;

    SwipeUtil mSwipeUtil;

    UserAchievementHeaderView headerView;

    private UserAchievementAdapter adapter;
    private List<UserAchievementEntity> dataList;

    private String userId;

    private boolean isShowAttained;// 显示已经获得成就
    private ArrayList<Integer> showingList;

    private SpinKitProgressDialog mDialog;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_ballq_user_achievement;
    }

    @Override
    protected void initViews()
    {

        refreshLayout.setOnRefreshListener(this);
        mSwipeUtil = new SwipeUtil(refreshLayout);

        GridLayoutManager llm = new GridLayoutManager(this, 3);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(llm);

        headerView = new UserAchievementHeaderView(this);
        headerView.setOnTabChangeListener(this);
        headerView.setShowingListener(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = ScreenUtil.getDisplayMetrics(this).widthPixels;
        headerView.setLayoutParams(lp);
        recycler_view.addHeaderView(headerView);
    }

    @Override
    protected View getLoadingTargetView()
    {
        return findViewById(R.id.swipe_refresh);
    }

    @Override
    protected void getIntentData(Intent intent)
    {
        userId = intent.getStringExtra("uid");
        if (TextUtils.isEmpty(userId))
        {
            userId = UserInfoUtil.getUserId(this);
            setTitleText("我的成就");
        }
        else
        {
            setTitleText("他的成就");
        }
        final boolean isSelf = userId.equals(UserInfoUtil.getUserId(this));

        dataList = new ArrayList<>();
        adapter = new UserAchievementAdapter(this, isSelf, dataList);
        recycler_view.setAdapter(adapter);

        showingList = new ArrayList<>();

        headerView.setShowingLayoutVisibility(isSelf);

        showLoading();
        headerView.startFirstCheck();
    }

    @Override
    protected boolean isCanceledEventBus()
    {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {}

    @Override
    protected void handleInstanceState(Bundle outState)
    {

    }

    public void addShowing(int id)
    {
        int showingNumber = 0;
        for (Integer showing : showingList)
        {
            if (showing > 0)
            {
                if (showing == id)
                {
                    ToastUtil.show(this, "该成就已经展示");
                    return;
                }
                showingNumber++;
            }
        }
        if (showingNumber >= 2)
        {
            ToastUtil.show(this, "最多只能展示两个成就");
            return;
        }
        if (id > 0)
        {
            showingList.add(id);
        }

        StringBuilder sb = new StringBuilder();
        for (Integer showing : showingList)
        {
            if (showing > 0)
            {
                sb.append(showing);
                sb.append(",");
            }
        }

        HashMap<String, String> map = new HashMap<>();
        if (UserInfoUtil.checkLogin(this))
        {
            map.put("user", UserInfoUtil.getUserId(this));
            map.put("token", UserInfoUtil.getUserToken(this));
        }
        if (sb.length() > 1)
            map.put("ids", sb.substring(0, sb.length() - 1));
        else
            map.put("ids", sb.toString());

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, HttpUrls.HOST_URL_V5 + "user/achievement/select_show/", map, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {
                showDialog();
            }

            @Override
            public void onError(Call call, Exception error)
            {
                ToastUtil.show(UserAchievementActivity.this, R.string.request_error);
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                JSONObject object = JSON.parseObject(response);
                if (JsonParams.isJsonRight(object))
                {
                    ToastUtil.show(UserAchievementActivity.this, "修改展示成就成功");
                    onRefresh();
                }
                else
                {
                    ToastUtil.show(UserAchievementActivity.this, object.getString(JsonParams.MESSAGE));
                }
            }

            @Override
            public void onFinish(Call call)
            {
                dismissDialog();
            }
        });
    }

    @Override
    protected void onViewClick(View view)
    {
        switch (view.getId())
        {
            case R.id.user_showing_achievement_1:
                try
                {
                    showingList.remove(0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                addShowing(0);
                break;
            case R.id.user_showing_achievement_2:
                try
                {
                    showingList.remove(1);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                addShowing(0);
                break;
        }
    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {

    }

    @Override
    public void onRefresh()
    {
        adapter.setShowAttained(isShowAttained);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, HttpUrls.HOST_URL_V5 + "user/" + userId + "/achievement/", 0, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {
            }

            @Override
            public void onError(Call call, Exception error)
            {
                ToastUtil.show(UserAchievementActivity.this, R.string.request_error);
                mSwipeUtil.onRefreshComplete();
                showErrorInfo(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showLoading();
                        headerView.startFirstCheck();
                    }
                });
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                hideLoad();
                mSwipeUtil.onRefreshComplete();
                JSONObject object = JSON.parseObject(response);
                if (JsonParams.isJsonRight(object))
                {

                    // 显示成就
                    JSONObject data = object.getJSONObject("data");
                    if (data != null)
                    {

                        // 已展示的成就
                        JSONArray showing = data.getJSONArray(JsonParams.SHOWING);
                        if (showing != null)
                        {
                            showingList.clear();
                            CommonUtils.getJSONListObject(showing, showingList, Integer.class);
                            headerView.setShowingAchievement(showingList);
                        }

                        dataList.clear();
                        JSONArray array;
                        if (isShowAttained)
                        {
                            array = data.getJSONArray(JsonParams.ATTAINED);
                        }
                        else
                        {
                            array = data.getJSONArray(JsonParams.UNATTAINED);
                        }
                        if (array != null && array.size() > 0)
                        {
                            CommonUtils.getJSONListObject(array, dataList, UserAchievementEntity.class);
                        }
                        adapter.notifyDataSetChanged();
                        recycler_view.setLoadMoreDataComplete();
                    }
                }
                else
                {
                    ToastUtil.show(UserAchievementActivity.this, object.getString(JsonParams.MESSAGE));
                }
            }

            @Override
            public void onFinish(Call call)
            {
                dismissDialog();
            }
        });
    }

    @Override
    public void onLeftCheck()
    {
        isShowAttained = true;
        showDialog();
        onRefresh();
    }

    @Override
    public void onCenterCheck()
    {
    }

    @Override
    public void onRightCheck()
    {
        isShowAttained = false;
        showDialog();
        onRefresh();
    }

    private void showDialog()
    {
        if (mDialog == null)
            mDialog = new SpinKitProgressDialog(this);
        mDialog.show();
    }

    private void dismissDialog()
    {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }
}
