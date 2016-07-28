package com.tysci.ballq.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LinDe on 2016-07-20 0020.
 *
 * @see android.support.v7.widget.RecyclerView.Adapter
 */
public abstract class WrapRecyclerAdapter<Bean, VH extends ButterKnifeRecyclerViewHolder> extends RecyclerView.Adapter<VH>
{
    private List<Bean> mDataList;

    public WrapRecyclerAdapter()
    {
        this(null);
    }

    public WrapRecyclerAdapter(List<Bean> dataList)
    {
        this.mDataList = dataList;
    }

    public final Bean getItem(int position)
    {
        return mDataList == null ? null : mDataList.get(position);
    }

    @Override
    public int getItemCount()
    {
        return mDataList == null ? 0 : mDataList.size();
    }

    public final boolean isEmpty()
    {
        return mDataList == null || mDataList.isEmpty();
    }

    public final void clear()
    {
        if (mDataList != null)
        {
            mDataList.clear();
            mDataList = null;
        }
        notifyDataSetChanged();
    }

    @Override
    public final void onBindViewHolder(VH holder, int position)
    {
        onBindViewHolder(holder, getItem(position), position);
    }

    public final void addDataList(boolean append, Bean... beans)
    {
        if (mDataList == null)
        {
            mDataList = new ArrayList<>();
        }
        if (!append)
        {
            mDataList.clear();
        }
        if (beans == null || beans.length == 0)
        {
            notifyDataSetChanged();
            return;
        }
        Collections.addAll(mDataList, beans);
        notifyDataSetChanged();
    }

    public final void addDataList(JSONArray array, boolean append, Class<Bean> cls)
    {
        if (mDataList == null)
        {
            mDataList = new ArrayList<>();
        }
        if (!append)
        {
            mDataList.clear();
        }
        if (array == null || array.isEmpty())
        {
            notifyDataSetChanged();
            return;
        }
        Bean bean;
        for (int i = 0, size = array.size(); i < size; i++)
        {
            if (cls == JSONObject.class)
                //noinspection unchecked
                bean = (Bean) array.getJSONObject(i);
            else
                bean = array.getObject(i, cls);
            mDataList.add(bean);
        }
        notifyDataSetChanged();
    }

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindViewHolder(VH holder, Bean bean, int position);
}
