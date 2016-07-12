package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQUserAttentionOrFansEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/1.
 */
public class UserAttentionOrFansAdapter extends RecyclerView.Adapter<UserAttentionOrFansAdapter.UserAttentionOrFansViweHolder>{
    private List<BallQUserAttentionOrFansEntity> userAttentionOrFansEntityList;
    private boolean isSelf=false;

    public UserAttentionOrFansAdapter(List<BallQUserAttentionOrFansEntity> userAttentionOrFansEntityList) {
        this.userAttentionOrFansEntityList = userAttentionOrFansEntityList;
    }

    public void setIsSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    @Override
    public UserAttentionOrFansViweHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_user_attention_item,parent,false);
        return new UserAttentionOrFansViweHolder(view);
    }

    @Override
    public void onBindViewHolder(UserAttentionOrFansViweHolder holder, int position) {
        BallQUserAttentionOrFansEntity info=userAttentionOrFansEntityList.get(position);
        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getPt(), R.mipmap.icon_user_default, holder.ivUserIcon);
        UserInfoUtil.setUserHeaderVMark(info.getIsv(),holder.iV,holder.ivUserIcon);
        holder.tvTipCount.setText(String.valueOf(info.getTipcount()));
        holder.tvUserNickName.setText(info.getFname());
        holder.tvRor.setText(String.format(Locale.getDefault(),"%.2f",info.getRor())+"%");
        holder.tvWins.setText(String.format(Locale.getDefault(),"%.2f",info.getWins())+"%");
        if(isSelf){
            holder.layoutPush.setVisibility(View.VISIBLE);
            holder.layoutAttention.setVisibility(View.VISIBLE);
            holder.divier.setVisibility(View.VISIBLE);
        }else{
            holder.layoutPush.setVisibility(View.GONE);
            holder.layoutAttention.setVisibility(View.GONE);
            holder.divier.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userAttentionOrFansEntityList.size();
    }

    public static final class UserAttentionOrFansViweHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserIcon;
        @Bind(R.id.isV)
        ImageView iV;
        @Bind(R.id.tvUserNickName)
        TextView tvUserNickName;
        @Bind(R.id.tvTipCount)
        TextView tvTipCount;
        @Bind(R.id.tvWins)
        TextView tvWins;
        @Bind(R.id.tvRor)
        TextView tvRor;
        @Bind(R.id.ivPush)
        ImageView ivPush;
        @Bind(R.id.ivAttention)
        ImageView ivAttention;
        @Bind(R.id.divider)
        View divier;
        @Bind(R.id.layout_push)
        FrameLayout layoutPush;
        @Bind(R.id.layout_attention)
        FrameLayout layoutAttention;

        public UserAttentionOrFansViweHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
