package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.UserProfileActivity;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.base.WrapRecyclerAdapter;
import com.tysci.ballq.utils.CalendarUtil;

import java.util.Locale;

import butterknife.Bind;

/**
 * Created by LinDe on 2016-07-26 0026.
 *
 * @see com.tysci.ballq.fragments.BallQGoRankListFragment
 */
public class BallQGoRankListAdapter extends WrapRecyclerAdapter<JSONObject, BallQGoRankListAdapter.ViewHolder>
{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_go_ruanking_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, final JSONObject json, int position)
    {
        if (position == 0)
        {
            holder.layoutDate.setVisibility(View.VISIBLE);
            holder.layoutMsg.setVisibility(View.VISIBLE);
            holder.imageView1.setVisibility(View.VISIBLE);
            CalendarUtil cal = new CalendarUtil();
            holder.tvDate.setText(cal.getStringFormat("MM-dd"));
        }
        else
        {
            holder.layoutDate.setVisibility(View.GONE);
            holder.layoutMsg.setVisibility(View.GONE);
            holder.imageView1.setVisibility(View.GONE);
        }

        holder.rank.setText(json.getString("rank"));
        holder.fname.setText(json.getString("fname"));

        holder.tvFightResult.setText(json.getString("win"));
        holder.tvFightResult.append("赢");
        holder.tvFightResult.append(json.getString("lose"));
        holder.tvFightResult.append("输");
        holder.tvFightResult.append(json.getString("go"));
        holder.tvFightResult.append("走");

        int profit = json.getInteger("profit");
        holder.profit.setText(String.format(Locale.getDefault(), "%.2f", profit * 1F / 100F));

        int yield_gap = json.getInteger("yield_gap");
        holder.yield_gap.setText(String.format(Locale.getDefault(), "%.2f", yield_gap * 1F / 100F));

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Class<UserProfileActivity> cls = UserProfileActivity.class;
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, cls);
                intent.putExtra(cls.getSimpleName(), Integer.parseInt(json.getString("user_id")));
                context.startActivity(intent);
            }
        });
    }

    class ViewHolder extends ButterKnifeRecyclerViewHolder
    {
        @Bind(R.id.layoutDate)
        ViewGroup layoutDate;
        @Bind(R.id.tvDate)
        TextView tvDate;// 日期

        @Bind(R.id.layoutMsg)
        ViewGroup layoutMsg;
        @Bind(R.id.imageView1)
        ImageView imageView1;

        @Bind(R.id.rank)
        TextView rank;
        @Bind(R.id.fname)
        TextView fname;
        @Bind(R.id.tvFightResult)
        TextView tvFightResult;
        @Bind(R.id.profit)
        TextView profit;
        @Bind(R.id.yield_gap)
        TextView yield_gap;

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
