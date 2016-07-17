package com.tysci.ballq.utils;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by LinDe on 2016-07-15 0015.
 *
 * @see SwipeRefreshLayout
 */
public final class SwipeUtil {
    private final SwipeRefreshLayout refreshLayout;
    private final Handler handler;

    private final Runnable handlerRefreshing = new Runnable() {
        @Override
        public void run() {
            refreshLayout.setRefreshing(true);
            refreshLayout.setEnabled(false);
        }
    };
    private final Runnable handlerRefreshComplete = new Runnable() {
        @Override
        public void run() {
            refreshLayout.setRefreshing(false);
            refreshLayout.setEnabled(true);
        }
    };

    public SwipeUtil(SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        this.handler = new Handler();
    }

    public void startRefreshing() {
        handler.post(handlerRefreshing);
    }

    public void onRefreshComplete() {
        onRefreshComplete(250);
    }

    public void onRefreshComplete(long delayMillis) {
        handler.postDelayed(handlerRefreshComplete, delayMillis);
    }
}
