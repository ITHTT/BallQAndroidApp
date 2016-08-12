package com.tysci.ballq.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQBannerImageEntity;
import com.tysci.ballq.utils.BallQBusinessControler;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.views.IndexEventsView;
import com.tysci.ballq.views.widgets.convenientbanner.ConvenientBanner;
import com.tysci.ballq.views.widgets.convenientbanner.holder.CBViewHolderCreator;
import com.tysci.ballq.views.widgets.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LinDe
 * on 2016-08-01 0001.
 */
public class BqIndexHeaderView extends LinearLayout implements OnItemClickListener
{
    @Bind(R.id.convenientBanner)
    ConvenientBanner mConvenientBanner;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<BallQBannerImageEntity> bannerImageEntityList = null;

    @Bind(R.id.event1)
    IndexEventsView mIndexEventsView1;
    @Bind(R.id.event2)
    IndexEventsView mIndexEventsView2;
    @Bind(R.id.event3)
    IndexEventsView mIndexEventsView3;

    public BqIndexHeaderView(Context context)
    {
        this(context, null);
    }

    public BqIndexHeaderView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BqIndexHeaderView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializing(context);
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
    {
        mSwipeRefreshLayout = swipeRefreshLayout;
    }

    private void initializing(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.view_header_bq_home_page, this, true);

        ButterKnife.bind(this);

        mConvenientBanner.setBackgroundColor(Color.parseColor("#f6f6f6"));
        mConvenientBanner.setOnItemClickListener(this);
        mConvenientBanner.getViewPager().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_MOVE:
                        if (mSwipeRefreshLayout != null)
                            mSwipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (mSwipeRefreshLayout != null)
                        {
                            mSwipeRefreshLayout.setEnabled(true);
                        }
                }
                return false;
            }
        });
    }

    public void onPause()
    {
        mConvenientBanner.stopTurning();
    }

    public void onResume()
    {
        //开始自动翻页
        mConvenientBanner.startTurning(3000);
    }

    public void setBannerPictures(JSONArray picArray)
    {
        if (bannerImageEntityList == null || bannerImageEntityList.isEmpty())
        {
            if (bannerImageEntityList == null)
            {
                bannerImageEntityList = new ArrayList<>(picArray.size());
            }
            CommonUtils.getJSONListObject(picArray, bannerImageEntityList, BallQBannerImageEntity.class);
            mConvenientBanner.setPages(new CBViewHolderCreator()
            {
                @Override
                public Object createHolder()
                {
                    return new BannerNetworkImageView();
                }
            }, bannerImageEntityList)
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可不设
                    .setPageIndicator(new int[]{R.drawable.guide_gray, R.drawable.guide_red});
            mConvenientBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
            mConvenientBanner.setPointViewVisible(true);
            mConvenientBanner.setManualPageable(true);
        }
        else
        {
            bannerImageEntityList.clear();
            CommonUtils.getJSONListObject(picArray, bannerImageEntityList, BallQBannerImageEntity.class);
            mConvenientBanner.notifyDataSetChanged();
        }
    }
    public void setEvents(JSONArray eventsArray)
    {
        if (eventsArray == null || eventsArray.isEmpty())
            return;
        try
        {
            mIndexEventsView1.setJSONObject(eventsArray.getJSONObject(0));
        }
        catch (Exception e)
        {
            return;
        }
        try
        {
            mIndexEventsView2.setJSONObject(eventsArray.getJSONObject(1));
        }
        catch (Exception e)
        {
          return;
        }
        try
        {
            mIndexEventsView3.setJSONObject(eventsArray.getJSONObject(2));
        }
        catch (Exception ignored)
        {
        }
    }

    @Override
    public void onItemClick(int position)
    {
        if (bannerImageEntityList != null)
        {
            BallQBannerImageEntity info = bannerImageEntityList.get(position);
            BallQBusinessControler.businessControler(getContext(), Integer.parseInt(info.getJump_type()), info.getJump_url());
        }
    }
}
