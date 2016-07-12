package com.tysci.ballq.utils;

/**
 * Created by HTT on 2016/6/5.
 */
public class BallQMatchStateUtil {
    private static String getBasketBallMatchState(int state) {
        switch (state) {
            case 0://未开始
                return "未开始";
            case 60://取消
                return "取消";
            case 70://延期
                return "延期";
            case 1://进行中
                return "进行中";
            case 10://第一节
                return "第一节";
            case 20://第二节
                return "第二节";
            case 30://第三节
                return "第三节";
            case 40://第四节
                return "第四节";
            case 50://加时
                return "加时";
            case 80://加时
                return "待定";
            case 100://已结束
            case 90://（算完场）
                return "完场";
            default:
                return "未开始";

        }
    }

    private static String getFootBallMatchState(int status) {
        switch (status) {
            case 0:
                return "未开始";
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return "上半场";
            case 7:
                return "下半场";
            case 31:
                return "中场";
            case 32:
            case 33:
            case 34:
            case 40:
            case 41:
            case 42:
                return "进行中";
            case 60:
            case 61:
                return "延期";
            case 70:
                return "取消";
            case 80:
                return "中断";
            case 90:
                return "弃赛";
            case 50:
            case 100:
            case 110:
            case 120:
                return "完场";
            default:
                return "进行中";
        }
    }

    public static String getMatchState(int status, int etype){
        return etype == 0 ? getFootBallMatchState(status) : getBasketBallMatchState(status);
    }
}
