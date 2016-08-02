package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.base.WrapRecyclerAdapter;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.UserVStatusImageView;
import com.tysci.ballq.views.widgets.CircleImageView;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderAdapter;

import butterknife.Bind;

/**
 * Created by LinDe
 * on 2016-08-01 0001.
 */
public class BqIndexAdapter extends WrapRecyclerAdapter<JSONObject, BqIndexAdapter.ViewHolder> implements StickyHeaderAdapter<BqIndexAdapter.StickyViewHolder>
{
    private int mWeeklySize;
    private final int gridCounts;

    public BqIndexAdapter(int gridCounts)
    {
        this.gridCounts = gridCounts;
    }

    public void addData(JSONArray weekly, JSONArray overseas)
    {
//        mWeeklySize = 20;
//        addDataList(false);
//        for (int i = 0; i < 100; i++)
//        {
//            addDataList(true, new JSONObject());
//        }
        if (weekly != null && !weekly.isEmpty())
        {
            for (int i = 0; i < gridCounts; i++)
            {
                weekly.add(0, new JSONObject());
            }
            while (true)
            {
                mWeeklySize = weekly.size();
                if ((mWeeklySize) % gridCounts == 0)
                {
                    break;
                }
                weekly.add(new JSONObject());
            }
            addDataList(weekly, false, JSONObject.class);
        }
        else
        {
            mWeeklySize = 0;
        }
        if (overseas != null && !overseas.isEmpty())
        {
            for (int i = 0; i < gridCounts; i++)
            {
                overseas.add(0, new JSONObject());
            }
            addDataList(overseas, true, JSONObject.class);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_bq_index_adapter, parent, false);
        return new ViewHolder(itemView, viewType);
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, final JSONObject object, int position)
    {
        if (object == null || object.isEmpty())
        {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            return;
        }
        else
        {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        ImageUtil.loadImage(holder.circle_image_view, R.mipmap.icon_user_default, object.getString("pt"));

        holder.tv_user_nickname.setText(object.getString("fname"));
        holder.tv_content.setText(object.getString("note"));

        holder.iv_analyst.setUserV_Icon(object.getInteger("v_status"));

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Context context = v.getContext();
                UserInfoUtil.lookUserInfo(context, object.getInteger("uid"));
            }
        });
    }

    @Override
    public String getHeaderId(int position)
    {
        if (position >= mWeeklySize)
        {
            return "2";
        }
        return "1";
    }

    @Override
    public StickyViewHolder onCreateHeaderViewHolder(ViewGroup parent)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_bq_index_adapter_sticky, parent, false);
        return new StickyViewHolder(itemView);
    }

    @Override
    public void onBindHeaderViewHolder(StickyViewHolder holder, int position)
    {
        final String id = getHeaderId(position);
        if ("1".equals(id))
        {
            holder.iv.setImageResource(R.mipmap.icon_bq_index_page_analyst);
        }
        else if ("2".equals(id))
        {
            holder.iv.setImageResource(R.mipmap.icon_bq_index_page_overseas);
        }
    }

    class StickyViewHolder extends ButterKnifeRecyclerViewHolder
    {
        @Bind(R.id.image_view)
        ImageView iv;

        public StickyViewHolder(View itemView)
        {
            super(itemView);
        }
    }

    class ViewHolder extends ButterKnifeRecyclerViewHolder
    {
        @Bind(R.id.circle_image_view)
        CircleImageView circle_image_view;
        @Bind(R.id.iv_analyst)
        UserVStatusImageView iv_analyst;

        @Bind(R.id.tv_user_nickname)
        TextView tv_user_nickname;
        @Bind(R.id.tv_content)
        TextView tv_content;

        public ViewHolder(View itemView, int viewType)
        {
            super(itemView, viewType);
        }
    }
}
