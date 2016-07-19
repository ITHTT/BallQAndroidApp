package com.tysci.ballq.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.modles.event.EventType;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.views.widgets.loading.LoadingViewController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Created by HTT on 2016/5/28.
 */
public abstract class BaseFragment extends Fragment {
    protected final String Tag = this.getClass().getSimpleName();
    protected BaseActivity baseActivity;
    protected LoadingViewController loadingViewController;
    protected View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!isCancledEventBus()) {
            EventBus.getDefault().register(this);
        }
        View view = inflater.inflate(getViewLayoutId(), container, false);
        contentView = view;
        ButterKnife.bind(this, view);
        if (getLoadingTargetView() != null) {
            loadingViewController = new LoadingViewController(getLoadingTargetView());
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.baseActivity = (BaseActivity) context;
    }

    /**
     * 获取布局ID
     */
    protected abstract int getViewLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initViews(View view, Bundle savedInstanceState);

    /**
     * 设置加载效果所在布局的目标视图
     */
    protected abstract View getLoadingTargetView();

    /**
     * 是否取消EventBus
     */
    protected abstract boolean isCancledEventBus();

    protected abstract void notifyEvent(String action);

    protected abstract void notifyEvent(String action, Bundle data);

    /**
     * 用户登录
     */
    protected void userLogin(UserInfoEntity userInfoEntity) {

    }

    /**
     * 用户退出
     */
    protected void userExit() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventObject eventObject) {
        if (eventObject != null) {
            String action = eventObject.getEventAction();
            if (action.equals(EventType.EVENT_USER_LOGIN)) {
                String data = eventObject.getData().getString("user_info");
                if (!TextUtils.isEmpty(data)) {
                    UserInfoEntity userInfoEntity = JSONObject.parseObject(data, UserInfoEntity.class);
                    if (userInfoEntity != null) {
                        userLogin(userInfoEntity);
                    }
                }
            } else {
                SparseArray<Class> receivers = eventObject.getReceivers();
                if (receivers.size() > 0) {
                    int size = receivers.size();
                    for (int i = 0; i < size; i++) {
                        if (receivers.valueAt(i) == this.getClass()) {
                            notifyEvent(action, eventObject.getData());
                        }
                    }
                } else {
                    notifyEvent(action, eventObject.getData());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(String action) {
        if (action.equals(EventType.EVENT_USER_EXIT)) {
            userExit();
        } else {
            notifyEvent(action);
        }
    }

    protected void showLoading() {
        if (loadingViewController != null)
            loadingViewController.showLoading(null);
    }

    protected void showErrorInfo(View.OnClickListener onClickListener) {
        if (loadingViewController != null)
            loadingViewController.showErrorInfo("当前网络不是很好", onClickListener);
    }

    protected void showEmptyInfo() {
        if (loadingViewController != null)
            loadingViewController.showEmptyInfo("暂无相关数据");
    }

    protected void showEmptyInfo(String empty) {
        if (loadingViewController != null)
            loadingViewController.showEmptyInfo(empty);
    }

    protected void showEmptyInfo(String emptyInfo, String clickInfo, View.OnClickListener clickListener) {
        if (loadingViewController != null)
            loadingViewController.showEmptyInfo(emptyInfo, clickInfo, clickListener);
    }

    protected void hideLoad() {
        if (loadingViewController != null)
            loadingViewController.restore();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadingViewController = null;
        HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
        if (!isCancledEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }
}
