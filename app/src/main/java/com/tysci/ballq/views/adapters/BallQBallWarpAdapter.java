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
import com.tysci.ballq.activitys.BallQBallWarpDetailActivity;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQBallWarpAdapter extends RecyclerView.Adapter<BallQBallWarpAdapter.BallQBallWarpViewHolder>{
    private List<BallQBallWarpInfoEntity> ballQInfoListItemEntityList;

    public BallQBallWarpAdapter(List<BallQBallWarpInfoEntity> ballQInfoListItemEntityList) {
        this.ballQInfoListItemEntityList = ballQInfoListItemEntityList;
    }

    @Override
    public BallQBallWarpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_ball_warp_item,parent,false);
        return new BallQBallWarpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQBallWarpViewHolder holder, int position) {
        final BallQBallWarpInfoEntity info=ballQInfoListItemEntityList.get(position);
        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getPt(), R.mipmap.icon_user_default, holder.ivUserHeader);
        UserInfoUtil.setUserHeaderVMark(info.getIsv(), holder.ivUserV, holder.ivUserHeader);
        holder.tvUserName.setText(info.getFname());
        holder.tvReadCounts.setText(String.valueOf(info.getReading_count()));
        holder.tvTitle.setText(info.getTitle());
        GlideImageLoader.loadImage(holder.itemView.getContext(),info.getCover(),R.mipmap.icon_ball_wrap_default_img,holder.ivImg);
        Date tipDate= CommonUtils.getDateAndTimeFromGMT(info.getCtime());
        if(tipDate!=null){
            String dateInfo=CommonUtils.getDateAndTimeFormatString(tipDate);
            if(!TextUtils.isEmpty(dateInfo)){
                String[] dates=dateInfo.split(" ");
                if(dates!=null){
                    holder.tvCreateDate.setText(dates[0]);
                    holder.tvCreateTime.setText(dates[dates.length-1]);
                }
            }
        }else{
            holder.tvCreateTime.setText("");
            holder.tvCreateDate.setText("");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=holder.itemView.getContext();
                Intent intent=new Intent(context, BallQBallWarpDetailActivity.class);
                intent.putExtra(BallQBallWarpDetailActivity.class.getSimpleName(),info);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ballQInfoListItemEntityList.size();
    }

    public static final class BallQBallWarpViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserHeader;
        @Bind(R.id.isV)
        ImageView ivUserV;
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.iv_user_level)
        ImageView ivUserLevel;
        @Bind(R.id.tv_create_date)
        TextView tvCreateDate;
        @Bind(R.id.tv_create_time)
        TextView tvCreateTime;
        @Bind(R.id.tv_read_counts)
        TextView tvReadCounts;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.iv_ball_ward_img)
        ImageView ivImg;

        public BallQBallWarpViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
