package com.twtstudio.tjwhm.lostfound.support;

/**
 * Created by tjwhm on 2017/7/7.
 **/

public class IntToType {
    public static String getType(int i) {

        if (i == 1) {
            return "身份证";
        } else if (i == 2) {
            return "饭卡";
        } else if (i == 3) {
            return "手机";
        } else if (i == 4) {
            return "钥匙";
        } else if (i == 5) {
            return "书包";
        } else if (i == 6) {
            return "手表&饰品";
        } else if (i == 7) {
            return "水杯";
        } else if (i == 8) {
            return "U盘&硬盘";
        } else if (i == 9) {
            return "钱包";
        } else if (i == 10) {
            return "银行卡";
        } else if (i == 11) {
            return "书";
        } else if (i == 12) {
            return "伞";
        } else if (i == 13) {
            return "其他";
        }
        return "wrong";
    }
}
