package com.tysci.ballq.modles;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2016-07-17 0017.
 */
public class JsonParams {
    public static final String STATUS;
    public static final String MESSAGE;

    public static final String JSON_RIGHT;

    public static final String ATTAINED;// 已获得的成就
    public static final String SHOWING;// 已展示的成就
    public static final String UNATTAINED;// 未获得的成就

    static {
        STATUS = "status";
        MESSAGE = "message";

        JSON_RIGHT = "OK";

        ATTAINED = "attained";
        SHOWING = "showing";
        UNATTAINED = "unattained";
    }

    public static boolean isJsonRight(JSONObject object) throws NullPointerException {
        return object.getInteger(STATUS) == 0 && object.getString(MESSAGE).equalsIgnoreCase(JSON_RIGHT);
    }
}
