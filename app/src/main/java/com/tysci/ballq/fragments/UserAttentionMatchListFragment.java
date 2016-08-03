package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQMatchAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/5/31.
 *
 * @author LinDe edit
 */
public class UserAttentionMatchListFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment {
    private List<BallQMatchEntity> matchEntityList;
    private BallQMatchAdapter adapter = null;

    @Override
    protected void onLoadMoreData() {
        requestDatas(currentPages, true);
    }

    @Override
    protected void onRefreshData() {
        requestDatas(1, false);
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        if (UserInfoUtil.checkLogin(baseActivity)) {
            showLoading();
            requestDatas(1, false);
        } else {
            showEmptyInfo("您尚未登录,登录后才可查看", "点击登录", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoUtil.userLogin(baseActivity);
                }
            });
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    private void requestDatas(final int pages, final boolean isLoadMore) {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder();
        sb.append(HttpUrls.HOST_URL_V6);
        sb.append("matches/following/");
        sb.append("?p=").append(pages);

        HashMap<String, String> params = null;
        if (UserInfoUtil.checkLogin(baseActivity)) {
            params = new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(baseActivity));
            params.put("token", UserInfoUtil.getUserToken(baseActivity));
        }

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, sb.toString(), params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if (!isLoadMore) {
                    if (adapter != null) {
                        recyclerView.setStartLoadMore();
                        ToastUtil.show(baseActivity, "请求失败");
                    } else {
                        showErrorInfo(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLoading();
                                requestDatas(pages, false);
                            }
                        });
                    }
                } else {
                    recyclerView.setLoadMoreDataFailed();
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                hideLoad();
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty() && obj.getIntValue("status") == 0) {
                        JSONArray arrays = obj.getJSONArray("data");
                        if (arrays != null && !arrays.isEmpty()) {
                            hideLoad();
                            if (matchEntityList == null) {
                                matchEntityList = new ArrayList<BallQMatchEntity>(10);
                            }
                            if (!isLoadMore) {
                                if (!matchEntityList.isEmpty()) {
                                    matchEntityList.clear();
                                }
                            }
                            CommonUtils.getJSONListObject(arrays, matchEntityList, BallQMatchEntity.class);
                            if (adapter == null) {
                                adapter = new BallQMatchAdapter(matchEntityList);
                                adapter.setTag(Tag);
                                adapter.setMatchType(1);
                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                            if (arrays.size() < 10) {
                                recyclerView.setLoadMoreDataComplete();
                            } else {
                                recyclerView.setStartLoadMore();
                                if (isLoadMore) {
                                    currentPages++;
                                } else {
                                    currentPages = 2;
                                }
                            }
                            return;
                        }
                    }
                }
                if (isLoadMore) {
                    recyclerView.setLoadMoreDataFailed();
                } else if (adapter == null) {
                    showEmptyInfo();
                }
            }

            @Override
            public void onFinish(Call call) {
                if (!isLoadMore) {
                    recyclerView.setRefreshComplete();
                    onRefreshCompelete();
                }
            }
        });
    }

    @Override
    protected boolean isCancledEventBus() {
        return false;
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {
        if (!TextUtils.isEmpty(action)) {
            if (action.equals("attention_refresh")) {
                if (adapter != null) {
                    if (adapter.getItemCount() > 0) {
                        matchEntityList.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
                recyclerView.setLoadMoreDataComplete();
                hideLoad();
                setRefreshing();
                requestDatas(1, false);
            }
        }

    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity) {
        super.userLogin(userInfoEntity);
        showLoading();
        requestDatas(1, false);
    }

    @Override
    protected void userExit() {
        super.userExit();
        HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
        if (matchEntityList != null && !matchEntityList.isEmpty()) {
            matchEntityList.clear();
            adapter.notifyDataSetChanged();
        }
        showEmptyInfo("您尚未登录,登录后才可查看", "点击登录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoUtil.userLogin(baseActivity);
            }
        });
    }
}
