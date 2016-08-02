package com.tysci.ballq.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.utils.CommonUtils;

/**
 * Created by LinDe on 2016-08-01 0001.
 *
 * @see com.tysci.ballq.fragments.BallQTipOffFragment
 */
public class BqTipOffPopupWindow extends PopupWindow implements View.OnClickListener
{
    private TextView tvCheckAll, tvCheckSoccer, tvCheckBasket;

    //    private final Context mContext;
    private OnCheckItemCallback mOnCheckItemCallback;

    public BqTipOffPopupWindow(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.view_popup_window_bq_tip_off, null);

        setContentView(contentView);
        setWidth(CommonUtils.dip2px(context, 150));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        initializingWidgets(contentView);

        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);// 设置可以点击泡泡之外关闭泡泡
        setFocusable(true);
    }

    private void initializingWidgets(View contentView)
    {
        tvCheckAll = (TextView) contentView.findViewById(R.id.tv_check_all);
        tvCheckSoccer = (TextView) contentView.findViewById(R.id.tv_check_soccer);
        tvCheckBasket = (TextView) contentView.findViewById(R.id.tv_check_basket);

        tvCheckAll.setSelected(true);
        tvCheckSoccer.setSelected(false);
        tvCheckBasket.setSelected(false);

        tvCheckAll.setOnClickListener(this);
        tvCheckSoccer.setOnClickListener(this);
        tvCheckBasket.setOnClickListener(this);
    }

    public void setOnCheckItemCallback(OnCheckItemCallback onCheckItemCallback)
    {
        mOnCheckItemCallback = onCheckItemCallback;
    }

    @Override
    public void onClick(View v)
    {
        if (mOnCheckItemCallback != null)
        {
            tvCheckAll.setSelected(false);
            tvCheckSoccer.setSelected(false);
            tvCheckBasket.setSelected(false);

            switch (v.getId())
            {
                case R.id.tv_check_all:
                    mOnCheckItemCallback.onCheckAllCallback();
                    tvCheckAll.setSelected(true);
                    dismiss();
                    break;
                case R.id.tv_check_soccer:
                    mOnCheckItemCallback.onCheckSoccerCallback();
                    tvCheckSoccer.setSelected(true);
                    dismiss();
                    break;
                case R.id.tv_check_basket:
                    mOnCheckItemCallback.onCheckBasketCallback();
                    tvCheckBasket.setSelected(true);
                    dismiss();
                    break;
            }
        }
    }

    public interface OnCheckItemCallback
    {
        void onCheckAllCallback();

        void onCheckSoccerCallback();

        void onCheckBasketCallback();
    }
}
