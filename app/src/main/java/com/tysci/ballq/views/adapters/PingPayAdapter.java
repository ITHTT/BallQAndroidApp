package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysci.ballq.R;

import java.util.ArrayList;

/**
 * Created by LinDe on 2016-07-18 0018.
 * Ping++ 适配器
 */
public class PingPayAdapter extends WrapAdapter<Integer, PingPayAdapter.ViewHolder> {
    private int checkPosition;

    public PingPayAdapter() {
        super(new ArrayList<Integer>());

        dataList.add(1);
        dataList.add(10);
        dataList.add(50);
        dataList.add(100);
        dataList.add(500);
        dataList.add(1000);

        checkPosition = -1;
    }

    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_ping_pay_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, final int position, Integer integer) {
        String amount = integer + "元";
        holder.tv.setText(amount);

        holder.tv.setSelected(checkPosition == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    public int getCheckAmount() {
        if (checkPosition == -1)
            return 0;
        try {
            return dataList.get(checkPosition);
        } catch (Exception e) {
            return 0;
        }
    }

    public void removeCheckAmount() {
        checkPosition = -1;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
