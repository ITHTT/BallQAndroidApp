package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysci.ballq.R;

/**
 * Created by Administrator on 2016/7/18.
 */
public class BallQGoRankingAdapter extends RecyclerView.Adapter<BallQGoRankingAdapter.BallQGoRankingViewHolder>{

    @Override
    public BallQGoRankingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_go_ranking_item,parent,false);
        return new BallQGoRankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQGoRankingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static final class BallQGoRankingViewHolder extends RecyclerView.ViewHolder{
        TextView tvRank;
        TextView tvFName;
        TextView tvFightResult;
        TextView tvProfit;
        TextView tvYiellGap;

        public BallQGoRankingViewHolder(View itemView) {
            super(itemView);
            tvRank=(TextView)itemView.findViewById(R.id.rank);
            tvFName=(TextView)itemView.findViewById(R.id.fname);
            tvFightResult=(TextView)itemView.findViewById(R.id.tvFightResult);
            tvProfit=(TextView)itemView.findViewById(R.id.profit);
            tvYiellGap=(TextView)itemView.findViewById(R.id.yield_gap);
        }
    }
}
