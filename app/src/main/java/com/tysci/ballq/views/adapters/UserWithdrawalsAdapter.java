package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.ButterKnifeRecyclerViewHolder;
import com.tysci.ballq.modles.UserWithdrawalsEntity;
import com.tysci.ballq.utils.CalendarUtil;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderAdapter;

import java.util.List;

import butterknife.Bind;

/**
 * Created by LinDe on 2016-07-18 0018.
 * 提现申请记录适配器
 */
public class UserWithdrawalsAdapter extends RecyclerView.Adapter<UserWithdrawalsAdapter.ViewHolder> implements StickyHeaderAdapter<UserWithdrawalsAdapter.ViewHolder> {
    private List<UserWithdrawalsEntity> dataList;

    public UserWithdrawalsAdapter(List<UserWithdrawalsEntity> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_account_record_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserWithdrawalsEntity info = dataList.get(position);

        CalendarUtil cal = CalendarUtil.parseStringTZ(info.getCtime());
        if (cal != null)
            holder.tvRecordTime.setText(cal.getStringFormat("HH:mm"));

        holder.tvRecordInfo.setText(info.getVerify_status());

        int amount = info.getAmount();
        holder.tvRecordMoneys.setText(amount > 0 ? "+" : "");
        holder.tvRecordMoneys.append(String.valueOf(amount));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public String getHeaderId(int position) {
        if (position > 0 && position < getItemCount()) {
            CalendarUtil cal = CalendarUtil.parseStringTZ(dataList.get(position).getCtime());
            if (cal != null) {
                return cal.getStringFormat("MM-dd");
            }
        }
        return "";
    }

    @Override
    public ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_uer_account_record_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder viewholder, int position) {
        if (position > 0 && position < getItemCount()) {
            TextView tvDate = (TextView) viewholder.itemView.findViewById(R.id.tvDate);
            CalendarUtil cal = CalendarUtil.parseStringTZ(dataList.get(position).getCtime());
            if (cal != null) {
                tvDate.setText(cal.getStringFormat("MM-dd"));
            }
        }
    }

    class ViewHolder extends ButterKnifeRecyclerViewHolder {
        @Bind(R.id.tv_record_time)
        TextView tvRecordTime;
        @Bind(R.id.tv_record_info)
        TextView tvRecordInfo;
        @Bind(R.id.tv_record_moneys)
        TextView tvRecordMoneys;

        public ViewHolder(View itemView) {
            super(itemView);
//            tvRecordInfo = (TextView) itemView.findViewById(R.id.tv_record_info);
//            tvRecordTime = (TextView) itemView.findViewById(R.id.tv_record_time);
//            tvRecordMoneys = (TextView) itemView.findViewById(R.id.tv_record_moneys);

        }
    }
}
