package com.tysci.ballq.utils;


import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2016/6/7.
 */
public class MatchBettingInfoUtil {

    public static String getBettingOddsTypeInfo(String oType) {
        switch (oType) {
            case "1":
                return "胜平负 ";
            case "2":
                return "大小球 ";
            case "3":
                return "胜负 ";
            case "4":
                return "竞彩让球";
            case "5":
                return "亚盘 ";
            default:
                return "";
        }
    }

    public static String getNoTypeBettingResultInfo(String choice, String otype, String odata) {
        if (!TextUtils.isEmpty(choice) && !TextUtils.isEmpty(otype) && !TextUtils.isEmpty(odata)) {
            JSONObject oddsInfo = JSONObject.parseObject(odata);

            String team = "";
//        String oddsType;
//        switch (otype) {
//            case "1":
//                oddsType = "胜平负 ";
//                break;
//            case "2":
//                oddsType = "大小球 ";
//                break;
//            case "3":// SP
//                oddsType = "亚盘";
//                break;
//            case "4":
//                oddsType = "竞彩让球(" + oddsInfo.optString("HC") + ")";
//                break;
//            case "5":
//                oddsType = "亚盘 ";
//                break;
//            default:
//                return null;
//        }
//            if ("HC".equals(bq.odds_type)) {
//                oddsType = "竞彩让球(" + oddsInfo.optString("HC") + ")";
//            } else if ("AHC".endsWith(bq.odds_type)) {
//                oddsType = "亚盘 ";
//            } else if ("3W".equals(bq.odds_type)) {
//                oddsType = "胜平负 ";
//            } else if ("TO".equals(bq.odds_type)) {
//                oddsType = "大小球 ";
//            }

            switch (choice) {
                case "DO":
                    team = "平@";
                    break;
                case "HO":
                    team = "主胜 @";
                    break;
                case "AO":
                    team = "客胜 @";
                    break;
                case "OO":
                    team = "高于" + oddsInfo.getString("T") + "@";
                    break;
                case "UO":
                    team = "低于" + oddsInfo.getString("T") + "@";
                    break;
                case "MLH":
                    if (Float.parseFloat(oddsInfo.getString("HCH")) > 0) {
                        team = "主队+" + oddsInfo.getString("HCH") + "@";
                    } else {
                        team = "主队" + oddsInfo.getString("HCH") + "@";
                    }
                    break;
                case "MLA":
                    if (Float.parseFloat(oddsInfo.getString("HCA")) > 0) {
                        team = "客队+" + oddsInfo.getString("HCA") + "@";
                    } else {
                        team = "客队" + oddsInfo.getString("HCA") + "@";
                    }
                    break;
            }
            return /*oddsType +*/ team + oddsInfo.getString(choice);
        }
        return null;
    }

    public static <T> String getBettingResultInfo(T _choice, T _otype, String odata) {
        String choice = _choice.toString();
        String otype = _otype.toString();
        if (!TextUtils.isEmpty(choice) && !TextUtils.isEmpty(otype) && !TextUtils.isEmpty(odata)) {
            JSONObject oddsInfo = JSONObject.parseObject(odata);
            String team = "";
            String oddsType;
            switch (otype) {
                case "1":
                    oddsType = "胜平负 ";
                    break;
                case "2":
                    oddsType = "大小球 ";
                    break;
                case "3":// SP
                    oddsType = "亚盘";
                    break;
                case "4":
                    oddsType = "竞彩让球(" + oddsInfo.getString("HC") + ")";
                    break;
                case "5":
                    oddsType = "亚盘 ";
                    break;
                default:
                    return null;
            }
//            if ("HC".equals(bq.odds_type)) {
//                oddsType = "竞彩让球(" + oddsInfo.optString("HC") + ")";
//            } else if ("AHC".endsWith(bq.odds_type)) {
//                oddsType = "亚盘 ";
//            } else if ("3W".equals(bq.odds_type)) {
//                oddsType = "胜平负 ";
//            } else if ("TO".equals(bq.odds_type)) {
//                oddsType = "大小球 ";
//            }

            switch (choice) {
                case "DO":
                    team = "平@";
                    break;
                case "HO":
                    team = "主胜 @";
                    break;
                case "AO":
                    team = "客胜 @";
                    break;
                case "OO":
                    team = "高于" + oddsInfo.getString("T") + "@";
                    break;
                case "UO":
                    team = "低于" + oddsInfo.getString("T") + "@";
                    break;
                case "MLH":
                    if (Float.parseFloat(oddsInfo.getString("HCH")) > 0) {
                        team = "主队+" + oddsInfo.getString("HCH") + "@";
                    } else {
                        team = "主队" + oddsInfo.getString("HCH") + "@";
                    }
                    break;
                case "MLA":
                    if (Float.parseFloat(oddsInfo.getString("HCA")) > 0) {
                        team = "客队+" + oddsInfo.getString("HCA") + "@";
                    } else {
                        team = "客队" + oddsInfo.getString("HCA") + "@";
                    }
                    break;
            }
            return oddsType + team + oddsInfo.getString(choice);
        }
        return null;
    }


}
