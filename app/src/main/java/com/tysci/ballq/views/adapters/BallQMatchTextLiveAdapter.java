package com.tysci.ballq.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQMatchTextLiveEntity;
import com.tysci.ballq.networks.GlideImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class BallQMatchTextLiveAdapter extends BaseAdapter{
    private List<BallQMatchTextLiveEntity> ballQMatchTextLiveEntityList;

    public BallQMatchTextLiveAdapter(List<BallQMatchTextLiveEntity>datas){
        this.ballQMatchTextLiveEntityList=datas;
    }

    @Override
    public int getCount() {
        return ballQMatchTextLiveEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return ballQMatchTextLiveEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BallQGameTextLiveHolderView holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_text_live_item,parent,false);
            holder=new BallQGameTextLiveHolderView(convertView);
            convertView.setTag(holder);
        }else{
            holder= (BallQGameTextLiveHolderView) convertView.getTag();
        }
        BallQMatchTextLiveEntity info=ballQMatchTextLiveEntityList.get(position);
        holder.tvGameTime.setText(info.getTime()+"'");
        holder.tvGameInfoContent.setText(info.getContent());
        if(position==0){
            holder.topLine.setVisibility(View.INVISIBLE);
        }else{
            holder.topLine.setVisibility(View.VISIBLE);
        }

        if(position==ballQMatchTextLiveEntityList.size()-1){
            holder.bottomLine.setVisibility(View.INVISIBLE);
        }else{
            holder.bottomLine.setVisibility(View.VISIBLE);
        }

        if(ballQMatchTextLiveEntityList.size()==1){
            holder.bottomLine.setVisibility(View.VISIBLE);
        }

        GlideImageLoader.loadImage(parent.getContext(),info.getIcon(),R.drawable.icon_default_team_logo,holder.ivGameInfoIcon);
        return convertView;
    }

    public static final class BallQGameTextLiveHolderView{
        View topLine;
        View bottomLine;
        TextView tvGameTime;
        ImageView ivGameInfoIcon;
        TextView tvGameInfoContent;

        public BallQGameTextLiveHolderView(View view){
            topLine=view.findViewById(R.id.top_line);
            bottomLine=view.findViewById(R.id.bottom_line);
            tvGameTime= (TextView) view.findViewById(R.id.tv_game_time);
            ivGameInfoIcon=(ImageView)view.findViewById(R.id.tv_game_info_icon);
            tvGameInfoContent=(TextView)view.findViewById(R.id.tv_game_info_content);
        }
    }
}
