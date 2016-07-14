package com.tysci.ballq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQFindCircleNoteActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.views.widgets.TitleBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQFindFragment extends BaseFragment{
    @Bind(R.id.title_bar)
    protected TitleBar titleBar;
    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_find;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
       titleBar.setTitleBarTitle("发现");
        titleBar.setTitleBarLeftIcon(0,null);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
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

    }

    @OnClick(R.id.layout_circle)
    protected void onClickCircle(View view){
        Intent intent=new Intent(baseActivity, BallQFindCircleNoteActivity.class);
        startActivity(intent);
    }
}
