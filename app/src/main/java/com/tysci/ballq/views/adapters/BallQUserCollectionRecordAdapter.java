package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQUserCollectionEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/20.
 */
public class BallQUserCollectionRecordAdapter extends RecyclerView.Adapter<BallQUserCollectionRecordAdapter.BallQUserCollectionRecordViewHolder>{
    private List<BallQUserCollectionEntity> userCollectionEntityList=null;

    public BallQUserCollectionRecordAdapter(List<BallQUserCollectionEntity> userCollectionEntityList) {
        this.userCollectionEntityList = userCollectionEntityList;
    }

    @Override
    public BallQUserCollectionRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ballq_user_collection_item,parent,false);
        return new BallQUserCollectionRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQUserCollectionRecordViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return userCollectionEntityList.size();
    }

    @Override
    public void onViewDetachedFromWindow(BallQUserCollectionRecordViewHolder holder) {
        ButterKnife.unbind(holder);
        super.onViewDetachedFromWindow(holder);
    }

    public static final class BallQUserCollectionRecordViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tvUserNickName)
        TextView tvUserName;
        @Bind(R.id.tvCollectionType)
        TextView tvType;
        @Bind(R.id.tvDate)
        TextView tvDate;
        @Bind(R.id.ivDelete)
        ImageView ivDelete;
        @Bind(R.id.tvContent)
        TextView tvContent;

        public BallQUserCollectionRecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
