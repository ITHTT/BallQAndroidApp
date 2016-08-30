package com.tysci.ballq.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.tysci.ballq.R;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.modles.event.EventType;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.widgets.TitleBar;
import com.tysci.ballq.views.widgets.loading.LoadingViewController;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Created by HTT on 2016/5/28.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener
{
    protected final String Tag = this.getClass().getSimpleName();
    protected TitleBar titleBar;
    protected LoadingViewController loadingViewController;

    private static int resumeNumber = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)//获取当前页面
                .setSwipeBackEnable(true)//设置是否可滑动
                .setSwipeEdge(30)//可滑动的范围。px。200表示为左边200px的屏幕
//                .setSwipeEdgePercent(0.2f)//可滑动的范围。百分比。0.2表示为左边20%的屏幕
                .setSwipeSensitivity(0.5f)//对横向滑动手势的敏感程度。0为迟钝 1为敏感
//                .setScrimColor(getResources().getColor(R.color.gold))//底层阴影颜色
                .setClosePercent(0.8f)//触发关闭Activity百分比
                .setSwipeRelateEnable(true)//是否与下一级activity联动(微信效果)。默认关
                .setSwipeRelateOffset(300)//activity联动时的偏移量。默认500px。
                .setDisallowInterceptTouchEvent(false);//不抢占事件，默认关（事件将先由子View处理再由滑动关闭处理）
//                .addListener(new SwipeListener()
//                {//滑动监听
//
//                    @Override
//                    public void onScroll(float percent, int px)
//                    {//滑动的百分比与距离
//                    }
//
//                    @Override
//                    public void onEdgeTouch()
//                    {//当开始滑动
//                    }
//
//                    @Override
//                    public void onScrollToClose()
//                    {//当滑动关闭
//                    }
//                });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getContentViewId());
        titleBar = (TitleBar) this.findViewById(R.id.title_bar);
        setTitleBarLeftIcon(R.mipmap.icon_back_gold);
        if (!isCanceledEventBus())
        {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        if (getLoadingTargetView() != null)
        {
            loadingViewController = new LoadingViewController(getLoadingTargetView());
        }
        initViews();
        if (getIntent() != null)
        {
            getIntentData(this.getIntent());
        }
        handleInstanceState(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    protected void setTitle(String title)
    {
        if (titleBar != null)
        {
            titleBar.setTitleBarTitle(title);
        }
    }

    protected <T> void setTitleText(T title)
    {
        if (titleBar != null)
        {
            String result;
            if (title == null)
            {
                result = "";
            }
            else if (title instanceof Integer)
            {
                try
                {
                    result = getResources().getString((Integer) title);
                }
                catch (Resources.NotFoundException e)
                {
                    result = title.toString();
                }
            }
            else
            {
                result = title.toString();
            }
            titleBar.setTitleBarTitle(result);
        }
    }

    public TitleBar getTitleBar()
    {
        return titleBar;
    }

    protected void setTitleBarLeftIcon(int res)
    {
        if (titleBar != null)
        {
            titleBar.setTitleBarLeftIcon(res, this);
        }
    }

    /**
     * 获取界面布局文件的ID
     */
    protected abstract int getContentViewId();

    /**
     * 初始化控件
     */
    protected abstract void initViews();

    /**
     * 设置加载效果所在布局的目标视图
     */
    protected abstract View getLoadingTargetView();

    /**
     * 获取Intent中的数据
     */
    protected abstract void getIntentData(Intent intent);

    /**
     * 是否取消EventBus
     */
    protected abstract boolean isCanceledEventBus();

    /**
     * 保存异常时的数据
     */
    protected abstract void saveInstanceState(Bundle outState);

    /**
     * 处理异常时的情况
     */
    protected abstract void handleInstanceState(Bundle savedInstanceState);

    /**
     * 控件点击事件
     */
    protected abstract void onViewClick(View view);

    protected abstract void notifyEvent(String action);

    protected abstract void notifyEvent(String action, Bundle data);

    /**
     * 用户登录
     */
    protected void userLogin(UserInfoEntity userInfoEntity)
    {

    }

    /**
     * 用户退出
     */
    protected void userExit()
    {

    }

    protected void back()
    {
        this.finish();
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.iv_titlebar_left)
        {
            back();
        }
        onViewClick(v);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        saveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        handleInstanceState(savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventObject eventObject)
    {
        if (eventObject != null)
        {
            String action = eventObject.getEventAction();
            if (action.equals(EventType.EVENT_USER_LOGIN))
            {
                String data = eventObject.getData().getString("user_info");
                if (!TextUtils.isEmpty(data))
                {
                    UserInfoEntity userInfoEntity = JSONObject.parseObject(data, UserInfoEntity.class);
                    if (userInfoEntity != null)
                    {
                        userLogin(userInfoEntity);
                    }
                }
            }
            else
            {
                SparseArray<Class> receivers = eventObject.getReceivers();
                if (receivers.size() > 0)
                {
                    int size = receivers.size();
                    for (int i = 0; i < size; i++)
                    {
                        if (receivers.valueAt(i) == this.getClass())
                        {
                            notifyEvent(action, eventObject.getData());
                        }
                    }
                }
                else
                {
                    notifyEvent(action, eventObject.getData());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(String action)
    {
        if (action.equals(EventType.EVENT_USER_EXIT))
        {
            userExit();
        }
        else
        {
            notifyEvent(action);
        }

    }

    protected void showLoading()
    {
        if (loadingViewController != null)
            loadingViewController.showLoading(null);
    }

    protected void showErrorInfo(View.OnClickListener onClickListener)
    {
        if (loadingViewController != null)
            loadingViewController.showErrorInfo("当前网络不是很好", onClickListener);
    }

    protected void showEmptyInfo()
    {
        if (loadingViewController != null)
            loadingViewController.showEmptyInfo("暂无相关数据");
    }

    protected void showEmptyInfo(String empty)
    {
        if (loadingViewController != null)
            loadingViewController.showEmptyInfo(empty);
    }

    protected void showEmptyInfo(String emptyInfo, String clickInfo, View.OnClickListener clickListener)
    {
        if (loadingViewController != null)
            loadingViewController.showEmptyInfo(emptyInfo, clickInfo, clickListener);
    }

    protected void hideLoad()
    {
        if (loadingViewController != null)
            loadingViewController.restore();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        resumeNumber++;
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(Tag);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        resumeNumber--;
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(Tag);
    }

    public static boolean isForeground()
    {
        return resumeNumber > 0;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
        loadingViewController = null;
        ButterKnife.unbind(this);
        if (!isCanceledEventBus())
        {
            EventBus.getDefault().unregister(this);
        }
        /**取消网络请求*/
        HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        KLog.e("finalize...");
    }

    @Override
    public void finish()
    {
//        SwipeBackHelper.finish(this);
        super.finish();
    }
}
