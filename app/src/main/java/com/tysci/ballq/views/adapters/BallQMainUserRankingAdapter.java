package com.tysci.ballq.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQUserRankingListDetailActivity;
import com.tysci.ballq.modles.BallQUserRankInfoEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HTT on 2016/7/18.
 */
public class BallQMainUserRankingAdapter extends RecyclerView.Adapter<BallQMainUserRankingAdapter.BallQMainUserRankingViewHolder>{
    private Map<String,List<BallQUserRankInfoEntity>> datas;
    private List<String>keys;

    public BallQMainUserRankingAdapter(Map<String, List<BallQUserRankInfoEntity>> datas) {
        this.datas = datas;
        keys=new ArrayList<>(5);
        keys.addAll(datas.keySet());
    }

    @Override
    public BallQMainUserRankingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_index_rank_item, parent, false);
        return new BallQMainUserRankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQMainUserRankingViewHolder holder, int position) {
        final String key=keys.get(position);
        holder.rankType.setText(key);
        List<BallQUserRankInfoEntity> rankInfoEntityList=datas.get(key);
        int size=rankInfoEntityList.size();
        for(int i=0;i<size;i++){
            setRankUserInfo(holder,key,i,rankInfoEntityList.get(i));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=holder.itemView.getContext();
                if(UserInfoUtil.checkLogin(context)) {
                    Intent intent = null;
                    if (key.equals("总盈利榜")) {
                        intent = new Intent(context, BallQUserRankingListDetailActivity.class);
                        intent.putExtra("title", key);
                        intent.putExtra("rank_type", "tearn");
                    } else if (key.equals("盈利率榜")) {
                        intent = new Intent(context, BallQUserRankingListDetailActivity.class);
                        intent.putExtra("title", key);
                        intent.putExtra("rank_type", "ror");
                    } else if (key.equals("亚盘胜率榜")) {
                        intent = new Intent(context, BallQUserRankingListDetailActivity.class);
                        intent.putExtra("title", key);
                        intent.putExtra("rank_type", "wins");
                    } else if (key.equals("人气榜")) {
                        intent = new Intent(context, BallQUserRankingListDetailActivity.class);
                        intent.putExtra("title", key);
                        intent.putExtra("rank_type", "follow");
                    }
                    if (intent != null) {
                        context.startActivity(intent);
                    }
                }else{
                    UserInfoUtil.userLogin(context);
                }
            }
        });
    }

    private void setRankUserInfo(BallQMainUserRankingViewHolder holder,String key,int i, final BallQUserRankInfoEntity info){
        final Context context=holder.itemView.getContext();
        CircleImageView ivUserHeader = null;
        ImageView ivUserV = null;
        TextView tvUserName = null;
        TextView tvRankNum = null;
        TextView tvRounds = null;
        if(i==0){
            ivUserHeader=holder.imageView1;
            ivUserV=holder.ivV1;
            tvUserName=holder.textView1;
            tvRankNum=holder.tvRankNum1;
            tvRounds=holder.tvRounds1;
        }else if (i == 1) {
            ivUserHeader=holder.imageView2;
            ivUserV=holder.ivV2;
            tvUserName=holder.textView2;
            tvRankNum=holder.tvRankNum2;
            tvRounds=holder.tvRounds2;
        }else if(i==2){
            ivUserHeader=holder.imageView3;
            ivUserV=holder.ivV3;
            tvUserName=holder.textView3;
            tvRankNum=holder.tvRankNum3;
            tvRounds=holder.tvRounds3;
        }else if(i==3){
            ivUserHeader=holder.imageView4;
            ivUserV=holder.ivV4;
            tvUserName=holder.textView4;
            tvRankNum=holder.tvRankNum4;
            tvRounds=holder.tvRounds4;
        }else if(i==4){
            ivUserHeader=holder.imageView5;
            ivUserV=holder.ivV5;
            tvUserName=holder.textView5;
            tvRankNum=holder.tvRankNum5;
            tvRounds=holder.tvRounds5;
        }

        if(ivUserHeader!=null) {
            GlideImageLoader.loadImage(context, info.getPt(), R.mipmap.icon_user_default, ivUserHeader);
            UserInfoUtil.setUserHeaderVMark(info.getIsv(), ivUserV, ivUserHeader);
            tvUserName.setText(info.getFname());
            if(key.equals("总盈利榜")){
                int color= Color.parseColor("#eb3a3a");
                tvRankNum.setTextColor(color);
                holder.ivLine1.setBackgroundColor(color);
                holder.ivLine2.setBackgroundColor(color);
                tvRankNum.setText(String.format(Locale.getDefault(),"%.2f",(float)info.getTearn()/100f));
            }else if(key.equals("盈利率榜")){
                int color= Color.parseColor("#cdaa44");
                tvRankNum.setTextColor(color);
                holder.ivLine1.setBackgroundColor(color);
                holder.ivLine2.setBackgroundColor(color);
                tvRankNum.setText(String.format(Locale.getDefault(),"%.2f",info.getRor())+"%");
            }else if(key.equals("亚盘胜率榜")){
                int color= Color.parseColor("#1bbc9b");
                tvRankNum.setTextColor(color);
                holder.ivLine1.setBackgroundColor(color);
                holder.ivLine2.setBackgroundColor(color);
                tvRankNum.setText(String.format(Locale.getDefault(),"%.2f",info.getWins())+"%");
            }else if(key.equals("人气榜")){
                int color= Color.parseColor("#397ebf");
                tvRankNum.setTextColor(color);
                holder.ivLine1.setBackgroundColor(color);
                holder.ivLine2.setBackgroundColor(color);
                tvRankNum.setText(String.valueOf(info.getFrc()));
            }

            tvRounds.setText("场次:" + info.getTipcount());
            //ivUserHeader.setTag(info.getUid());
            final CircleImageView finalIvUserHeader = ivUserHeader;
            ivUserHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoUtil.lookUserInfo(context, info.getUid());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static final class BallQMainUserRankingViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.rankType)
        TextView rankType;
        @Bind(R.id.imageView1)
        CircleImageView imageView1;
        @Bind(R.id.imageView2)
        CircleImageView imageView2;
        @Bind(R.id.imageView3)
        CircleImageView imageView3;
        @Bind(R.id.imageView4)
        CircleImageView imageView4;
        @Bind(R.id.imageView5)
        CircleImageView imageView5;
        @Bind(R.id.ivV1)
        ImageView ivV1;
        @Bind(R.id.ivV2)
        ImageView ivV2;
        @Bind(R.id.ivV3)
        ImageView ivV3;
        @Bind(R.id.ivV4)
        ImageView ivV4;
        @Bind(R.id.ivV5)
        ImageView ivV5;
        @Bind(R.id.textView1)
        TextView textView1;
        @Bind(R.id.textView2)
        TextView textView2;
        @Bind(R.id.textView3)
        TextView textView3;
        @Bind(R.id.textView4)
        TextView textView4;
        @Bind(R.id.textView5)
        TextView textView5;
        @Bind(R.id.ivLine1)
        ImageView ivLine1;
        @Bind(R.id.ivLine2)
        ImageView ivLine2;
        @Bind(R.id.vgRankNum)
        ViewGroup vgRankNum;
        @Bind(R.id.tvRankNum1)
        TextView tvRankNum1;
        @Bind(R.id.tvRankNum2)
        TextView tvRankNum2;
        @Bind(R.id.tvRankNum3)
        TextView tvRankNum3;
        @Bind(R.id.tvRankNum4)
        TextView tvRankNum4;
        @Bind(R.id.tvRankNum5)
        TextView tvRankNum5;
        @Bind(R.id.vgRounds)
        ViewGroup vgRounds;
        @Bind(R.id.tvRounds1)
        TextView tvRounds1;
        @Bind(R.id.tvRounds2)
        TextView tvRounds2;
        @Bind(R.id.tvRounds3)
        TextView tvRounds3;
        @Bind(R.id.tvRounds4)
        TextView tvRounds4;
        @Bind(R.id.tvRounds5)
        TextView tvRounds5;

        public BallQMainUserRankingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
