package com.twtstudio.tjwhm.lostfound.base;

import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallBean;

import java.util.List;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public class BaseBean {
    public int error_code;
    public String message;
    public List<WaterfallBean.DataBean> data;

    public static class DataBean {
        /**
         * id : 27
         * name : 杨同学
         * title : 捡到银行卡
         * place : 校外
         * time : 前天
         * phone : 13733659842
         * detail_type : 10
         * isback : 0
         * picture :
         */

        public int id;
        public String name;
        public String title;
        public String place;
        public String time;
        public String phone;
        public int detail_type;
        public int isback;
        public String picture;
    }
}
