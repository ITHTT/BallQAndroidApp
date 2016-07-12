package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQUserAccountRecordEntity;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderAdapter;

import java.util.List;
import java.util.Locale;

/**
 * Created by HTT on 2016/6/24.
 */
public class BallQUserAccountRecordAdapter extends RecyclerView.Adapter<BallQUserAccountRecordAdapter.BallQUserAccountRecordViewHolder>
implements StickyHeaderAdapter<BallQUserAccountRecordAdapter.BallQUserAccountRecordViewHolder>{
    private List<BallQUserAccountRecordEntity> accountRecordEntityList;

    public BallQUserAccountRecordAdapter(List<BallQUserAccountRecordEntity> accountRecordEntityList) {
        this.accountRecordEntityList = accountRecordEntityList;
    }

    @Override
    public BallQUserAccountRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_account_record_item,parent,false);
        return new BallQUserAccountRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQUserAccountRecordViewHolder holder, int position) {
        BallQUserAccountRecordEntity info=accountRecordEntityList.get(position);
        holder.tvRecordInfo.setText(info.getNote());
        String moneyInfo=String.format(Locale.getDefault(),"%.2f",(float)info.getAmt()/100);
        holder.tvRecordMoneys.setText(info.getAmt()>0?"+"+moneyInfo:moneyInfo);
        holder.tvRecordTime.setText(info.getTime());
    }

    @Override
    public int getItemCount() {
        return accountRecordEntityList.size();
    }

    @Override
    public void onViewDetachedFromWindow(BallQUserAccountRecordViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public String getHeaderId(int position) {
        if(position>=0&&position<getItemCount()){
            return accountRecordEntityList.get(position).getDate();
        }
        return "";
    }

    @Override
    public BallQUserAccountRecordViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_uer_account_record_header,parent,false);
        return new BallQUserAccountRecordViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(BallQUserAccountRecordViewHolder viewholder, int position) {
        if(position>=0&&position<getItemCount()){
            TextView tvDate= (TextView) viewholder.itemView.findViewById(R.id.tvDate);
            tvDate.setText(accountRecordEntityList.get(position).getDate());
        }
    }

    public static final class BallQUserAccountRecordViewHolder extends RecyclerView.ViewHolder{
        TextView tvRecordTime;
        TextView tvRecordInfo;
        TextView tvRecordMoneys;
        public BallQUserAccountRecordViewHolder(View itemView) {
            super(itemView);
            tvRecordInfo=(TextView)itemView.findViewById(R.id.tv_record_info);
            tvRecordTime=(TextView)itemView.findViewById(R.id.tv_record_time);
            tvRecordMoneys=(TextView)itemView.findViewById(R.id.tv_record_moneys);

        }
    }
}
