package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQUserMessageRecordEntity;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.List;

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
    public void onBindViewHolder(BallQUserMessageRecordViewHolder holder, int position) {

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
