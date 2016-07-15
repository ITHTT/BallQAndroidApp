package com.tysci.ballq.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQFindMenuEntity;
import com.tysci.ballq.networks.GlideImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class BallQFindMenuAdapter extends BaseAdapter{
    private List<BallQFindMenuEntity> findMenuList;

    public BallQFindMenuAdapter(List<BallQFindMenuEntity> finds){
        this.findMenuList=finds;
    }

    @Override
    public int getCount() {
        return this.findMenuList.size();
    }

    @Override
    public Object getItem(int position) {
        return findMenuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BallQFindContentViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_find_content_item,parent,false);
            holder=new BallQFindContentViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (BallQFindContentViewHolder) convertView.getTag();
        }
        BallQFindMenuEntity info=findMenuList.get(position);
        GlideImageLoader.loadImage(parent.getContext(),info.getPic_url(),R.mipmap.ic_ballq_logo,holder.ivFindIcon);
        holder.tvFindName.setText(info.getName());
        holder.tvFindBrief.setText(info.getNote());
        return convertView;
    }

    public static final class BallQFindContentViewHolder{
        ImageView ivFindIcon;
        TextView tvFindName;
        TextView tvFindBrief;

        public BallQFindContentViewHolder(View itemView) {
            ivFindIcon=(ImageView)itemView.findViewById(R.id.iv_find_icon);
            tvFindName=(TextView)itemView.findViewById(R.id.tv_find_title);
            tvFindBrief=(TextView)itemView.findViewById(R.id.tv_find_brief);

        }
    }
}
