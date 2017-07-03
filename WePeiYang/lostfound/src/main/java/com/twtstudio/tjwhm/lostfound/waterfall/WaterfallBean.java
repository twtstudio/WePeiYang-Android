package com.twtstudio.tjwhm.lostfound.waterfall;

import java.util.List;

/**
 * Created by tjwhm on 2017/7/3.
 **/

public class WaterfallBean {

    /**
     * error_code : -1
     * message : aaa
     * data : [{"id":123,"name":"aa","title":"aaa","place":"aaa","phone":122,"isback":"aaa","picture":"aaa"}]
     */

    public int error_code;
    public String message;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 123
         * name : aa
         * title : aaa
         * place : aaa
         * phone : 122
         * isback : aaa
         * picture : aaa
         */

        public int id;
        public String name;
        public String title;
        public String place;
        public int phone;
        public String isback;
        public String picture;
    }
}
