package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tysci.ballq.R;

/**
 * Created by Administrator on 2016/7/14.
 */
public class BallQFindContentAdapter extends RecyclerView.Adapter<BallQFindContentAdapter.BallQFindContentViewHolder>{

    @Override
    public BallQFindContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_find_content_item,parent,false);
        return new BallQFindContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallQFindContentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static final class BallQFindContentViewHolder extends RecyclerView.ViewHolder{

        public BallQFindContentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
