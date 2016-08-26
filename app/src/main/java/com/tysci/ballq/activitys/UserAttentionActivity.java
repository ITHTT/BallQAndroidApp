package com.tysci.ballq.activitys;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.fragments.UserAttentionFragment;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.utils.HandlerUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;
import com.tysci.ballq.views.widgets.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/23.
 */
public class UserAttentionActivity extends BaseActivity
{
    @Bind(R.id.tab_layout)
    protected PagerSlidingTabStrip tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;

    private List<TextView> tvTitles = null;


    private String uid = null;
    private int followingCount;
    private int followerCount;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_user_attention;
    }

    @Override
    protected void initViews()
    {
    }

    @Override
    protected View getLoadingTargetView()
    {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent)
    {
        uid = intent.getStringExtra(Tag);
        if (TextUtils.isEmpty(uid))
        {
            uid = UserInfoUtil.getUserId(this);
        }
        setTitle(uid.equals(UserInfoUtil.getUserId(this)) ? "我的关注" : "关注的人");

        if (uid.equals(UserInfoUtil.getUserId(this)))
        {
            UserInfoEntity info = UserInfoUtil.getUserInfo(this);
            if (info != null)
            {
                followingCount = info.getFlc();
                followerCount = info.getFrc();
            }
            else
            {
                followingCount = 0;
                followerCount = 0;
            }
        }
        else
        {
            followingCount = intent.getIntExtra("flc", 0);
            followerCount = intent.getIntExtra("frc", 0);
        }
        addFragments();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LoadingProgressDialog dialog = new LoadingProgressDialog(this);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                new HandlerUtil().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (uid.equals(UserInfoUtil.getUserId(UserAttentionActivity.this)))
                        {
                            UserInfoEntity info = UserInfoUtil.getUserInfo(UserAttentionActivity.this);
                            if (info != null)
                            {
                                followingCount = info.getFlc();
                                followerCount = info.getFrc();
                            }
                            else
                            {
                                followingCount = 0;
                                followerCount = 0;
                            }
                        }
                    }
                });
            }
        });
        UserInfoUtil.getUserInfo(this, "", UserInfoUtil.getUserId(this), false, dialog);
    }

    private void addFragments()
    {
        String[] titles = new String[]{"关注 " + followingCount, "粉丝 " + followerCount};

        List<BaseFragment> fragments = new ArrayList<>(titles.length);
        tvTitles = new ArrayList<>(titles.length);
        for (int i = 0; i < titles.length; i++)
        {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.layout_attention_tab_title, null);
            TextView title = (TextView) view.findViewById(R.id.tv_tab_title);
            title.setText(titles[i]);
            tvTitles.add(title);
            tabLayout.addTab(view);
            UserAttentionFragment fragment = new UserAttentionFragment();
            fragment.setUid(uid);
            if (i == 0)
            {
                fragment.setEtype(1);
            }
            else
            {
                fragment.setEtype(0);
            }
            fragments.add(fragment);
        }
        BallQFragmentPagerAdapter adapter = new BallQFragmentPagerAdapter(this.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
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

    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {
        if (!TextUtils.isEmpty(action))
        {
            if (action.equals("user_attention"))
            {
                if (data != null)
                {
                    int etype = data.getInt("etype");
                    int count = data.getInt("count");
                    if (etype == 0)
                    {
                        TextView tvTitle = tvTitles.get(1);
                        tvTitle.setText("粉丝 " + count);
                    }
                    else if (etype == 1)
                    {
                        TextView tvTitle = tvTitles.get(0);
                        tvTitle.setText("关注 " + count);
                    }
                }
            }
            else if (action.equals("cancel_attention"))
            {
                int size = data.getInt("size");
                TextView tvTitle = tvTitles.get(0);
                tvTitle.setText("关注 " + size);
            }
        }
    }
}
