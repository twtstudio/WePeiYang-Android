package com.twtstudio.tjwhm.lostfound.detail;

/**
 * Created by tjwhm on 2017/7/7.
 **/

public class DetailBean {

    /**
     * error_code : -1
     * message :
     * data : {"id":24,"type":0,"title":"丢失人民币","name":"杨同学","time":"前天","place":"学三","phone":"13733659842","item_description":"一张桂林山水","detail_type":13,"picture":"","card_name":null,"card_number":null,"publish_start":"2017-07-04 15:04:11","publish_end":"2017-08-03 15:04:11","other_tag":"钱"}
     */

    public int error_code;
    public String message;
    public DataBean data;

    public static class DataBean {
        /**
         * id : 24
         * type : 0
         * title : 丢失人民币
         * name : 杨同学
         * time : 前天
         * place : 学三
         * phone : 13733659842
         * item_description : 一张桂林山水
         * detail_type : 13
         * picture :
         * card_name : null
         * card_number : null
         * publish_start : 2017-07-04 15:04:11
         * publish_end : 2017-08-03 15:04:11
         * other_tag : 钱
         */

        public int id;
        public int type;
        public String title;
        public String name;
        public String time;
        public String place;
        public String phone;
        public String item_description;
        public int detail_type;
        public String picture;
        public String card_name;
        public String card_number;
        public String publish_start;
        public String publish_end;
        public String other_tag;
    }
}
