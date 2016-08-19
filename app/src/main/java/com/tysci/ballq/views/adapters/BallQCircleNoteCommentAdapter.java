package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQCircleUserCommentEntity;
import com.tysci.ballq.modles.BallQNoteContentEntity;
import com.tysci.ballq.modles.BallQUserAchievementEntity;
import com.tysci.ballq.modles.BallQUserEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.interfaces.OnLongClickUserHeaderListener;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HTT on 2016/6/25.
 */
public class BallQCircleNoteCommentAdapter extends RecyclerView.Adapter<BallQCircleNoteCommentAdapter.BallQCircleNoteCommentViewHolder>
{
    private List<BallQCircleUserCommentEntity> commentEntityList;
    private int contentColor = Color.parseColor("#3a3a3a");
    private LinearLayout.LayoutParams layoutContentParams = null;
    private OnLongClickUserHeaderListener onLongClickUserHeaderListener;

    public BallQCircleNoteCommentAdapter(List<BallQCircleUserCommentEntity> commentEntityList)
    {
        this.commentEntityList = commentEntityList;
        layoutContentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setOnLongClickUserHeaderListener(OnLongClickUserHeaderListener onLongClickUserHeaderListener)
    {
        this.onLongClickUserHeaderListener = onLongClickUserHeaderListener;
    }

    @Override
    public BallQCircleNoteCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_circle_comment_item, parent, false);
        return new BallQCircleNoteCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQCircleNoteCommentViewHolder holder, final int position)
    {
        BallQCircleUserCommentEntity info = commentEntityList.get(position);
        final BallQUserEntity author = info.getCreator();
        holder.tvFloorNum.setText(info.getIndexCount() + "楼");
        if (author != null)
        {
            GlideImageLoader.loadImage(holder.itemView.getContext(), author.getPortrait(), R.mipmap.icon_user_default, holder.ivUserIcon);
            UserInfoUtil.setUserHeaderVMark(author.getIsV(), holder.isV, holder.ivUserIcon);
            holder.ivUserIcon.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    if (onLongClickUserHeaderListener != null)
                    {
                        onLongClickUserHeaderListener.onLongClickUserHead(v, position);
                    }
                    return true;
                }
            });
            holder.ivUserIcon.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    UserInfoUtil.lookUserInfo(v.getContext(), author.getUserId());
                }
            });
            holder.tvUserName.setText(author.getFirstName());
            if (author.getIsAuthor() == 1)
            {
                holder.tvLZ.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.tvLZ.setVisibility(View.GONE);
            }
            List<BallQUserAchievementEntity> achievementEntityList = author.getAchievements();
            if (achievementEntityList != null)
            {
                int size = achievementEntityList.size();
                if (size == 0)
                {
                    holder.ivUserAchievement01.setVisibility(View.GONE);
                    holder.ivUserAchievement02.setVisibility(View.GONE);
                }
                else if (size == 1)
                {
                    holder.ivUserAchievement01.setVisibility(View.VISIBLE);
                    holder.ivUserAchievement02.setVisibility(View.GONE);
                    GlideImageLoader.loadImage(holder.itemView.getContext(), achievementEntityList.get(0).getLogo(), R.mipmap.icon_user_achievement_circle_mark, holder.ivUserAchievement01);
                }
                else if (size == 2)
                {
                    holder.ivUserAchievement01.setVisibility(View.VISIBLE);
                    holder.ivUserAchievement02.setVisibility(View.VISIBLE);
                    GlideImageLoader.loadImage(holder.itemView.getContext(), achievementEntityList.get(0).getLogo(), R.mipmap.icon_user_achievement_circle_mark, holder.ivUserAchievement01);
                    GlideImageLoader.loadImage(holder.itemView.getContext(), achievementEntityList.get(1).getLogo(), R.mipmap.icon_user_achievement_circle_mark, holder.ivUserAchievement02);
                }
            }
            else
            {
                holder.ivUserAchievement01.setVisibility(View.GONE);
                holder.ivUserAchievement02.setVisibility(View.GONE);
            }
        }
        holder.tvTime.setText(CommonUtils.getTimeFormatName(info.getCreateTime()));
        initContentType(holder.itemView.getContext(), holder.layoutContent, info.getContent());
    }

    private void initContentType(Context context, LinearLayout layoutContent, List<BallQNoteContentEntity> contents)
    {
        layoutContent.removeAllViews();
        BallQNoteContentEntity content;
        int size = contents.size();
        for (int i = 0; i < size; i++)
        {
            content = contents.get(i);
            switch (content.getType())
            {
                case 0:
                case 1:
                    // TODO: 16/3/22
                    break;
                case 2:
                    // TODO: 16/3/22
                    break;
                case 3:
                    // TODO: 16/3/22
                    break;
                case 4:
                    initContentText(context, layoutContent, content);
                    break;
            }
        }
    }

    private void initContentText(Context context, LinearLayout layoutContent, BallQNoteContentEntity content)
    {
        TextView tv = new TextView(context);
        tv.setTextColor(contentColor);
        tv.setText(content.getContent().trim());
        tv.setLayoutParams(layoutContentParams);
        layoutContent.addView(tv);
    }

    @Override
    public int getItemCount()
    {
        return commentEntityList.size();
    }

    @Override
    public void onViewDetachedFromWindow(BallQCircleNoteCommentViewHolder holder)
    {
        ButterKnife.unbind(this);
        super.onViewDetachedFromWindow(holder);
    }

    public static final class BallQCircleNoteCommentViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserIcon;
        @Bind(R.id.isV)
        ImageView isV;
        @Bind(R.id.tv_author_name)
        TextView tvUserName;
        @Bind(R.id.tv_LZ)
        TextView tvLZ;
        @Bind(R.id.iv_user_achievement01)
        ImageView ivUserAchievement01;
        @Bind(R.id.iv_user_achievement02)
        ImageView ivUserAchievement02;
        @Bind(R.id.tv_floor_num)
        TextView tvFloorNum;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.layout_content)
        LinearLayout layoutContent;

        public BallQCircleNoteCommentViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
