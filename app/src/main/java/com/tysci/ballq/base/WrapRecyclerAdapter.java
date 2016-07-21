package com.tysci.ballq.base;

import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LinDe on 2016-07-20 0020.
 *
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public abstract class WrapRecyclerAdapter<Bean, VH extends ButterKnifeRecyclerViewHolder> extends RecyclerView.Adapter<VH> {
    private List<Bean> dataList;

    public WrapRecyclerAdapter() {
        this(null);
    }

    public WrapRecyclerAdapter(List<Bean> dataList) {
        this.dataList = dataList;
    }

    public final Bean getItem(int position) {
        return dataList == null ? null : dataList.get(position);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public final boolean isEmpty() {
        return dataList == null || dataList.isEmpty();
    }

    public final void clear() {
        if (dataList != null) {
            dataList.clear();
            dataList = null;
        }
        notifyDataSetChanged();
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, getItem(position), position);
    }

    public final void addDataList(JSONArray array, boolean append, Class<Bean> cls) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        if (!append) {
            dataList.clear();
        }
        if (array == null || array.isEmpty()) {
            notifyDataSetChanged();
            return;
        }
        for (int i = 0, size = array.size(); i < size; i++) {
            Bean bean = array.getObject(i, cls);
            dataList.add(bean);
        }
        notifyDataSetChanged();
    }

    protected abstract void onBindViewHolder(VH holder, Bean bean, int position);
}
