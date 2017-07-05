package com.twtstudio.tjwhm.lostfound.waterfall;

import java.util.List;

/**
 * Created by tjwhm on 2017/7/3.
 **/

public class WaterfallBean {

    /**
     * error_code : -1
     * message :
     * data : [{"id":27,"name":"杨同学","title":"捡到银行卡 ","place":"校外","time":"前天","phone":"13733659842","detail_type":10,"isback":0,"picture":""},{"id":26,"name":"杨同学","title":"捡到银行卡 ","place":"校外","time":"前天","phone":"13733659842","detail_type":10,"isback":0,"picture":""},{"id":25,"name":"杨同学","title":"捡到银行卡 ","place":"校外","time":"前天","phone":"13733659842","detail_type":10,"isback":0,"picture":""},{"id":24,"name":"杨同学","title":"丢失人民币","place":"学三","time":"前天","phone":"13733659842","detail_type":13,"isback":0,"picture":""},{"id":23,"name":"杨同学","title":"丢失人民币","place":"学三","time":"前天","phone":"13733659842","detail_type":13,"isback":0,"picture":""},{"id":22,"name":"杨同学","title":"丢失人民币","place":"学三","time":"前天","phone":"13733659842","detail_type":13,"isback":0,"picture":""},{"id":21,"name":"汪同学","title":"丢失学生证","place":"学五","time":"昨天","phone":"13733659842","detail_type":2,"isback":0,"picture":""},{"id":20,"name":"汪同学","title":"丢失学生证","place":"学五","time":"昨天","phone":"13733659842","detail_type":13,"isback":0,"picture":""}]
     */

    public int error_code;
    public String message;
    public List<DataBean> data;

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
