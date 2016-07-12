package com.tysci.ballq.views.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQBallWarpDetailActivity;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
public class BallQBallWarpInfoAdapter extends RecyclerView.Adapter<BallQBallWarpInfoAdapter.BallQBallWarpInfoViewHolder>{
    private List<BallQBallWarpInfoEntity> ballQInfoListItemEntityList;

    public BallQBallWarpInfoAdapter(List<BallQBallWarpInfoEntity> list){
        this.ballQInfoListItemEntityList=list;
    }
    @Override
    public BallQBallWarpInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_home_ball_warp_item,parent,false);
        return new BallQBallWarpInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQBallWarpInfoViewHolder holder, int position) {
        final BallQBallWarpInfoEntity info=ballQInfoListItemEntityList.get(position);
        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getCover(), R.mipmap.icon_ball_wrap_default_img, holder.ivInfoConver);
        holder.tvUserName.setText(info.getFname());
        holder.tvLikeCounts.setText(String.valueOf(info.getLike_count()));
        holder.tvCommentCounts.setText(String.valueOf(info.getComcount()));
        holder.tvReadCounts.setText(String.valueOf(info.getReading_count()));
        holder.tvRewardCounts.setText(String.valueOf(info.getBoncount()));
        holder.tvTitle.setText(info.getTitle());

        Date date= CommonUtils.getDateAndTimeFromGMT(info.getCtime());
        if(date!=null){
            holder.tvCreateDate.setText(CommonUtils.getDateAndTimeFormatString(date));
        }else{
            holder.tvCreateDate.setText("");
        }

        GlideImageLoader.loadImage(holder.itemView.getContext(),info.getPt(), R.mipmap.icon_user_default,holder.ivUserHeader);
        UserInfoUtil.setUserHeaderVMark(info.getIsv(), holder.isV, holder.ivUserHeader);
        UserInfoUtil.setUserAchievementInfo(holder.itemView.getContext(),info.getTitle1(),holder.ivUserAchievement01,
                info.getTitle2(),holder.ivUserAchievement02);

        if(position==0){
            holder.divider.setVisibility(View.VISIBLE);
        }else{
            holder.divider.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.itemView.getContext(), BallQBallWarpDetailActivity.class);
                intent.putExtra(BallQBallWarpDetailActivity.class.getSimpleName(),info);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

//    public void notifyDatas(String type,String id,int value){
//        int index=getItemById(id);
//        if(index>=0){
//            BallQInfoEntity info=ballQInfoListItemEntityList.get(index);
//            if(type.equals("user_like")){
//                info.setLike_count(info.getLike_count()+value);
//            }else if(type.equals("user_comment")){
//                info.setComcount(info.getComcount()+value);
//            }else if(type.equals("user_read")){
//                info.setReading_count(info.getReading_count()+value);
//            }
//            notifyItemChanged(index);
//        }
//    }
//
//    private int getItemById(String id){
//        if(ballQInfoListItemEntityList!=null&&ballQInfoListItemEntityList.size()>0){
//            int size=ballQInfoListItemEntityList.size();
//            for(int i=0;i<size;i++){
//                if(id.equals(String.valueOf(ballQInfoListItemEntityList.get(i).getId()))){
//                    return i;
//                }
//            }
//        }
//        return -1;
//    }

    @Override
    public int getItemCount() {
        return ballQInfoListItemEntityList.size();
    }

    public static final class BallQBallWarpInfoViewHolder extends RecyclerView.ViewHolder{
        ImageView ivInfoConver;
        TextView tvUserName;
        CircleImageView ivUserHeader;
        ImageView isV;
        ImageView ivUserAchievement01;
        ImageView ivUserAchievement02;
        TextView tvCreateDate;
        TextView tvReadCounts;
        TextView tvTitle;
        TextView tvLikeCounts;
        TextView tvCommentCounts;
        TextView tvRewardCounts;
        View divider;

        public BallQBallWarpInfoViewHolder(View itemView) {
            super(itemView);
            ivInfoConver=(ImageView)itemView.findViewById(R.id.iv_ballq_info_cover);
            tvUserName=(TextView)itemView.findViewById(R.id.tv_ballq_info_author_name);
            ivUserHeader=(CircleImageView)itemView.findViewById(R.id.ivUserIcon);
            isV=(ImageView)itemView.findViewById(R.id.isV);
            ivUserAchievement01=(ImageView)itemView.findViewById(R.id.iv_ballq_info_author_achievement01);
            ivUserAchievement02=(ImageView)itemView.findViewById(R.id.iv_ballq_info_author_achievement02);
            tvCreateDate=(TextView)itemView.findViewById(R.id.tv_ballq_info_create_date);
            tvReadCounts=(TextView)itemView.findViewById(R.id.tv_ballq_info_read_counts);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_ballq_info_title);
            tvLikeCounts=(TextView)itemView.findViewById(R.id.tv_ballq_info_like_count);
            tvCommentCounts=(TextView)itemView.findViewById(R.id.tv_ballq_info_comments_count);
            tvRewardCounts=(TextView)itemView.findViewById(R.id.tv_ballq_info_reward_counts);
            divider=itemView.findViewById(R.id.divider);
        }
    }

}
