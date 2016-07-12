package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQMatchClashEntity;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderAdapter;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/6/13.
 */
public class BallQMatchClashAdapter extends RecyclerView.Adapter<BallQMatchClashAdapter.BallQMatchClashViewHolder>
        implements StickyHeaderAdapter<BallQMatchClashAdapter.BallQMatchClashViewHolder> {
    private List<BallQMatchClashEntity> matchClashEntityList=null;

    public BallQMatchClashAdapter(List<BallQMatchClashEntity> matchClashEntityList){
        this.matchClashEntityList=matchClashEntityList;
    }

    @Override
    public BallQMatchClashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_match_clash_data,parent,false);
        return new BallQMatchClashViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQMatchClashViewHolder holder, int position) {
        BallQMatchClashEntity info=matchClashEntityList.get(position);
        holder.tvMatchHTeam.setText(info.getHome_team());
        holder.tvMatchATeam.setText(info.getAway_team());
        Date date= CommonUtils.getDateAndTimeFromGMT(info.getMatch_time());
        if(date!=null){
            holder.tvMatchDate.setText(CommonUtils.getMMdd(date));
        }
        holder.tvMatchScoreState.setText(info.getHome_team_score()+":"+info.getAway_team_score());
    }

    @Override
    public int getItemCount() {
        return matchClashEntityList.size();
    }

    @Override
    public String getHeaderId(int position) {
        if(position>=0&&position<getItemCount()){
            return matchClashEntityList.get(position).getMatchType();
        }
        return "";
    }

    @Override
    public BallQMatchClashViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_match_clash_titiel,parent,false);
        return new BallQMatchClashViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(BallQMatchClashViewHolder viewholder, int position) {
        if(position>=0&&position<getItemCount()){
            TextView title= (TextView) viewholder.itemView.findViewById(R.id.tv_match_clash_title);
            title.setText(getHeaderId(position));
        }
    }

    public static final class BallQMatchClashViewHolder extends RecyclerView.ViewHolder{
        TextView tvMatchDate;
        TextView tvMatchHTeam;
        TextView tvMatchScoreState;
        TextView tvMatchATeam;
        RelativeLayout layoutMatchResult;
        ImageView ivMatchResult;

        public BallQMatchClashViewHolder(View itemView) {
            super(itemView);
            tvMatchDate= (TextView) itemView.findViewById(R.id.tv_match_date);
            tvMatchHTeam=(TextView)itemView.findViewById(R.id.tv_match_hteam);
            tvMatchScoreState=(TextView)itemView.findViewById(R.id.tv_match_score_state);
            tvMatchATeam=(TextView)itemView.findViewById(R.id.tv_match_ateam);
            layoutMatchResult=(RelativeLayout)itemView.findViewById(R.id.layout_match_result_state);
            ivMatchResult=(ImageView)itemView.findViewById(R.id.iv_match_result);
        }
    }
}
