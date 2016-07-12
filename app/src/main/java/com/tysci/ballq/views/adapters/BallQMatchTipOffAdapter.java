package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.MatchBettingInfoUtil;
import com.tysci.ballq.views.widgets.CustomRattingBar;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/10.
 */
public class BallQMatchTipOffAdapter extends RecyclerView.Adapter<BallQMatchTipOffAdapter.BallQMatchTipOffViewHolder>{
    private List<BallQTipOffEntity> matchTipOffList;

    public BallQMatchTipOffAdapter(List<BallQTipOffEntity> matchTipOffList) {
        this.matchTipOffList = matchTipOffList;
    }

    @Override
    public int getItemCount() {
        return matchTipOffList.size();
    }

    @Override
    public BallQMatchTipOffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_tip_off_item,parent,false);
        return new BallQMatchTipOffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQMatchTipOffViewHolder holder, int position) {
        BallQTipOffEntity info=matchTipOffList.get(position);

        Date date= CommonUtils.getDateAndTimeFromGMT(info.getCtime());
        if(date!=null){
            holder.tvTipCreatedDate.setText(CommonUtils.getDayOfMonth(date));
            String time= CommonUtils.getTimeOfDay(date);
            holder.tvTipCreatedTime.setText(time);

            String times[]=time.split(":");
            if(times!=null){
               int hour=Integer.parseInt(times[0]);
                if(hour>6&&hour<18) {
                    holder.ivTimeDayOrNight.setImageResource(R.mipmap.icon_match_tip_off_day_tag);
                }else{
                    holder.ivTimeDayOrNight.setImageResource(R.mipmap.icon_match_tip_off_night_tag);
                }
            }
        }

        GlideImageLoader.loadImage(holder.itemView.getContext(),info.getPt(), R.mipmap.icon_user_default,holder.ivUserIcon);
        holder.tvUserNickName.setText(info.getFname());
        holder.tvCommentNum.setText(String.valueOf(info.getComcount()));
        holder.tvContent.setText(info.getCont());
        holder.tvTipCount.setText(String.valueOf(info.getTipcount()));
        holder.tvWinPercent.setText(String.format(Locale.getDefault(), "%.2f", info.getWins() * 100) + "%");
        holder.tvBonCount.setText(String.valueOf(info.getBoncount()));
        holder.tvRor.setText(String.format(Locale.getDefault(),"%.2f",info.getRor())+"%");
        holder.tvChoice.setText(MatchBettingInfoUtil.getBettingResultInfo(info.getChoice(),info.getOtype(),info.getOdata()));
        holder.tvBettingNum.setText(String.valueOf(info.getSam()));

        if(info.getConfidence()==0){
            holder.layoutConfidence.setVisibility(View.GONE);
        }else{
            holder.layoutConfidence.setVisibility(View.VISIBLE);
            holder.rattingBar.setRattingValue(info.getConfidence()/10);
        }
    }

    @Override
    public void onViewDetachedFromWindow(BallQMatchTipOffViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ButterKnife.unbind(holder);
    }

    public static final class BallQMatchTipOffViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ivTimeDayOrNight)
        ImageView ivTimeDayOrNight;
        @Bind(R.id.tvTipCreateTime)
        TextView tvTipCreatedTime;
        @Bind(R.id.tvTipCreateDate)
        TextView tvTipCreatedDate;
        @Bind(R.id.tvUserNickName)
        TextView tvUserNickName;
        @Bind(R.id.ivUserIcon)
        ImageView ivUserIcon;
        @Bind(R.id.tvCommentNum)
        TextView tvCommentNum;
        @Bind(R.id.tvTipCount)
        TextView tvTipCount;
        @Bind(R.id.tvWinPercent)
        TextView tvWinPercent;
        @Bind(R.id.tvRor)
        TextView tvRor;
        @Bind(R.id.tvBonCount)
        TextView tvBonCount;
        @Bind(R.id.tvChoice)
        TextView tvChoice;
        @Bind(R.id.tvBettingNum)
        TextView tvBettingNum;
        @Bind(R.id.layoutConfidence)
        LinearLayout layoutConfidence;
        @Bind(R.id.rating_bar)
        CustomRattingBar rattingBar;
        @Bind(R.id.tvContent)
        TextView tvContent;

        public BallQMatchTipOffViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
