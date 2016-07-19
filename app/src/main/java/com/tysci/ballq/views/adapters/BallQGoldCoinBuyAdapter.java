package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQGoldCoinBuyEntity;

import java.util.List;

/**
 * Created by HTT on 2016/7/6.
 */
public class BallQGoldCoinBuyAdapter extends RecyclerView.Adapter<BallQGoldCoinBuyAdapter.BallQGoldCoinBuyViewHolder> {
    private List<BallQGoldCoinBuyEntity> goldCoinBuyEntityList = null;

    private int checkPosition;

    public BallQGoldCoinBuyAdapter(List<BallQGoldCoinBuyEntity> goldCoinBuyEntityList) {
        this.goldCoinBuyEntityList = goldCoinBuyEntityList;
        checkPosition = -1;
    }

    @Override
    public BallQGoldCoinBuyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_buy_gold_coin_item, parent, false);
        return new BallQGoldCoinBuyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQGoldCoinBuyViewHolder holder, final int position) {
        final BallQGoldCoinBuyEntity info = goldCoinBuyEntityList.get(position);
        holder.item.setText(info.getName());
//        if(selectedItem!=null&&selectedItem.getId()==info.getId()){
        if (checkPosition == position) {
            holder.item.setSelected(true);
        } else {
            holder.item.setSelected(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return goldCoinBuyEntityList.size();
    }

    public final BallQGoldCoinBuyEntity getCheckInfo() {
        BallQGoldCoinBuyEntity info = null;
        try {
            info = goldCoinBuyEntityList.get(checkPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public static final class BallQGoldCoinBuyViewHolder extends RecyclerView.ViewHolder {
        CheckBox item;

        public BallQGoldCoinBuyViewHolder(View itemView) {
            super(itemView);
            item = (CheckBox) itemView.findViewById(R.id.gold_coin_item);
        }
    }
}
