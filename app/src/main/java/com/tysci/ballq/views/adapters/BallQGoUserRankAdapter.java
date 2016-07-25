package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQGoUserRankEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class BallQGoUserRankAdapter extends RecyclerView.Adapter<BallQGoUserRankAdapter.BallQGoUserRankViewHolder>{
    private List<BallQGoUserRankEntity> userRankEntityList;

    public BallQGoUserRankAdapter(List<BallQGoUserRankEntity> userRankEntityList) {
        this.userRankEntityList = userRankEntityList;
    }

    @Override
    public BallQGoUserRankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_go_ranking_item,parent,false);
        return new BallQGoUserRankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQGoUserRankViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return userRankEntityList.size();
    }

    public static final class BallQGoUserRankViewHolder extends RecyclerView.ViewHolder{
        TextView tvRank;
        TextView tvUserName;
        TextView tvFightResult;
        TextView tvProfit;
        TextView tvYieldGap;

        public BallQGoUserRankViewHolder(View itemView) {
            super(itemView);
            tvRank=(TextView)itemView.findViewById(R.id.rank);
            tvUserName=(TextView)itemView.findViewById(R.id.fname);
            tvFightResult=(TextView)itemView.findViewById(R.id.tvFightResult);
            tvProfit=(TextView)itemView.findViewById(R.id.profit);
            tvYieldGap=(TextView)itemView.findViewById(R.id.yield_gap);
        }
    }
}
