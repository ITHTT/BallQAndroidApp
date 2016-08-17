package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.modles.BallQUserGuessBettingRecordEntity;
import com.tysci.ballq.utils.BallQMatchStateUtil;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.MatchBettingInfoUtil;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderAdapter;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/6/20.
 */
public class BallQUserBettingGuessRecordAdapter extends RecyclerView.Adapter<BallQUserBettingGuessRecordAdapter.BallQUserBettingGuessRecordViewHolder>
        implements StickyHeaderAdapter<BallQUserBettingGuessRecordAdapter.BallQUserBettingGuessRecordViewHolder>
{
    private List<BallQUserGuessBettingRecordEntity> userGuessBettingRecordEntityList;

    public BallQUserBettingGuessRecordAdapter(List<BallQUserGuessBettingRecordEntity> userGuessBettingRecordEntityList)
    {
        this.userGuessBettingRecordEntityList = userGuessBettingRecordEntityList;
    }

    @Override
    public BallQUserBettingGuessRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_betting_record_item, parent, false);
        return new BallQUserBettingGuessRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQUserBettingGuessRecordViewHolder holder, int position)
    {
        final BallQUserGuessBettingRecordEntity info = userGuessBettingRecordEntityList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (info.getIst() != 1)
                    return;
                final Context context = v.getContext();

                BallQTipOffEntity tipInfo = new BallQTipOffEntity();
                tipInfo.setEid(info.getEid());
                tipInfo.setId(info.getId());
                Class cls = BallQTipOffDetailActivity.class;
                Intent intent = new Intent(context, cls);
                intent.putExtra(cls.getSimpleName(), tipInfo);
                context.startActivity(intent);
            }
        });
        Date date = CommonUtils.getDateAndTimeFromGMT(info.getCtime());
        if (date != null)
        {
            String dates = CommonUtils.getMMddString(date);
            if (!TextUtils.isEmpty(dates))
            {
                String[] ds = dates.split(" ");
                holder.tvBettingTime.setText(ds[ds.length - 1]);
                holder.tvBettingDate.setText(ds[0]);
            }
        }
        holder.tvBettingMatchName.setText(info.getTourname());
        Date mDate = CommonUtils.getDateAndTimeFromGMT(info.getMtime());
        if (mDate != null)
        {
            holder.tvBettingMatchDate.setText(CommonUtils.getMM_ddString(mDate));
        }
        holder.tvHomeTeamName.setText(info.getHtname());
        holder.tvAwayTeamName.setText(info.getAtname());
        holder.tvSam.setText(String.format(Locale.getDefault(), "%.0f", info.getSam() * 1F / 100));
        holder.tvBettingResultInfo.setText(MatchBettingInfoUtil.getBettingResultInfo(info.getChoice(), String.valueOf(info.getOtype()), info.getOdata()));
        if (info.getStatus() == 0)
        {
            holder.viewMark.setBackgroundResource(R.color.gold);
        }
        else
        {
            holder.viewMark.setBackgroundColor(Color.parseColor("#ff0000"));
        }

        String state = BallQMatchStateUtil.getMatchState(info.getMstatus(), info.getEtype());
        if (state.equals("未开始"))
        {
            if (mDate != null)
            {
                holder.tvMatchState.setText(CommonUtils.getTimeOfDay(mDate));
            }
        }
        else
        {
            String score = info.getHtscore() + " - " + info.getAtscore();
            holder.tvMatchState.setText(score);
        }
        setBettingResult(info, holder);
    }

    private void setBettingResult(BallQUserGuessBettingRecordEntity info, BallQUserBettingGuessRecordViewHolder holder)
    {
        int status = info.getStatus();
        switch (status)
        {
            case 1:
                /**赢*/
                holder.ivBetResult.setVisibility(View.VISIBLE);
                holder.tvBetResultSam.setVisibility(View.VISIBLE);
                holder.ivBetResult.setImageResource(R.mipmap.win_icon);
                holder.tvBetResultSam.setTextColor(Color.parseColor("#df575a"));
                break;
            case 2:
                /**输*/
                holder.ivBetResult.setVisibility(View.VISIBLE);
                holder.tvBetResultSam.setVisibility(View.VISIBLE);
                holder.ivBetResult.setImageResource(R.mipmap.lose_icon);
                holder.tvBetResultSam.setTextColor(Color.parseColor("#469c4a"));
                break;
            case 3:
                /**走*/
                holder.ivBetResult.setVisibility(View.VISIBLE);
                holder.tvBetResultSam.setVisibility(View.VISIBLE);
                holder.ivBetResult.setImageResource(R.mipmap.gone_icon);
                holder.tvBetResultSam.setTextColor(Color.parseColor("#a8a8a8"));
                break;
            default:
                holder.ivBetResult.setVisibility(View.GONE);
                holder.tvBetResultSam.setVisibility(View.GONE);
        }

        float resultValue = (info.getRam() - info.getSam()) / 100f;
        if (resultValue > 0)
        {
            holder.tvBetResultSam.setText("+" + String.format(Locale.getDefault(), "%.2f", resultValue));
        }
        else
        {
            holder.tvBetResultSam.setText(String.format(Locale.getDefault(), "%.2f", resultValue));
        }
    }

    @Override
    public int getItemCount()
    {
        return userGuessBettingRecordEntityList.size();
    }

    @Override
    public String getHeaderId(int position)
    {
        if (position >= 0 && position < getItemCount())
        {
            if (userGuessBettingRecordEntityList.get(position).getStatus() == 0)
            {
                return "0";
            }
            else
            {
                return "1";
            }
        }
        return null;
    }

    @Override
    public BallQUserBettingGuessRecordViewHolder onCreateHeaderViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_guess_betting_record_header, parent, false);
        return new BallQUserBettingGuessRecordViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(BallQUserBettingGuessRecordViewHolder viewholder, int position)
    {
        if (position >= 0 && position < getItemCount())
        {
            TextView title = (TextView) viewholder.itemView;
            if (userGuessBettingRecordEntityList.get(position).getStatus() == 0)
            {
                title.setText("未结算");
            }
            else
            {
                title.setText("已结算");
            }
        }
    }

    public static final class BallQUserBettingGuessRecordViewHolder extends RecyclerView.ViewHolder
    {
        View viewMark;

        TextView tvBettingTime;

        TextView tvBettingDate;

        TextView tvBettingMatchName;

        TextView tvBettingMatchDate;

        TextView tvHomeTeamName;

        TextView tvMatchState;

        TextView tvAwayTeamName;

        TextView tvBettingResultInfo;

        TextView tvSam;

        ImageView ivBetResult;

        TextView tvBetResultSam;

        public BallQUserBettingGuessRecordViewHolder(View itemView)
        {
            super(itemView);
            viewMark = itemView.findViewById(R.id.view_mark);
            tvBettingTime = (TextView) itemView.findViewById(R.id.tv_betting_time);
            tvBettingDate = (TextView) itemView.findViewById(R.id.tv_betting_date);
            tvBettingMatchName = (TextView) itemView.findViewById(R.id.tv_betting_match_name);
            tvBettingMatchDate = (TextView) itemView.findViewById(R.id.tv_betting_match_date);
            tvHomeTeamName = (TextView) itemView.findViewById(R.id.tv_home_team_name);
            tvMatchState = (TextView) itemView.findViewById(R.id.tv_match_state);
            tvAwayTeamName = (TextView) itemView.findViewById(R.id.tv_away_team_name);
            tvBettingResultInfo = (TextView) itemView.findViewById(R.id.tv_betting_result_info);
            tvSam = (TextView) itemView.findViewById(R.id.tvSam);
            ivBetResult = (ImageView) itemView.findViewById(R.id.ivBetResult);
            tvBetResultSam = (TextView) itemView.findViewById(R.id.tvBetResultSam);
        }
    }
}
