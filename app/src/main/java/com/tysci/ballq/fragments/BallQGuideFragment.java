package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.utils.ImageUtil;

/**
 * Created by LinDe on 2016/6/20 0020.
 * BallQGuideFragment
 */
public class BallQGuideFragment extends BaseFragment {
    @Override
    protected int getViewLayoutId() {
        return R.layout.layout_only_image_view;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        ImageView iv = (ImageView) view;

        final Bundle b = getArguments();
        final int page = b.getInt(BallQGuideFragment.class.getName());
        switch (page) {
            case 0:
                ImageUtil.loadImage(iv, R.mipmap.guide_1);
                break;
            case 1:
                ImageUtil.loadImage(iv, R.mipmap.guide_2);
                break;
            case 2:
                ImageUtil.loadImage(iv, R.mipmap.guide_3);
                break;
            case 3:
                ImageUtil.loadImage(iv, R.mipmap.guide_4);
                break;
        }
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
}
