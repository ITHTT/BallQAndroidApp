package com.tysci.ballq.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQCircleNoteDetailActivity;
import com.tysci.ballq.activitys.BallQImageBrowseActivity;
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
 * Created by HTT on 2016/7/13.
 */
public class BallQFindCircleNoteAdapter extends RecyclerView.Adapter<BallQFindCircleNoteAdapter.BallQFindCircleNoteViewHolder>{
    private List<BallQCircleNoteEntity> ballQCircleNoteEntities;

    public BallQFindCircleNoteAdapter(List<BallQCircleNoteEntity> ballQCircleNoteEntities) {
        this.ballQCircleNoteEntities = ballQCircleNoteEntities;
    }

    @Override
    public BallQFindCircleNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_circle_note_item,parent,false);
        return new BallQFindCircleNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQFindCircleNoteViewHolder holder, int position) {
        final BallQCircleNoteEntity info = ballQCircleNoteEntities.get(position);
        BallQUserEntity author = info.getCreater();
        holder.tvReadCounts.setText(String.valueOf(info.getViewCount()));
        holder.tvComments.setText(String.valueOf(info.getCommentCount()));
        holder.tvLikeCounts.setText(String.valueOf(info.getClickCount()));
        holder.tvCircleName.setText(info.getSectionName());
        GlideImageLoader.loadImage(holder.itemView.getContext(),info.getSectionPortrait(),R.mipmap.ic_ballq_logo,holder.ivCircleIcon);
        holder.tvCreatedTime.setText(CommonUtils.getDateAndTimeFormatString(info.getCreateTime()));
        if (info.getTop() == 1) {
            holder.tvTop.setVisibility(View.VISIBLE);
            holder.tvCreatedTime.setVisibility(View.INVISIBLE);
        } else {
            holder.tvTop.setVisibility(View.GONE);
            holder.tvCreatedTime.setVisibility(View.VISIBLE);
        }
        setAuthorInfo(author, holder);
        setCircleNoteContent(info, holder);

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

    private void setAuthorInfo(final BallQUserEntity author, final BallQFindCircleNoteViewHolder holder) {
        if (author != null) {
            holder.tvUserName.setText(author.getFirstName());
            final Context context=holder.itemView.getContext();
            GlideImageLoader.loadImage(context,author.getPortrait(),R.mipmap.icon_user_default,holder.ivUserIcon);
            UserInfoUtil.setUserHeaderVMark(author.getIsV(),holder.isV,holder.ivUserIcon);
            List<BallQUserAchievementEntity> achievementEntities = author.getAchievements();
            if (achievementEntities == null || achievementEntities.size() == 0) {
                holder.ivUserAchievement01.setVisibility(View.GONE);
                holder.ivUserAchievement02.setVisibility(View.GONE);
            } else if (achievementEntities.size() == 1) {
                holder.ivUserAchievement02.setVisibility(View.GONE);
                holder.ivUserAchievement01.setVisibility(View.VISIBLE);
                GlideImageLoader.loadImage(context,achievementEntities.get(0).getLogo(),R.mipmap.icon_user_achievement_circle_mark,holder.ivUserAchievement01);
            } else if (achievementEntities.size() == 2) {
                holder.ivUserAchievement02.setVisibility(View.VISIBLE);
                holder.ivUserAchievement01.setVisibility(View.VISIBLE);
                GlideImageLoader.loadImage(context,achievementEntities.get(0).getLogo(),R.mipmap.icon_user_achievement_circle_mark,holder.ivUserAchievement01);
                GlideImageLoader.loadImage(context,achievementEntities.get(1).getLogo(),R.mipmap.icon_user_achievement_circle_mark,holder.ivUserAchievement02);
            }
            holder.ivUserIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   UserInfoUtil.lookUserInfo(context,author.getUserId());
                }
            });
        }
    }

    /**
     * 设置圈子内帖子的内容
     *
     * @param info
     * @param holder
     */
    public void setCircleNoteContent(BallQCircleNoteEntity info, BallQFindCircleNoteViewHolder holder) {
        final Context context=holder.itemView.getContext();
        List<BallQNoteContentEntity> ballQNoteContentEntityList = info.getContents();
        boolean isInitTitle = false;
        if (!TextUtils.isEmpty(info.getTitle())) {
            isInitTitle = true;
            holder.tvTitle.setText(info.getTitle());
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
                        holder.tvTitle.setText(contentEntity.getContent());
                    }
                }
            }
        }
        if (isInitTitle) {
            holder.tvTitle.setVisibility(View.VISIBLE);
        } else {
            holder.tvTitle.setVisibility(View.GONE);
        }
        if (imageContentList == null || imageContentList.size() == 0) {
            holder.layoutImgs.setVisibility(View.GONE);
        } else {
            holder.layoutImgs.setVisibility(View.VISIBLE);
            setContentClickListener(imageContentList, holder);
            int size = imageContentList.size();
            if (size == 1) {
                holder.circleImg1.setVisibility(View.VISIBLE);
                holder.circleImg2.setVisibility(View.GONE);
                holder.layoutImg3.setVisibility(View.GONE);
                GlideImageLoader.loadImage(context,imageContentList.get(0).getContent(),R.mipmap.icon_default_note_img,holder.circleImg1);
            } else if (size == 2) {
                holder.circleImg1.setVisibility(View.VISIBLE);
                holder.circleImg2.setVisibility(View.VISIBLE);
                holder.layoutImg3.setVisibility(View.GONE);
                GlideImageLoader.loadImage(context,imageContentList.get(0).getContent(),R.mipmap.icon_default_note_img,holder.circleImg1);
                GlideImageLoader.loadImage(context,imageContentList.get(1).getContent(),R.mipmap.icon_default_note_img,holder.circleImg2);
            } else if (size >= 3) {
                holder.circleImg1.setVisibility(View.VISIBLE);
                holder.circleImg2.setVisibility(View.VISIBLE);
                holder.layoutImg3.setVisibility(View.VISIBLE);
                GlideImageLoader.loadImage(context,imageContentList.get(0).getContent(),R.mipmap.icon_default_note_img,holder.circleImg1);
                GlideImageLoader.loadImage(context,imageContentList.get(1).getContent(),R.mipmap.icon_default_note_img,holder.circleImg2);
                GlideImageLoader.loadImage(context,imageContentList.get(2).getContent(),R.mipmap.icon_default_note_img,holder.circleImg3);
                if (size > 3) {
                    holder.tvImgCounts.setVisibility(View.VISIBLE);
                    holder.tvImgCounts.setText(String.valueOf(size) + "图");
                } else {
                    holder.tvImgCounts.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setContentClickListener(final List<BallQNoteContentEntity> imageContentList, final BallQFindCircleNoteViewHolder holder) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity a = (Activity) holder.itemView.getContext();
                int id = v.getId();
                int index = 0;
                if (id == R.id.iv_circle_picture_01) {
                    index = 0;
                } else if (id == R.id.iv_circle_picture_02) {
                    index = 1;
                } else if (id == R.id.layout_circle_picture_03) {
                    index = 2;
                }
                Intent intent = new Intent(a, BallQImageBrowseActivity.class);
                intent.putExtra("index", index);
                intent.putParcelableArrayListExtra("images", (ArrayList<? extends Parcelable>) imageContentList);
                a.startActivity(intent);
            }
        };
        holder.circleImg1.setOnClickListener(onClickListener);
        holder.circleImg2.setOnClickListener(onClickListener);
        holder.layoutImg3.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return ballQCircleNoteEntities.size();
    }

    public static final class BallQFindCircleNoteViewHolder extends RecyclerView.ViewHolder{
        CircleImageView ivUserIcon;
        ImageView isV;
        TextView tvUserName;
        ImageView ivUserAchievement01;
        ImageView ivUserAchievement02;
        TextView tvCreatedTime;
        TextView tvTitle;
        ImageView ivCircleIcon;
        TextView tvCircleName;
        View layoutImgs;
        ImageView circleImg1;
        ImageView circleImg2;
        ImageView circleImg3;
        View layoutImg3;
        TextView tvImgCounts;
        TextView tvComments;
        TextView tvReadCounts;
        TextView tvLikeCounts;
        TextView tvTop;
        public BallQFindCircleNoteViewHolder(View itemView) {
            super(itemView);
            ivUserIcon = (CircleImageView) itemView.findViewById(R.id.ivUserIcon);
            isV = (ImageView) itemView.findViewById(R.id.isV);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            ivUserAchievement01 = (ImageView) itemView.findViewById(R.id.iv_user_achievement01);
            ivUserAchievement02 = (ImageView) itemView.findViewById(R.id.iv_user_achievement02);
            tvCreatedTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivCircleIcon = (ImageView) itemView.findViewById(R.id.iv_ballq_circle_cion);
            tvCircleName = (TextView) itemView.findViewById(R.id.tv_ballq_circle_name);
            layoutImgs = itemView.findViewById(R.id.layout_circle_pictures);
            circleImg1 = (ImageView) itemView.findViewById(R.id.iv_circle_picture_01);
            circleImg2 = (ImageView) itemView.findViewById(R.id.iv_circle_picture_02);
            circleImg3 = (ImageView) itemView.findViewById(R.id.iv_circle_picture_03);
            layoutImg3 = itemView.findViewById(R.id.layout_circle_picture_03);
            tvImgCounts = (TextView) itemView.findViewById(R.id.tv_picture_counts);
            tvReadCounts = (TextView) itemView.findViewById(R.id.tv_user_read_counts);
            tvComments = (TextView) itemView.findViewById(R.id.tv_comment_num);
            tvLikeCounts = (TextView) itemView.findViewById(R.id.tv_like_counts);
            tvTop = (TextView) itemView.findViewById(R.id.tv_top_mark);
        }
    }
}
