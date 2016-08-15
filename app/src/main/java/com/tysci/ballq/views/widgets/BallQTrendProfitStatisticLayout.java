package com.tysci.ballq.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQTrendProfitStatisticEntity;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.UserTrendProfitStatisticAdapter;
import com.tysci.ballq.views.widgets.expandablelayout.ExpandableLayoutListenerAdapter;
import com.tysci.ballq.views.widgets.expandablelayout.ExpandableLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HTT on 2016/6/18.
 */
public class BallQTrendProfitStatisticLayout extends LinearLayout {
    private TextView tvTypeName;
    private ExpandableLinearLayout layoutExpandable;
    private ListView mListView;
    private ImageView ivArrow;
    private View divider;

    private List<BallQTrendProfitStatisticEntity> trendProfitStatisticEntityList = null;
    private UserTrendProfitStatisticAdapter adapter = null;

    public BallQTrendProfitStatisticLayout(Context context) {
        super(context);
        initViews(context);
    }

    public BallQTrendProfitStatisticLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public BallQTrendProfitStatisticLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BallQTrendProfitStatisticLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_trend_profit_statistic_item, this, true);
        tvTypeName = (TextView) this.findViewById(R.id.tv_type_name);
        layoutExpandable = (ExpandableLinearLayout) this.findViewById(R.id.layout_expandable);
        mListView = (ListView) this.findViewById(R.id.lv);
        ivArrow = (ImageView) this.findViewById(R.id.ivBtn);
        divider = this.findViewById(R.id.divider);
        this.findViewById(R.id.layout_type).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("dianji....");
                layoutExpandable.toggle();
//                if(layoutExpandable.isExpanded()){
//                    layoutExpandable.collapse();
//                }else{
//                    layoutExpandable.expand();
//                }
            }
        });
        layoutExpandable.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                ivArrow.setImageResource(R.mipmap.btn_up);
                divider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPreClose() {
                ivArrow.setImageResource(R.mipmap.btn_down);
                divider.setVisibility(View.GONE);
            }
        });
    }

    public void setTrendProfitTitle(String title) {
        tvTypeName.setText(title);
    }

    public void setTrendProfitStatistValue(String bet, String query, int etype,boolean isOldUser, List<BallQTrendProfitStatisticEntity> datas) {
        if (datas != null && !datas.isEmpty()) {
            if (trendProfitStatisticEntityList == null) {
                trendProfitStatisticEntityList = new ArrayList<>();
                trendProfitStatisticEntityList.addAll(datas);
                adapter = new UserTrendProfitStatisticAdapter(trendProfitStatisticEntityList);
                mListView.setAdapter(adapter);
            } else {
                if (!trendProfitStatisticEntityList.isEmpty()) {
                    trendProfitStatisticEntityList.clear();
                }
                trendProfitStatisticEntityList.addAll(datas);
                adapter.notifyDataSetChanged();
            }
            adapter.setBet(bet);
            adapter.setQuery(query);
            adapter.setEtype(etype);
            adapter.setOldUser(isOldUser);
            layoutExpandable.initLayout(true);
        }
    }


}
