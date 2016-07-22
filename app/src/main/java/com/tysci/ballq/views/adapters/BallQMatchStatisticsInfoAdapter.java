package com.tysci.ballq.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQMatchStatisticsEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class BallQMatchStatisticsInfoAdapter extends BaseAdapter{
    List<BallQMatchStatisticsEntity> ballQMatchStatisticsEntityList;

    public BallQMatchStatisticsInfoAdapter(List<BallQMatchStatisticsEntity>datas){
        this.ballQMatchStatisticsEntityList=datas;
    }
    @Override
    public int getCount() {
        return ballQMatchStatisticsEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return ballQMatchStatisticsEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BallQMatchStatisticsInfoHolderView holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_game_statistics_item,parent,false);
            holder=new BallQMatchStatisticsInfoHolderView(convertView);
            convertView.setTag(holder);
        }else{
            holder= (BallQMatchStatisticsInfoHolderView) convertView.getTag();
        }

        BallQMatchStatisticsEntity info=ballQMatchStatisticsEntityList.get(position);
        holder.tvHomeTeamValue.setText(String.valueOf(info.getHomeTeamValue()));
        holder.tvMatchInfo.setText(info.getStatisticKey());
        holder.tvAwayTeamValue.setText(String.valueOf(info.getAwayTeamValue()));
        return convertView;
    }

    public static final class BallQMatchStatisticsInfoHolderView{
        TextView tvHomeTeamValue;
        TextView tvMatchInfo;
        TextView tvAwayTeamValue;

        public BallQMatchStatisticsInfoHolderView(View view){
            tvHomeTeamValue=(TextView)view.findViewById(R.id.tv_game_left_data);
            tvMatchInfo=(TextView)view.findViewById(R.id.tv_game_data);
            tvAwayTeamValue=(TextView)view.findViewById(R.id.tv_game_right_data);
        }
    }
}
