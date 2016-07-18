package com.tysci.ballq.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by LinDe on 2016-07-18 0018.
 * @see BaseAdapter
 */
public abstract class WrapAdapter<Bean, VH extends RecyclerView.ViewHolder> extends BaseAdapter {
    protected ArrayList<Bean> dataList;

    public WrapAdapter(ArrayList<Bean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public final Bean getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        if (convertView == null) {
            holder = onCreateViewHolder(parent);
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            //noinspection unchecked
            holder = (VH) convertView.getTag();
        }
        onBindViewHolder(holder, position, getItem(position));
        return convertView;
    }

    protected abstract VH onCreateViewHolder(ViewGroup parent);

    protected abstract void onBindViewHolder(VH holder, int position, Bean bean);
}
