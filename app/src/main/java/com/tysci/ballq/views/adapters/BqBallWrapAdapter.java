package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQBallWarpDetailActivity;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.base.WrapRecyclerAdapter;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.utils.CalendarUtil;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import butterknife.Bind;

/**
 * Created by LinDe on 2016-08-02 0002.
 *
 * @author used in 2016-08-01
 */
public class BqBallWrapAdapter extends WrapRecyclerAdapter<BallQBallWarpInfoEntity, BqBallWrapAdapter.ViewHolder>
{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_ball_warp_item_2, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder2(final ViewHolder holder, final BallQBallWarpInfoEntity info, int position)
    {
        String tmp;

        holder.tvUserName.setText(info.getFname());
        holder.tvLikeCounts.setText(String.valueOf(info.getLike_count()));
        holder.tvCommentCounts.setText(String.valueOf(info.getComcount()));
        holder.tvReadCounts.setText(String.valueOf(info.getReading_count()));
        holder.tvRewardCounts.setText(String.valueOf(info.getBoncount()));
        holder.tvTitle.setText(info.getTitle());

        CalendarUtil createCalendar = CalendarUtil.parseStringTZ(info.getCtime());
        if (createCalendar == null)
        {
            holder.tvCreateDate.setVisibility(View.GONE);
        }
        else
        {
            holder.tvCreateDate.setVisibility(View.VISIBLE);
            holder.tvCreateDate.setText(createCalendar.getStringFormat("MM-dd HH:mm"));
        }

        ImageUtil.loadImage(holder.ivInfoConver, R.mipmap.icon_ball_wrap_default_img, info.getCover());

        ImageUtil.loadImage(holder.ivUserHeader, R.mipmap.icon_user_default, info.getPt());
        UserInfoUtil.setUserHeaderVMark(info.getIsv(), holder.isV, holder.ivUserHeader);

        tmp = info.getTitle1();
        if (TextUtils.isEmpty(tmp))
        {
            holder.ivUserAchievement01.setVisibility(View.GONE);
        }
        else
        {
            holder.ivUserAchievement01.setVisibility(View.VISIBLE);
            ImageUtil.loadImage(holder.ivUserAchievement01, R.mipmap.icon_user_achievement_circle_mark, tmp);
        }
        tmp = info.getTitle2();
        if (TextUtils.isEmpty(tmp))
        {
            holder.ivUserAchievement02.setVisibility(View.GONE);
        }
        else
        {
            holder.ivUserAchievement02.setVisibility(View.VISIBLE);
            ImageUtil.loadImage(holder.ivUserAchievement02, R.mipmap.icon_user_achievement_circle_mark, tmp);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Context context = v.getContext();
                Intent intent = new Intent(context, BallQBallWarpDetailActivity.class);
                intent.putExtra(BallQBallWarpDetailActivity.class.getSimpleName(), info);
                context.startActivity(intent);
            }
        });
        holder.ivUserHeader.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Context context = v.getContext();
                UserInfoUtil.lookUserInfo(context, info.getUid());
            }
        });
//        holder.user_v_status.setUserV_Icon(info.getIsv());
    }

    static class ViewHolder extends ButterKnifeRecyclerViewHolder
    {
        @Bind(R.id.iv_ballq_info_cover)
        ImageView ivInfoConver;
        @Bind(R.id.tv_ballq_info_author_name)
        TextView tvUserName;
        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserHeader;
        @Bind(R.id.isV)
        ImageView isV;
        @Bind(R.id.iv_ballq_info_author_achievement01)
        ImageView ivUserAchievement01;
        @Bind(R.id.iv_ballq_info_author_achievement02)
        ImageView ivUserAchievement02;
        @Bind(R.id.tv_ballq_info_create_date)
        TextView tvCreateDate;
        //        TextView tvCreateTime;
        @Bind(R.id.tv_ballq_info_read_counts)
        TextView tvReadCounts;
        @Bind(R.id.tv_ballq_info_title)
        TextView tvTitle;
        @Bind(R.id.tv_ballq_info_like_count)
        TextView tvLikeCounts;
        @Bind(R.id.tv_ballq_info_comments_count)
        TextView tvCommentCounts;
        @Bind(R.id.tv_ballq_info_reward_counts)
        TextView tvRewardCounts;

//        @Bind(R.id.user_v_status)
//        UserVStatusImageView user_v_status;

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
