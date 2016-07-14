package com.tysci.ballq.bigdata;

/**
 * Created by Administrator on 2016-07-14 0014.
 */
public enum BigDataST {
    FOOTBALL(0), BASKETBALL(1), OTHERS(99);
    public final int value;

    BigDataST(int value) {
        this.value = value;
    }
}
