package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQUserMessageRecordEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/20.
 */
public class BallQUserMessageRecordAdapter extends RecyclerView.Adapter<BallQUserMessageRecordAdapter.BallQUserMessageRecordViewHolder>{
    private List<BallQUserMessageRecordEntity> userMessageRecordEntityList;

    public BallQUserMessageRecordAdapter(List<BallQUserMessageRecordEntity> userMessageRecordEntityList) {
        this.userMessageRecordEntityList = userMessageRecordEntityList;
    }

    @Override
    public BallQUserMessageRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_user_message_item,parent,false);
        return new BallQUserMessageRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BallQUserMessageRecordViewHolder holder, int position) {
        final BallQUserMessageRecordEntity info=userMessageRecordEntityList.get(position);

        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getPt(), R.mipmap.icon_user_default, holder.ivUserIcon);
        if (TextUtils.isEmpty(info.getIsv())) {
            UserInfoUtil.setUserHeaderVMark(0, holder.isV, holder.ivUserIcon);
        }else {
            try {
                UserInfoUtil.setUserHeaderVMark(Integer.parseInt(info.getIsv()), holder.isV, holder.ivUserIcon);
            }catch(Exception e){
                UserInfoUtil.setUserHeaderVMark(0, holder.isV, holder.ivUserIcon);
            }
        }
        holder.tvUserName.setText(info.getFname());

        Date date= CommonUtils.getDateAndTimeFromGMT(info.getCtime());
        if(date!=null){
            holder.tvCreateTime.setText(CommonUtils.getMMddString(date));
        }

        /**爆料*/
        if(info.getEtype()==38){
            holder.tvFaceObject.setText("评论了我");
            holder.layoutRewardContent.setVisibility(View.GONE);
            holder.tvTextContent.setVisibility(View.VISIBLE);
            holder.tvTextContent.setText(info.getCont());
        }else if(info.getEtype()==54){
            /**球经*/
            holder.tvFaceObject.setText("关注了我");
            holder.layoutRewardContent.setVisibility(View.GONE);
            holder.tvTextContent.setVisibility(View.GONE);
        }else if(info.getEtype()==43){
            holder.tvFaceObject.setText("打赏了我");
            holder.layoutRewardContent.setVisibility(View.VISIBLE);
            holder.tvTextContent.setVisibility(View.GONE);
            //holder.tvRewardMoneys.setText(info.getCont());
            String rewardInfo=info.getCont();
            if(!TextUtils.isEmpty(rewardInfo)&&TextUtils.isDigitsOnly(rewardInfo)){
                float rewardMoneys=Float.parseFloat(rewardInfo)/100;
                holder.tvRewardMoneys.setText(String.format(Locale.getDefault(),"%.2f",rewardMoneys));
            }
        }else{
            holder.tvFaceObject.setText("");
            holder.layoutRewardContent.setVisibility(View.GONE);
            holder.tvTextContent.setVisibility(View.GONE);
        }

        holder.ivUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(info.getUid())) {
                    UserInfoUtil.lookUserInfo(holder.itemView.getContext(), Integer.parseInt(info.getUid()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userMessageRecordEntityList.size();
    }

    @Override
    public void onViewDetachedFromWindow(BallQUserMessageRecordViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ButterKnife.unbind(holder);
    }

    public static final class BallQUserMessageRecordViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ivUserIcon)
        CircleImageView ivUserIcon;
        @Bind(R.id.isV)
        ImageView isV;
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.tv_face_object)
        TextView tvFaceObject;
        @Bind(R.id.tv_create_time)
        TextView tvCreateTime;
        @Bind(R.id.tv_text_content)
        TextView tvTextContent;
        @Bind(R.id.layout_reward_content)
        LinearLayout layoutRewardContent;
        @Bind(R.id.tv_reward_moneys)
        TextView tvRewardMoneys;

        public BallQUserMessageRecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


    }
}
