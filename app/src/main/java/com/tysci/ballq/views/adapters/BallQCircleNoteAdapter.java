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
import com.tysci.ballq.activitys.BallQCircleNoteDetailActivity;
import com.tysci.ballq.modles.BallQCircleNoteEntity;
import com.tysci.ballq.modles.BallQNoteContentEntity;
import com.tysci.ballq.modles.BallQUserAchievementEntity;
import com.tysci.ballq.modles.BallQUserEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
public class BallQCircleNoteAdapter extends RecyclerView.Adapter<BallQCircleNoteAdapter.BallQHomeCircleNoteViewHolder>{
    private List<BallQCircleNoteEntity> ballQNoteEntities;

    public BallQCircleNoteAdapter(List<BallQCircleNoteEntity> list) {
        this.ballQNoteEntities = list;
    }

    @Override
    public BallQHomeCircleNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_home_circle_note_item, parent, false);
        return new BallQHomeCircleNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQHomeCircleNoteViewHolder holder, int position) {
        final BallQCircleNoteEntity info = ballQNoteEntities.get(position);
        BallQUserEntity userInfo = info.getCreater();

        setCircleNoteContent(info, holder);
        setCircleAuthorInfo(userInfo, holder);

        holder.tvReadCounts.setText(String.valueOf(info.getViewCount()));
        holder.tvLikeCounts.setText(String.valueOf(info.getClickCount()));
        holder.tvCommentsCounts.setText(String.valueOf(info.getCommentCount()));

        String date = CommonUtils.getDateAndTimeFormatString(info.getCreateTime());
        String dates[] = date.split(" ");
        if (dates != null && dates.length >= 2) {
            holder.tvCreateDate.setText(dates[0]);
            holder.tvCreateTime.setText(dates[1]);
        }

        if (position == 0) {
            holder.divider.setVisibility(View.VISIBLE);
        } else {
            holder.divider.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=holder.itemView.getContext();
                Intent intent=new Intent(context, BallQCircleNoteDetailActivity.class);
                intent.putExtra(BallQCircleNoteDetailActivity.class.getSimpleName(),info.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ballQNoteEntities.size();
    }

    /**
     * 设置圈子内帖子的内容
     *
     * @param info
     * @param holder
     */
    public void setCircleNoteContent(BallQCircleNoteEntity info, BallQHomeCircleNoteViewHolder holder) {
        List<BallQNoteContentEntity> ballQNoteContentEntityList = info.getContents();
        boolean isInitTitle = false;
        if (!TextUtils.isEmpty(info.getTitle())) {
            isInitTitle = true;
            holder.tvCircleTitle.setText(info.getTitle());
        }
        List<BallQNoteContentEntity> imageContentList = null;

        if (ballQNoteContentEntityList != null && ballQNoteContentEntityList.size() > 0) {
            BallQNoteContentEntity contentEntity = null;
            int size = ballQNoteContentEntityList.size();
            for (int i = 0; i < size; i++) {
                contentEntity = ballQNoteContentEntityList.get(i);
                if (contentEntity.getType() == 0) {
                    if (imageContentList == null) {
                        imageContentList = new ArrayList<>(9);
                    }
                    imageContentList.add(contentEntity);
                } else if (contentEntity.getType() == 4) {
                    if (!isInitTitle) {
                        isInitTitle = true;
                        holder.tvCircleTitle.setText(contentEntity.getContent());
                    }
                }
            }
        }
        if (isInitTitle) {
            holder.tvCircleTitle.setVisibility(View.VISIBLE);
        } else {
            holder.tvCircleTitle.setVisibility(View.GONE);
        }

        if (imageContentList == null || imageContentList.size() == 0) {
            holder.layoutCirclePictures.setVisibility(View.GONE);
        } else {
            holder.layoutCirclePictures.setVisibility(View.VISIBLE);
            //setContentClickListener(imageContentList, holder);
            int size = imageContentList.size();
            if (size == 1) {
                holder.ivCirclePicture01.setVisibility(View.VISIBLE);
                holder.ivCirclePicture02.setVisibility(View.GONE);
                holder.layoutCirclePicture03.setVisibility(View.GONE);
                GlideImageLoader.loadImage(holder.itemView.getContext(),imageContentList.get(0).getContent(), R.mipmap.icon_default_note_img,holder.ivCirclePicture01);
            } else if (size == 2) {
                holder.ivCirclePicture01.setVisibility(View.VISIBLE);
                holder.ivCirclePicture02.setVisibility(View.VISIBLE);
                holder.layoutCirclePicture03.setVisibility(View.GONE);
                GlideImageLoader.loadImage(holder.itemView.getContext(), imageContentList.get(0).getContent(), R.mipmap.icon_default_note_img, holder.ivCirclePicture01);
                GlideImageLoader.loadImage(holder.itemView.getContext(), imageContentList.get(1).getContent(), R.mipmap.icon_default_note_img, holder.ivCirclePicture02);
            } else if (size >= 3) {
                holder.ivCirclePicture01.setVisibility(View.VISIBLE);
                holder.ivCirclePicture02.setVisibility(View.VISIBLE);
                holder.layoutCirclePicture03.setVisibility(View.VISIBLE);
                GlideImageLoader.loadImage(holder.itemView.getContext(), imageContentList.get(0).getContent(), R.mipmap.icon_default_note_img, holder.ivCirclePicture01);
                GlideImageLoader.loadImage(holder.itemView.getContext(), imageContentList.get(1).getContent(), R.mipmap.icon_default_note_img, holder.ivCirclePicture02);
                GlideImageLoader.loadImage(holder.itemView.getContext(), imageContentList.get(2).getContent(), R.mipmap.icon_default_note_img, holder.ivCirclePicture03);
                if (size > 3) {
                    holder.tvCirclePictureCounts.setVisibility(View.VISIBLE);
                    holder.tvCirclePictureCounts.setText(String.valueOf(size) + "图");
                } else {
                    holder.tvCirclePictureCounts.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setCircleAuthorInfo(final BallQUserEntity info, final BallQHomeCircleNoteViewHolder holder) {
        if (info != null) {
            holder.tvUserName.setText(info.getFirstName());
            GlideImageLoader.loadImage(holder.itemView.getContext(), info.getPortrait(), R.mipmap.icon_user_default, holder.ivUserHeader);
            UserInfoUtil.setUserHeaderVMark(info.getIsV(),holder.isV,holder.ivUserHeader);
            List<BallQUserAchievementEntity> achievementEntities = info.getAchievements();
            if (achievementEntities == null || achievementEntities.size() == 0) {
                holder.ivUserAchievement01.setVisibility(View.GONE);
                holder.ivUserAchievement02.setVisibility(View.GONE);
            } else if (achievementEntities.size() == 1) {
                holder.ivUserAchievement01.setVisibility(View.VISIBLE);
                holder.ivUserAchievement02.setVisibility(View.GONE);
                GlideImageLoader.loadImage(holder.itemView.getContext(),achievementEntities.get(0).getLogo(), R.mipmap.icon_user_achievement_circle_mark,holder.ivUserAchievement01);
            } else if (achievementEntities.size() == 2) {
                holder.ivUserAchievement01.setVisibility(View.VISIBLE);
                holder.ivUserAchievement02.setVisibility(View.VISIBLE);
                GlideImageLoader.loadImage(holder.itemView.getContext(), achievementEntities.get(0).getLogo(), R.mipmap.icon_user_achievement_circle_mark, holder.ivUserAchievement01);
                GlideImageLoader.loadImage(holder.itemView.getContext(), achievementEntities.get(1).getLogo(), R.mipmap.icon_user_achievement_circle_mark, holder.ivUserAchievement02);
            }
        }
    }


    public static final class BallQHomeCircleNoteViewHolder extends RecyclerView.ViewHolder{
        View divider;
        CircleImageView ivUserHeader;
        ImageView isV;
        TextView tvUserName;
        TextView tvCreateDate;
        TextView tvCreateTime;
        ImageView ivUserAchievement01;
        ImageView ivUserAchievement02;
        TextView tvCircleTitle;
        View layoutCirclePictures;
        ImageView ivCirclePicture01;
        ImageView ivCirclePicture02;
        ImageView ivCirclePicture03;
        View layoutCirclePicture03;
        TextView tvCirclePictureCounts;
        TextView tvReadCounts;
        TextView tvLikeCounts;
        TextView tvCommentsCounts;

        public BallQHomeCircleNoteViewHolder(View itemView) {
            super(itemView);
            ivUserHeader = (CircleImageView) itemView.findViewById(R.id.ivUserIcon);
            isV = (ImageView) itemView.findViewById(R.id.isV);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvCreateDate = (TextView) itemView.findViewById(R.id.tv_create_date);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            ivUserAchievement01 = (ImageView) itemView.findViewById(R.id.iv_user_achievement01);
            ivUserAchievement02 = (ImageView) itemView.findViewById(R.id.iv_user_achievement02);
            tvCircleTitle = (TextView) itemView.findViewById(R.id.tv_ballq_circle_title);
            layoutCirclePictures = itemView.findViewById(R.id.layout_ballq_circle_pictures);
            ivCirclePicture01 = (ImageView) itemView.findViewById(R.id.iv_ballq_circle_picture01);
            ivCirclePicture02 = (ImageView) itemView.findViewById(R.id.iv_ballq_circle_picture02);
            ivCirclePicture03 = (ImageView) itemView.findViewById(R.id.iv_ballq_circle_picture03);
            layoutCirclePicture03 = itemView.findViewById(R.id.layout_ballq_circle_picture03);
            tvCirclePictureCounts = (TextView) itemView.findViewById(R.id.tv_picture_counts);
            tvReadCounts = (TextView) itemView.findViewById(R.id.tv_user_read_counts);
            tvLikeCounts = (TextView) itemView.findViewById(R.id.tv_ballq_circle_like_count);
            tvCommentsCounts = (TextView) itemView.findViewById(R.id.tv_ballq_circle_comment_count);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
