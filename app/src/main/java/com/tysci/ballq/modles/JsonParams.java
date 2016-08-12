package com.tysci.ballq.modles;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.WrapRecyclerAdapter;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

/**
 * Created by Administrator on 2016-07-17 0017.
 */
public class JsonParams
{
    public static final String STATUS;
    public static final String MESSAGE;

    public static final String JSON_RIGHT;

    public static final String ATTAINED;// 已获得的成就
    public static final String SHOWING;// 已展示的成就
    public static final String UNATTAINED;// 未获得的成就

    static
    {
        STATUS = "status";
        MESSAGE = "message";

        JSON_RIGHT = "OK";

        ATTAINED = "attained";
        SHOWING = "showing";
        UNATTAINED = "unattained";
    }

    public static boolean isJsonRight(String json) throws NullPointerException
    {
        return isJsonRight(JSON.parseObject(json));
    }

    public static boolean isJsonRight(JSONObject object) throws NullPointerException
    {
        return object.getInteger(STATUS) == 0 && object.getString(MESSAGE).equalsIgnoreCase(JSON_RIGHT);
    }

    /**
     * @return add data success
     */
    public static <Bean> boolean addArrayToWrapRecyclerAdapter(String json, boolean isLoadMore, AutoLoadMoreRecyclerView recyclerView, WrapRecyclerAdapter<Bean, ?> adapter, Class<Bean> cls)
    {
        KLog.json(json);
        if (TextUtils.isEmpty(json))
            return false;
        JSONObject object;
        try
        {
            object = JSON.parseObject(json);
        }
        catch (Exception e)
        {
            return false;
        }
        if (object == null || object.isEmpty())
            return false;

        if (!isJsonRight(object))
        {
            ToastUtil.show(recyclerView.getContext(), object.getString(MESSAGE));
            return false;
        }
        JSONArray array;
        try
        {
            array = object.getJSONArray("data");
        }
        catch (Exception e)
        {
            return false;
        }
        if (array == null || array.isEmpty())
        {
            recyclerView.setLoadMoreDataComplete();
            return false;
        }
//        if (recyclerView.getAdapter() != adapter)
//        {
//            recyclerView.setAdapter(adapter);
//        }
        adapter.addDataList(array, isLoadMore, cls);
        if (array.size() >= 10)
        {
            recyclerView.setStartLoadMore();
        }
        else
        {
            recyclerView.setLoadMoreDataComplete();
        }
        return true;
    }
}
