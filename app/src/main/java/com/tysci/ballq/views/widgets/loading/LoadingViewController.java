package com.tysci.ballq.views.widgets.loading;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;

/**
 * Created by Administrator on 2015/12/30.
 */
public class LoadingViewController {
    private LoadingViewHelperInterface helper;

    public LoadingViewController(View view) {
        this(new LoadingViewHelper(view));
    }

    public LoadingViewController(LoadingViewHelperInterface helper) {
        super();
        this.helper = helper;
    }

    public void showErrorInfo(String errorMsg, View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.layout_load_result);
        TextView textView = (TextView) layout.findViewById(R.id.message_info);
        TextView tvClick=(TextView)layout.findViewById(R.id.tv_click_info);
        if (!TextUtils.isEmpty(errorMsg)) {
            textView.setText(errorMsg);
        } else {
            textView.setText("当前网络不是很好");
        }

        ImageView imageView = (ImageView) layout.findViewById(R.id.message_icon);
        imageView.setImageResource(R.mipmap.page_icon_failed);

        if (null != onClickListener) {
            tvClick.setOnClickListener(onClickListener);
        }

        helper.showLayout(layout);
    }

    public void showEmptyInfo(String emptyMsg){
        showEmptyInfo(emptyMsg,null,null);
    }

    public void showEmptyInfo(String emptyMsg,View.OnClickListener onClickListener){
        showEmptyInfo(emptyMsg,null,onClickListener);
    }

    public void showEmptyInfo(String emptyMsg, String clickTipInfo,View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.layout_load_result);
        TextView textView = (TextView) layout.findViewById(R.id.message_info);
        TextView tvClick=(TextView)layout.findViewById(R.id.tv_click_info);
        if (!TextUtils.isEmpty(emptyMsg)) {
            textView.setText(emptyMsg);
        } else {
            textView.setText("暂无相关数据");
        }

        if(!TextUtils.isEmpty(clickTipInfo)){
            tvClick.setText(clickTipInfo);
        }else{
            tvClick.setText(clickTipInfo);
        }
        ImageView imageView = (ImageView) layout.findViewById(R.id.message_icon);
        imageView.setImageResource(R.mipmap.page_icon_empty);

        if (null != onClickListener) {
            tvClick.setVisibility(View.VISIBLE);
            tvClick.setOnClickListener(onClickListener);
        }else{
            tvClick.setVisibility(View.GONE);
        }

        helper.showLayout(layout);
    }

    public void showLoading(String msg) {
        View layout = helper.inflate(R.layout.layout_loading);
        if (!TextUtils.isEmpty(msg)) {
            TextView textView = (TextView) layout.findViewById(R.id.loading_msg);
            textView.setText(msg);
        }
        helper.showLayout(layout);
    }

    public void restore() {
        helper.restoreView();
    }

}
