package com.tysci.ballq.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by LinDe on 2016-07-19 0019.
 *
 * @see android.support.v7.widget.RecyclerView.ViewHolder
 */
public abstract class ButterKnifeRecyclerViewHolder extends RecyclerView.ViewHolder
{

    public ButterKnifeRecyclerViewHolder(View itemView)
    {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
