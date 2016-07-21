package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQTipOffAdapter extends RecyclerView.Adapter<BallQTipOffAdapter.BallQTipOffViewHolder> {
    private List<BallQTipOffEntity> tipOffEntityList = null;

    public BallQTipOffAdapter(List<BallQTipOffEntity> tipOffEntityList) {
        this.tipOffEntityList = tipOffEntityList;
    }

    @Override
    public BallQTipOffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_tip_off_item, parent, false);
        return new BallQTipOffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQTipOffViewHolder holder, int position) {
        final BallQTipOffEntity info = tipOffEntityList.get(position);
        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getPt(), R.mipmap.icon_user_default, holder.ivUserHeader);
        UserInfoUtil.setUserHeaderVMark(info.getIsv(), holder.ivUserV, holder.ivUserHeader);
        holder.tvUserName.setText(info.getFname());
        holder.tvLikeCounts.setText(String.valueOf(info.getTipcount()));
        holder.tvTipOffContent.setText(info.getCont().trim());
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

        if (info.getRichtext_type() == 2) {
            holder.ivVideoMark.setVisibility(View.VISIBLE);
        } else {
            holder.ivVideoMark.setVisibility(View.GONE);
        }
        holder.tv_user_tip_count.setText(String.valueOf(info.getTipcount()));
        holder.tv_user_tip_win_rate.setText(String.format(Locale.getDefault(), "%.2f", info.getWins() * 100));
        holder.tv_user_tip_win_rate.append("%");
        holder.tv_user_tip_trend.setText(String.format(Locale.getDefault(), "%.2f", info.getRor()));
        holder.tv_user_tip_trend.append("%");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, BallQTipOffDetailActivity.class);
                intent.putExtra(BallQTipOffDetailActivity.class.getSimpleName(), info);
                context.startActivity(intent);
            }
        });

        holder.ivUserHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                UserInfoUtil.lookUserInfo(context, info.getUid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tipOffEntityList.size();
    }


    public static final class BallQTipOffViewHolder extends ButterKnifeRecyclerViewHolder {
        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserHeader;
        @Bind(R.id.isV)
        ImageView ivUserV;
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.iv_user_level)
        ImageView ivUserLevel;
        @Bind(R.id.iv_video_mark)
        ImageView ivVideoMark;
        @Bind(R.id.tv_create_date)
        TextView tvCreateDate;
        @Bind(R.id.tv_create_time)
        TextView tvCreateTime;
        @Bind(R.id.tv_like_counts)
        TextView tvLikeCounts;
        @Bind(R.id.tv_tip_off_content)
        TextView tvTipOffContent;

        @Bind(R.id.tv_user_tip_count)
        TextView tv_user_tip_count;
        @Bind(R.id.tv_user_tip_win_rate)
        TextView tv_user_tip_win_rate;
        @Bind(R.id.tv_user_tip_trend)
        TextView tv_user_tip_trend;

        public BallQTipOffViewHolder(View itemView) {
            super(itemView);
        }
    }
}
