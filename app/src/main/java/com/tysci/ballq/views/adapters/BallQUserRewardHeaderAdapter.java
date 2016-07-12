package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQUserRewardHeaderEntity;
import com.tysci.ballq.networks.GlideImageLoader;

import java.util.List;

/**
 * Created by HTT on 2016/6/6.
 */
public class BallQUserRewardHeaderAdapter extends BaseAdapter {
    private List<BallQUserRewardHeaderEntity> userRewardHeaderEntityList;
    private Context context;

    public BallQUserRewardHeaderAdapter(Context context,List<BallQUserRewardHeaderEntity>list){
        this.context=context;
        this.userRewardHeaderEntityList=list;
    }
    @Override
    public int getCount() {
        return userRewardHeaderEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return userRewardHeaderEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BallQUserRewardHeaderViewHolder viewHolder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_ballq_user_reward_header_item,parent,false);
            viewHolder=new BallQUserRewardHeaderViewHolder();
            viewHolder.imageView= (ImageView) convertView;
            convertView.setTag(R.id.header_tag,viewHolder);
        }else{
            viewHolder= (BallQUserRewardHeaderViewHolder) convertView.getTag(R.id.header_tag);
        }

        BallQUserRewardHeaderEntity info=userRewardHeaderEntityList.get(position);
        GlideImageLoader.loadImage(context,info.getPt(),0,viewHolder.imageView);
        return convertView;
    }

    public static final class BallQUserRewardHeaderViewHolder{
        ImageView imageView;
    }
}
