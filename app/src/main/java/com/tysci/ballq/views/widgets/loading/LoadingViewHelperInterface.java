package com.tysci.ballq.views.widgets.loading;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2015/12/30.
 */
public interface LoadingViewHelperInterface {

    public abstract View getCurrentLayout();

    public abstract void restoreView();

    public abstract void showLayout(View view);

    public abstract View inflate(int layoutId);

    public abstract Context getContext();

    public abstract View getView();

}
