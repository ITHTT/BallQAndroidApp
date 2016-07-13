package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQMatchLeagueTableEntity;
import com.tysci.ballq.networks.GlideImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/15.
 */
public class BallQMatchLeagueTableAdapter extends RecyclerView.Adapter<BallQMatchLeagueTableAdapter.BallQMatchLeagueTableViewHolder>{
    private List<BallQMatchLeagueTableEntity> matchLeagueTableEntityList=null;

    private boolean isFilter=true;

    public BallQMatchLeagueTableAdapter(List<BallQMatchLeagueTableEntity> matchLeagueTableEntityList) {
        this.matchLeagueTableEntityList = matchLeagueTableEntityList;
    }

    public void setFilter(boolean isFilter){
        this.isFilter=isFilter;
        notifyDataSetChanged();
    }

    @Override
    public BallQMatchLeagueTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_match_league_table_item,parent,false);
        return new BallQMatchLeagueTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQMatchLeagueTableViewHolder holder, int position) {
        BallQMatchLeagueTableEntity info=matchLeagueTableEntityList.get(position);

        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getTeam_logo(), R.drawable.icon_default_team_logo, holder.ivTeamLogo);
        holder.tvTeamRank.setText(String.valueOf(info.getRanking()));
        holder.tvTeamName.setText(info.getTeam());
        holder.tvRounds.setText(String.valueOf(info.getMatches_total()));
        holder.tvGoalDifference.setText(String.valueOf(info.getGoals_total() - info.getLost_total()));
        holder.tvWin.setText(String.valueOf(info.getWin_total()));
        holder.tvEqual.setText(String.valueOf(info.getDraw_total()));
        holder.tvLose.setText(String.valueOf(info.getLose_total()));
        holder.tvHaveConceded.setText(String.valueOf(info.getGoals_total() + ":" + info.getLost_total()));
        holder.tvIntegeral.setText(String.valueOf(info.getPoints_total()));
        if(isFilter){
            holder.tvWin.setVisibility(View.GONE);
            holder.tvEqual.setVisibility(View.GONE);
            holder.tvLose.setVisibility(View.GONE);
            holder.tvHaveConceded.setVisibility(View.GONE);
            holder.ivTeamLogo.setVisibility(View.VISIBLE);
            holder.tvGoalDifference.setVisibility(View.VISIBLE);
        }else{
            holder.tvWin.setVisibility(View.VISIBLE);
            holder.tvEqual.setVisibility(View.VISIBLE);
            holder.tvLose.setVisibility(View.VISIBLE);
            holder.tvHaveConceded.setVisibility(View.VISIBLE);
            holder.ivTeamLogo.setVisibility(View.GONE);
            holder.tvGoalDifference.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return matchLeagueTableEntityList.size();
    }

    @Override
    public void onViewDetachedFromWindow(BallQMatchLeagueTableViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ButterKnife.unbind(holder);
    }

    public static final class BallQMatchLeagueTableViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tvRanking)
        TextView tvTeamRank;
        @Bind(R.id.ivTeamLogo)
        ImageView ivTeamLogo;
        @Bind(R.id.tvTeamName)
        TextView tvTeamName;
        @Bind(R.id.tvRounds)
        TextView tvRounds;
        @Bind(R.id.tvGoalDifference)
        TextView tvGoalDifference;
        @Bind(R.id.tvWin)
        TextView tvWin;
        @Bind(R.id.tvEqual)
        TextView tvEqual;
        @Bind(R.id.tvLose)
        TextView tvLose;
        @Bind(R.id.tvHaveConceded)
        TextView tvHaveConceded;
        @Bind(R.id.tvIntegral)
        TextView tvIntegeral;

        public BallQMatchLeagueTableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
