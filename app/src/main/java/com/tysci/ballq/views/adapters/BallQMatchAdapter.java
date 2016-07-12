package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQMatchDetailActivity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.BallQMatchStateUtil;
import com.tysci.ballq.utils.CommonUtils;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/7.
 */
public class BallQMatchAdapter extends RecyclerView.Adapter<BallQMatchAdapter.BallQMatchViewHolder>{
    private List<BallQMatchEntity> ballQMatchEntityList=null;

    public BallQMatchAdapter(List<BallQMatchEntity> ballQMatchEntityList) {
        this.ballQMatchEntityList = ballQMatchEntityList;
    }

    @Override
    public BallQMatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_match_item,parent,false);
        return new BallQMatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQMatchViewHolder holder, int position) {
        final BallQMatchEntity info=ballQMatchEntityList.get(position);
        holder.tvMatchName.setText(info.getTourname());
        Date date= CommonUtils.getDateAndTimeFromGMT(info.getMtime());
        if(date!=null){
            holder.tvMatchTime.setText(CommonUtils.getTimeOfDay(date));
            holder.tvMatchDate.setText(CommonUtils.getMM_ddString(date));
        }else{
            holder.tvMatchTime.setText("");
            holder.tvMatchDate.setText("");
        }
        GlideImageLoader.loadImage(holder.itemView.getContext(),info.getHtlogo(), R.drawable.icon_default_team_logo,holder.ivHtLogo);
        holder.tvHtName.setText(info.getHtname());
        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getAtlogo(), R.drawable.icon_default_team_logo, holder.ivAtLogo);
        holder.tvAtName.setText(info.getAtname());
        holder.tvMatchState.setText(BallQMatchStateUtil.getMatchState(info.getStatus(), info.getEtype()));
        holder.tvTipNum.setText(String.valueOf(info.getTipcount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=holder.itemView.getContext();
                Intent intent=new Intent(context, BallQMatchDetailActivity.class);
                intent.putExtra(BallQMatchDetailActivity.class.getSimpleName(),info);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ballQMatchEntityList.size();
    }

    @Override
    public void onViewDetachedFromWindow(BallQMatchViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ButterKnife.unbind(holder);
    }

    public static final class BallQMatchViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tvMatchTime)
        TextView tvMatchTime;
        @Bind(R.id.tvMatchDate)
        TextView tvMatchDate;
        @Bind(R.id.ivHtLogo)
        ImageView ivHtLogo;
        @Bind(R.id.tvHtName)
        TextView tvHtName;
        @Bind(R.id.tvMatchName)
        TextView tvMatchName;
        @Bind(R.id.tvScore)
        TextView tvScore;
        @Bind(R.id.tvMatchState)
        TextView tvMatchState;
        @Bind(R.id.ivGameState)
        ImageView ivGameState;
        @Bind(R.id.tvTipNum)
        TextView tvTipNum;
        @Bind(R.id.vgBet)
        LinearLayout layoutBet;
        @Bind(R.id.tvBetNum)
        TextView tvBetNum;
        @Bind(R.id.ivAtLogo)
        ImageView ivAtLogo;
        @Bind(R.id.tvAtName)
        TextView tvAtName;
        @Bind(R.id.ivBell)
        ImageView ivBell;

        public BallQMatchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
