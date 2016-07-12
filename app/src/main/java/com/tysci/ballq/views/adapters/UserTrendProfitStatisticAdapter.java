package com.tysci.ballq.views.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQTrendProfitStatisticEntity;
import com.tysci.ballq.utils.WeekDayUtil;

import java.util.List;
import java.util.Locale;

/**
 * Created by HTT on 2016/6/18.
 */
public class UserTrendProfitStatisticAdapter extends BaseAdapter{
    private List<BallQTrendProfitStatisticEntity>trendProfitStatisticEntityList;

    public UserTrendProfitStatisticAdapter(List<BallQTrendProfitStatisticEntity>datas){
        this.trendProfitStatisticEntityList=datas;
    }
    @Override
    public int getCount() {
        return trendProfitStatisticEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return trendProfitStatisticEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserTrendProfitStatisticViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trend_statistic_item,parent,false);
            holder=new UserTrendProfitStatisticViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (UserTrendProfitStatisticViewHolder) convertView.getTag();
        }
        BallQTrendProfitStatisticEntity info=trendProfitStatisticEntityList.get(position);
        int type=info.getType();
        if(type==1){
            holder.tvTitle.setText(String.valueOf(info.getAhc_type()));
        }else if(type==2){
            holder.tvTitle.setText(info.getTournname());
        } else if (type == 3) {
            holder.tvTitle.setText(info.getMonth());
        }else if(type==4){
            holder.tvTitle.setText(String.valueOf(info.getTo_type()));
        }else if(type==5){
            String sam = String.format(Locale.getDefault(), "%.2f", (float)info.getSam() / 100F);
            sam = sam + "(" + info.getAllq() + ")";
            holder.tvTitle.setText(String.valueOf(sam));
        }else if(type==6){
            holder.tvTitle.setText(WeekDayUtil.getZhWeekDay(info.getWeekday()));
        }

        float value=info.getEarn();
        String earnText = (value > 0 ? "+" : "") + String.format(Locale.getDefault(), "%.2f", value / 100F);
        //noinspection deprecation
        holder.tvValue.setTextColor(Color.parseColor(value > 0 ? "#ce483d": (value == 0 ? "#9b9b9b": "#469c4a")));
        holder.tvValue.setText(earnText);
        return convertView;
    }

    public static final class UserTrendProfitStatisticViewHolder{
        TextView tvTitle;
        TextView tvValue;
        public UserTrendProfitStatisticViewHolder(View view){
            tvTitle= (TextView) view.findViewById(R.id.tv_title);
            tvValue=(TextView)view.findViewById(R.id.tv_value);

        }
    }
}
