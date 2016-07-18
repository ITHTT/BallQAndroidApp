package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQUserRankInfoEntity;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HTT on 2016/7/18.
 */
public class BallQMainUserRankingAdapter extends RecyclerView.Adapter<BallQMainUserRankingAdapter.BallQMainUserRankingViewHolder>{
    private Map<String,List<BallQUserRankInfoEntity>> datas;
    private List<String>keys;

    public BallQMainUserRankingAdapter(Map<String, List<BallQUserRankInfoEntity>> datas) {
        this.datas = datas;
        keys=new ArrayList<>(5);
        keys.addAll(datas.keySet());
    }

    @Override
    public BallQMainUserRankingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_index_rank_item,parent,false);
        return new BallQMainUserRankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQMainUserRankingViewHolder holder, int position) {
        String key=keys.get(position);
        holder.rankType.setText(key);
        List<BallQUserRankInfoEntity> rankInfoEntityList=datas.get(key);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static final class BallQMainUserRankingViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.rankType)
        TextView rankType;
        @Bind(R.id.imageView1)
        CircleImageView imageView1;
        @Bind(R.id.imageView2)
        CircleImageView imageView2;
        @Bind(R.id.imageView3)
        CircleImageView imageView3;
        @Bind(R.id.imageView4)
        CircleImageView imageView4;
        @Bind(R.id.imageView5)
        CircleImageView imageView5;
        @Bind(R.id.iv_1)
        ImageView ivV1;
        @Bind(R.id.iv_2)
        ImageView ivV2;
        @Bind(R.id.iv_3)
        ImageView ivV3;
        @Bind(R.id.iv_4)
        ImageView ivV4;
        @Bind(R.id.iv_5)
        ImageView ivV5;
        @Bind(R.id.textView1)
        TextView textView1;
        @Bind(R.id.textView2)
        TextView textView2;
        @Bind(R.id.textView3)
        TextView textView3;
        @Bind(R.id.textView4)
        TextView textView4;
        @Bind(R.id.textView5)
        TextView textView5;
        @Bind(R.id.ivLine1)
        ImageView ivLine1;
        @Bind(R.id.ivLine2)
        ImageView ivLine2;
        @Bind(R.id.vgRankNum)
        ViewGroup vgRankNum;
        @Bind(R.id.tvRankNum1)
        TextView tvRankNum1;
        @Bind(R.id.tvRankNum2)
        TextView tvRankNum2;
        @Bind(R.id.tvRankNum3)
        TextView tvRankNum3;
        @Bind(R.id.tvRankNum4)
        TextView tvRankNum4;
        @Bind(R.id.tvRankNum5)
        TextView tvRankNum5;
        @Bind(R.id.vgRounds)
        ViewGroup vgRounds;
        @Bind(R.id.tvRounds1)
        TextView tvRounds1;
        @Bind(R.id.tvRounds2)
        TextView tvRounds2;
        @Bind(R.id.tvRounds3)
        TextView tvRounds3;
        @Bind(R.id.tvRounds4)
        TextView tvRounds4;
        @Bind(R.id.tvRounds5)
        TextView tvRounds5;

        public BallQMainUserRankingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
