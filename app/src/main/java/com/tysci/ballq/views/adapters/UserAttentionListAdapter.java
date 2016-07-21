package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQBallWarpDetailActivity;
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.base.WrapRecyclerAdapter;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.modles.UserAttentionListEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.CalendarUtil;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.MatchBettingInfoUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.Date;

import butterknife.Bind;

/**
 * Created by Administrator on 2016-07-20 0020.
 *
 * @see com.tysci.ballq.fragments.UserAttentionMatchListFragment
 */
public class UserAttentionListAdapter extends WrapRecyclerAdapter<UserAttentionListEntity, UserAttentionListAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case 1:// 比赛
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_attention_list_match_item, parent, false);
                break;
            case 2:// 爆料
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_tip_off_attention_item, parent, false);
                break;
            case 3:// 球茎
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_ball_warp_item, parent, false);
                break;
        }
        if (itemView != null) {
            return new ViewHolder(itemView, viewType);
        }
        return null;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, UserAttentionListEntity info, int position) {
        switch (holder.viewType) {
            case 1:
                try {
                    onBindMatchDataViewHolder(holder, info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    onBindTipOffViewHolder(holder, info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    onBindArticleViewHolder(holder, info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 比赛
     */
    @SuppressWarnings("ConstantConditions")
    private void onBindMatchDataViewHolder(ViewHolder holder, UserAttentionListEntity userAttentionListEntity) {
        final UserAttentionListEntity.Data info = userAttentionListEntity.getData();

        ImageUtil.loadImage(holder.ivUserPortrait, R.mipmap.icon_user_default, info.getPt());
        UserInfoUtil.setUserHeaderVMark(info.getIsv(), holder.ivUserV, holder.ivUserPortrait);
        holder.tvUserName.setText(info.getFname());

        Date tipDate = CommonUtils.getDateAndTimeFromGMT(info.getCtime());
        if (tipDate != null) {
            String dateInfo = CommonUtils.getDateAndTimeFormatString(tipDate);
            if (!TextUtils.isEmpty(dateInfo)) {
                String[] dates = dateInfo.split(" ");
                holder.tvCreateDate.setText(dates[0]);
                holder.tvCreateTime.setText(dates[dates.length - 1]);
            }
        } else {
            holder.tvCreateTime.setText("");
            holder.tvCreateDate.setText("");
        }
        CalendarUtil matchCal = CalendarUtil.parseStringTZ(info.getMtime());
        if (matchCal != null) {
            holder.tv_match_date.setText(matchCal.getStringFormat("MM-dd"));
            holder.tv_match_time.setText(matchCal.getStringFormat("HH:mm"));
        } else {
            holder.tv_match_date.setText("");
            holder.tv_match_time.setText("");
        }

        holder.tv_tour_name.setText(info.getTourname());
        holder.tv_home_team_name.setText(info.getHtname());
        holder.tv_away_team_name.setText(info.getAtname());

        holder.tv_match_betting_info.setText(MatchBettingInfoUtil.getBettingResultInfo(info.getChoice(), info.getOtype(), info.getOdata()));
        final int sam = info.getSam();
        holder.iv_money_icon.setVisibility(sam == 0 ? View.GONE : View.VISIBLE);
        holder.tv_match_betting_moneys.setVisibility(sam == 0 ? View.GONE : View.VISIBLE);
        holder.tv_match_betting_moneys.setText(String.valueOf(sam));

        // TODO: 2016-07-21 0021
    }

    /**
     * 爆料
     */
    @SuppressWarnings("ConstantConditions")
    private void onBindTipOffViewHolder(final ViewHolder holder, final UserAttentionListEntity userAttentionListEntity) {
        final UserAttentionListEntity.Data info = userAttentionListEntity.getData();

        ImageUtil.loadImage(holder.ivUserPortrait, R.mipmap.icon_user_default, info.getPt());
        UserInfoUtil.setUserHeaderVMark(info.getIsv(), holder.ivUserV, holder.ivUserPortrait);

        holder.tvUserName.setText(info.getFname());
        holder.tvLikeCounts.setText(String.valueOf(info.getTipcount()));
        holder.tvTipOffContent.setText(info.getCont().trim());
        Date tipDate = CommonUtils.getDateAndTimeFromGMT(info.getCtime());
        if (tipDate != null) {
            String dateInfo = CommonUtils.getDateAndTimeFormatString(tipDate);
            if (!TextUtils.isEmpty(dateInfo)) {
                String[] dates = dateInfo.split(" ");
                holder.tvCreateDate.setText(dates[0]);
                holder.tvCreateTime.setText(dates[dates.length - 1]);
            }
        } else {
            holder.tvCreateTime.setText("");
            holder.tvCreateDate.setText("");
        }

        ImageUtil.loadImage(holder.iv_home_team_icon, info.getHtlogo());
        ImageUtil.loadImage(holder.iv_away_team_icon, info.getAtlogo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, BallQTipOffDetailActivity.class);

                BallQTipOffEntity tipInfo = new BallQTipOffEntity();
                tipInfo.setId(info.getId());
                tipInfo.setEid(info.getEid());
                intent.putExtra(BallQTipOffDetailActivity.class.getSimpleName(), tipInfo);

                context.startActivity(intent);
            }
        });

        holder.ivUserPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = holder.itemView.getContext();
                UserInfoUtil.lookUserInfo(context, info.getUid());
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void onBindArticleViewHolder(final ViewHolder holder, final UserAttentionListEntity userAttentionListEntity) {
        final UserAttentionListEntity.Data info = userAttentionListEntity.getData();

        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getPt(), R.mipmap.icon_user_default, holder.ivUserPortrait);
        UserInfoUtil.setUserHeaderVMark(info.getIsv(), holder.ivUserV, holder.ivUserPortrait);
        holder.tvUserName.setText(info.getFname());
        holder.tvReadCounts.setText(String.valueOf(info.getReading_count()));
        holder.tvTitle.setText(info.getTitle());
        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getCover(), R.mipmap.icon_ball_wrap_default_img, holder.ivImg);
        Date tipDate = CommonUtils.getDateAndTimeFromGMT(info.getCtime());
        if (tipDate != null) {
            String dateInfo = CommonUtils.getDateAndTimeFormatString(tipDate);
            if (!TextUtils.isEmpty(dateInfo)) {
                String[] dates = dateInfo.split(" ");
                if (dates != null) {
                    holder.tvCreateDate.setText(dates[0]);
                    holder.tvCreateTime.setText(dates[dates.length - 1]);
                }
            }
        } else {
            holder.tvCreateTime.setText("");
            holder.tvCreateDate.setText("");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, BallQBallWarpDetailActivity.class);

                BallQBallWarpInfoEntity articleInfo = new BallQBallWarpInfoEntity();
                articleInfo.setId(info.getId());
                intent.putExtra(BallQBallWarpDetailActivity.class.getSimpleName(), articleInfo);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
//        mtype 1 比赛 2爆料 3球茎
        if (position < 0 || position >= getItemCount()) {
            return super.getItemViewType(position);
        }
        return getItem(position).getMtype();
    }

    class ViewHolder extends ButterKnifeRecyclerViewHolder {
        @Nullable
        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserPortrait;
        @Nullable
        @Bind(R.id.isV)
        ImageView ivUserV;
        @Nullable
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Nullable
        @Bind(R.id.iv_user_level)
        ImageView ivUserLevel;
        @Nullable
        @Bind(R.id.iv_video_mark)
        ImageView ivVideoMark;
        @Nullable
        @Bind(R.id.tv_create_date)
        TextView tvCreateDate;
        @Nullable
        @Bind(R.id.tv_create_time)
        TextView tvCreateTime;
        @Nullable
        @Bind(R.id.tv_like_counts)
        TextView tvLikeCounts;
        @Nullable
        @Bind(R.id.tv_tip_off_content)
        TextView tvTipOffContent;
        @Nullable
        @Bind(R.id.tv_read_counts)
        TextView tvReadCounts;
        @Nullable
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Nullable
        @Bind(R.id.iv_ball_ward_img)
        ImageView ivImg;
        @Nullable
        @Bind(R.id.iv_home_team_icon)
        ImageView iv_home_team_icon;
        @Nullable
        @Bind(R.id.iv_away_team_icon)
        ImageView iv_away_team_icon;
        @Nullable
        @Bind(R.id.tv_home_team_name)
        TextView tv_home_team_name;
        @Nullable
        @Bind(R.id.tv_away_team_name)
        TextView tv_away_team_name;
        @Nullable
        @Bind(R.id.tv_match_date)
        TextView tv_match_date;
        @Nullable
        @Bind(R.id.tv_match_time)
        TextView tv_match_time;
        @Nullable
        @Bind(R.id.tv_tour_name)
        TextView tv_tour_name;
        @Nullable
        @Bind(R.id.tv_match_betting_info)
        TextView tv_match_betting_info;
        @Nullable
        @Bind(R.id.iv_money_icon)
        ImageView iv_money_icon;
        @Nullable
        @Bind(R.id.tv_match_betting_moneys)
        TextView tv_match_betting_moneys;

        public ViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
        }
    }
}
