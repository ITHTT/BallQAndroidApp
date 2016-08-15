package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.UserBettingGuessRecordActivity;
import com.tysci.ballq.modles.BallQTrendProfitStatisticEntity;
import com.tysci.ballq.utils.WeekDayUtil;

import java.util.List;
import java.util.Locale;

/**
 * Created by HTT on 2016/6/18.
 */
public class UserTrendProfitStatisticAdapter extends BaseAdapter
{
    private List<BallQTrendProfitStatisticEntity> trendProfitStatisticEntityList;

    private String bet, query;
    private int etype;
    private boolean isOldUser;

    public UserTrendProfitStatisticAdapter(List<BallQTrendProfitStatisticEntity> datas)
    {
        this.trendProfitStatisticEntityList = datas;
    }

    public void setOldUser(boolean isOldUser)
    {
        this.isOldUser = isOldUser;
    }

    public void setBet(String bet)
    {
        this.bet = bet;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public void setEtype(int etype)
    {
        this.etype = etype;
    }

    @Override
    public int getCount()
    {
        return trendProfitStatisticEntityList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return trendProfitStatisticEntityList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final UserTrendProfitStatisticViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trend_statistic_item, parent, false);
            holder = new UserTrendProfitStatisticViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (UserTrendProfitStatisticViewHolder) convertView.getTag();
        }
        final BallQTrendProfitStatisticEntity info = trendProfitStatisticEntityList.get(position);
        int type = info.getType();
        if (type == 1)
        {
            holder.tvTitle.setText(String.valueOf(info.getAhc_type()));
        }
        else if (type == 2)
        {
            holder.tvTitle.setText(info.getTournname());
        }
        else if (type == 3)
        {
            holder.tvTitle.setText(info.getMonth());
        }
        else if (type == 4)
        {
            holder.tvTitle.setText(String.valueOf(info.getTo_type()));
        }
        else if (type == 5)
        {
            String sam = String.format(Locale.getDefault(), "%.2f", info.getSam() * 1F / 100F);
            sam = sam + "(" + info.getAllq() + "场)";
            holder.tvTitle.setText(String.valueOf(sam));
        }
        else if (type == 6)
        {
            holder.tvTitle.setText(WeekDayUtil.getZhWeekDay(info.getWeekday()));
        }

        float value = info.getEarn();
        String earnText = (value > 0 ? "+" : "") + String.format(Locale.getDefault(), "%.2f", value / 100F);
        //noinspection deprecation
        holder.tvValue.setTextColor(Color.parseColor(value > 0 ? "#ce483d" : (value == 0 ? "#9b9b9b" : "#469c4a")));
        holder.tvValue.setText(earnText);
        final Context context = holder.itemView.getContext();
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, UserBettingGuessRecordActivity.class);
                intent.putExtra(UserBettingGuessRecordActivity.class.getSimpleName(), String.valueOf(info.getUid()));
                intent.putExtra("bet", bet);

                switch (bet)
                {
                    case "ahc":
                    case "month":
                    case "to":
                        intent.putExtra("query", holder.tvTitle.getText().toString());
                        break;
                    case "amount":
                        intent.putExtra("query", String.valueOf(info.getSam()));
                        break;
                    case "tourn":
                        intent.putExtra("query", String.valueOf(info.getTournid()));
                        break;
                    case "weekday":
                        intent.putExtra("query", String.valueOf(info.getWeekday()));
                        break;
                }
                intent.putExtra("etype", String.valueOf(etype));
                intent.putExtra("old_user", isOldUser);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public static final class UserTrendProfitStatisticViewHolder
    {
        public final View itemView;

        TextView tvTitle;
        TextView tvValue;

        public UserTrendProfitStatisticViewHolder(View view)
        {
            itemView = view;

            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvValue = (TextView) view.findViewById(R.id.tv_value);

        }
    }
}
