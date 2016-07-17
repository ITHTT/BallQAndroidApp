package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQUserRankInfoEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQTipOffUserInfoAdapter extends RecyclerView.Adapter<BallQTipOffUserInfoAdapter.BallQTipOffUserInfoViewHolder>{
    private List<BallQUserRankInfoEntity> rankInfoEntityList;

    public BallQTipOffUserInfoAdapter(List<BallQUserRankInfoEntity> rankInfoEntityList) {
        this.rankInfoEntityList = rankInfoEntityList;
    }

    @Override
    public BallQTipOffUserInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tip_off_user_item,parent,false);
        return new BallQTipOffUserInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQTipOffUserInfoViewHolder holder, int position) {
        BallQUserRankInfoEntity info=rankInfoEntityList.get(position);
        holder.tvUuserName.setText(info.getFname());
        holder.tvUserTipCount.setText(String.valueOf(info.getTipcount()));
        holder.tvUserAllProfit.setText(String.valueOf(info.getTearn()));
        holder.tvUserRecommendCount.setText(String.valueOf(info.getFrc()));
        holder.ivAttention.setSelected(info.getIsf()==1);
    }

    @Override
    public int getItemCount() {
        return rankInfoEntityList.size();
    }

    public static final class BallQTipOffUserInfoViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_user_name)
        TextView tvUuserName;
        @Bind(R.id.tv_user_tip_count)
        TextView tvUserTipCount;
        @Bind(R.id.tv_user_recommend_count)
        TextView tvUserRecommendCount;
        @Bind(R.id.tv_user_all_profit)
        TextView tvUserAllProfit;
        @Bind(R.id.iv_attention)
        ImageView ivAttention;

        public BallQTipOffUserInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
