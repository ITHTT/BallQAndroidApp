package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQTaskPointsRecordEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/6.
 */
public class BallQTaskPointsRecordAdapter extends RecyclerView.Adapter<BallQTaskPointsRecordAdapter.BallQTaskPointsRecordViewHolder>{
    private List<BallQTaskPointsRecordEntity> pointsRecordEntityList=null;

    public BallQTaskPointsRecordAdapter(List<BallQTaskPointsRecordEntity> pointsRecordEntityList) {
        this.pointsRecordEntityList = pointsRecordEntityList;
    }

    @Override
    public BallQTaskPointsRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_points_record_item,parent,false);
        return new BallQTaskPointsRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQTaskPointsRecordViewHolder holder, int position) {
        BallQTaskPointsRecordEntity info=pointsRecordEntityList.get(position);
        holder.ivTaskState.setSelected(info.getDone()==1);
        holder.tvTaskContent.setText(info.getName());
        holder.tvTaskPoints.setText("+" + info.getPoint());
        holder.divider.setVisibility(position==getItemCount()-1?View.GONE:View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return pointsRecordEntityList.size();
    }

    @Override
    public void onViewDetachedFromWindow(BallQTaskPointsRecordViewHolder holder) {
        ButterKnife.unbind(holder);
        super.onViewDetachedFromWindow(holder);
    }

    public static final class BallQTaskPointsRecordViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ivTaskCheck)
        ImageView ivTaskState;
        @Bind(R.id.vgIntegral)
        View divider;
        @Bind(R.id.tvTaskContent)
        TextView tvTaskContent;
        @Bind(R.id.tvTaskScore)
        TextView tvTaskPoints;

        public BallQTaskPointsRecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
