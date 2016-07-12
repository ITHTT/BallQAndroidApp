package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQMatchForecastDataEntity;
import com.tysci.ballq.modles.BallQMatchForecastDataEntity.MatchForecastDataEntity;
import com.tysci.ballq.utils.CommonUtils;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HTT on 2016/6/10.
 */
public class BallQMatchForecastDataAdapter extends RecyclerView.Adapter<BallQMatchForecastDataAdapter.BallQMatchForecastDataViewHolder>{
    private List<BallQMatchForecastDataEntity.MatchForecastDataEntity> matchForecastDataEntityList;

    public BallQMatchForecastDataAdapter(List<MatchForecastDataEntity> matchForecastDataEntityList) {
        this.matchForecastDataEntityList = matchForecastDataEntityList;
    }

    @Override
    public BallQMatchForecastDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_match_forecast_data,parent,false);
        return new BallQMatchForecastDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQMatchForecastDataViewHolder holder, int position) {
        MatchForecastDataEntity info=matchForecastDataEntityList.get(position);
        Date date= CommonUtils.getDateAndTimeFromGMT(info.getMatch_date());
        if(date!=null){
            holder.tvMatchDateInfo.setText(info.getTournament()+" "+ CommonUtils.getYYMMdd(date));
        }
        holder.tvMatchHomeTeam.setText(info.getHome_name());
        holder.tvMatchScoreState.setText(info.getHome_score()+"-"+info.getAway_score());
        holder.tvMatchAwayTeam.setText(info.getAway_name());
        holder.tvMatchPercent.setText(String.format(Locale.getDefault(),"%.0f",info.getRate())+"%");

        holder.tvMatchState.setText(info.getResult());
        if (TextUtils.isEmpty(info.getResult())) {
            /**
             * 没有比赛结果，隐藏背景
             */
            holder.tvMatchState.setBackgroundColor(0);
        } else {
//            holder.imageViewBackground.setVisibility(View.VISIBLE);
            /**
             * 赛果背景色
             */
            final int colorRed = R.drawable.icon_rounded_rectangle_ca574b;
            final int colorGreen = R.drawable.icon_rounded_rectangle_66b249;
            final int colorGray = R.drawable.icon_rounded_rectangle_c3c3c3;

            switch (info.getResult()) {
                case "上盘":
                case "大球":
                case "主胜":
                    holder.tvMatchState.setBackgroundResource(colorRed);
                    break;
                case "走盘":
                case "平局":
                    holder.tvMatchState.setBackgroundResource(colorGray);
                    break;
                case "下盘":
                case "小球":
                case "客胜":
                    holder.tvMatchState.setBackgroundResource(colorGreen);
                    break;
                default:
                    holder.tvMatchScoreState.setVisibility(View.GONE);
                    break;
            }
        }



    }

    @Override
    public int getItemCount() {
        return matchForecastDataEntityList.size();
    }

    public static final class BallQMatchForecastDataViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_match_date_info)
        protected TextView tvMatchDateInfo;
        @Bind(R.id.tv_match_home_team)
        protected TextView tvMatchHomeTeam;
        @Bind(R.id.tv_match_away_team)
        protected TextView tvMatchAwayTeam;
        @Bind(R.id.tv_match_score_state)
        protected TextView tvMatchScoreState;
        @Bind(R.id.tv_match_percent)
        protected TextView tvMatchPercent;
        @Bind(R.id.tv_match_state)
        protected TextView tvMatchState;

        public BallQMatchForecastDataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
