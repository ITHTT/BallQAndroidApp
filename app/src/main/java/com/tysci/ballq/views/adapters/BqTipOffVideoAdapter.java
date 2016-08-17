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
import com.tysci.ballq.activitys.BallQTipOffDetailActivity;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.base.WrapRecyclerAdapter;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.utils.CalendarUtil;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.UserVStatusImageView;
import com.tysci.ballq.views.widgets.CircleImageView;

import butterknife.Bind;

/**
 * Created by LinDe on 2016-08-02 0002.
 *
 * @author used in 2016-08-02
 */
public class BqTipOffVideoAdapter extends WrapRecyclerAdapter<BallQTipOffEntity, BqTipOffVideoAdapter.ViewHolder>
{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_video_item_2, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, final BallQTipOffEntity info, int position)
    {
        String tmp;

        ImageUtil.loadImage(holder.ivUserHeader, R.mipmap.icon_user_default, info.getPt());
        UserInfoUtil.setUserHeaderVMark(info.getIsv(), holder.isV, holder.ivUserHeader);
        holder.tvUserName.setText(info.getFname());
        holder.tvTitle.setText(info.getCont());
        holder.tvCommentCounts.setText(String.valueOf(info.getComcount()));
        holder.tvReadingCount.setText(String.valueOf(info.getReading_count()));
        holder.tvLikeCOunt.setText(String.valueOf(info.getLike_count()));
        holder.user_v_status.setUserV_Icon(info.getIsv());

        tmp = info.getFirst_image();
        if (!TextUtils.isEmpty(tmp))
        {
            ImageUtil.loadImage(holder.ivInfoConver, R.mipmap.icon_ball_wrap_default_img, tmp);
        }
        else
        {
            holder.ivInfoConver.setImageResource(R.mipmap.icon_ball_wrap_default_img);
        }
        CalendarUtil createCalendar = CalendarUtil.parseStringTZ(info.getCtime());
        if (createCalendar == null)
        {
            holder.tvCreateDate.setText("");
        }
        else
        {
            holder.tvCreateDate.setText(createCalendar.getStringFormat("MM-dd HH:mm"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Context context = v.getContext();
                Intent intent = new Intent(context, BallQTipOffDetailActivity.class);
                intent.putExtra(BallQTipOffDetailActivity.class.getSimpleName(), info);
                context.startActivity(intent);
            }
        });

        holder.ivUserHeader.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Context context = v.getContext();
                UserInfoUtil.lookUserInfo(context, info.getUid());
            }
        });
    }

    class ViewHolder extends ButterKnifeRecyclerViewHolder
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
//        @Bind(R.id.tv_ballq_info_read_counts)
//        TextView tvReadCounts;
        @Bind(R.id.tv_ballq_info_title)
        TextView tvTitle;
        @Bind(R.id.tvReadingCount)
        TextView tvReadingCount;
        @Bind(R.id.tv_ballq_info_comments_count)
        TextView tvCommentCounts;
        @Bind(R.id.tvLikeCount)
        TextView tvLikeCOunt;

        @Bind(R.id.user_v_status)
        UserVStatusImageView user_v_status;

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
