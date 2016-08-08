package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQUserCommentEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.interfaces.OnLongClickUserHeaderListener;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/6.
 */
public class BallQUserCommentAdapter extends RecyclerView.Adapter<BallQUserCommentAdapter.BallQUserCommentViewHolder>
{
    private List<BallQUserCommentEntity> userCommentEntityList;
    private OnLongClickUserHeaderListener onLongClickUserHeaderListener;

    public BallQUserCommentAdapter(List<BallQUserCommentEntity> userCommentEntityList)
    {
        this.userCommentEntityList = userCommentEntityList;
    }

    public void setOnLongClickUserHeaderListener(OnLongClickUserHeaderListener onLongClickUserHeaderListener)
    {
        this.onLongClickUserHeaderListener = onLongClickUserHeaderListener;
    }

    @Override
    public BallQUserCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_comment_item, parent, false);
        return new BallQUserCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQUserCommentViewHolder holder, final int position)
    {
        final BallQUserCommentEntity info = userCommentEntityList.get(position);
        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getPt(), R.mipmap.icon_user_default, holder.ivUserIcon);
        UserInfoUtil.setUserHeaderVMark(info.getIsV(), holder.isV, holder.ivUserIcon);
        UserInfoUtil.setUserAchievementInfo(holder.itemView.getContext(), info.getTitle1(), holder.ivAchievement01, info.getTitle2(), holder.ivAchievement02);
        holder.tvUserName.setText(info.getFname().trim());
        holder.tvCommentContent.setText(info.getCont().trim());
        Date date = CommonUtils.getDateAndTimeFromGMT(info.getCtime());
        if (date != null)
        {
            holder.tvCreateDate.setText(CommonUtils.getTimeFormatName(date.getTime()));
        }
        else
        {
            holder.tvCreateDate.setText("");
        }
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

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserInfoUtil.lookUserInfo(holder.itemView.getContext(), info.getUid());
            }
        });
        holder.ivUserIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserInfoUtil.lookUserInfo(holder.itemView.getContext(), info.getUid());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return userCommentEntityList.size();
    }

    @Override
    public void onViewDetachedFromWindow(BallQUserCommentViewHolder holder)
    {
        super.onViewDetachedFromWindow(holder);
        ButterKnife.unbind(holder);
    }

    public static final class BallQUserCommentViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserIcon;
        @Bind(R.id.isV)
        ImageView isV;
        @Bind(R.id.iv_user_achievement01)
        ImageView ivAchievement01;
        @Bind(R.id.iv_user_achievement02)
        ImageView ivAchievement02;
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.tv_create_time)
        TextView tvCreateDate;
        @Bind(R.id.tv_comment_content)
        TextView tvCommentContent;

        public BallQUserCommentViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
