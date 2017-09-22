package com.twtstudio.tjwhm.lostfound.base;

import java.util.List;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public class BaseBean {


    /**
     * error_code : -1
     * message :
     * data : [{"id":59,"detail_type":5,"name":"jkoi","title":"hrllo","place":"忘记了","time":"忘记了","phone":"5151","publish_start":"2017-09-01 18:46:57","publish_end":"2017-09-16 18:46:57","other_tag":null,"picture":"uploads/17-07-13/71ce677bf14de2da3341a2c97832113f.jpg"}]
     */

    public int error_code;
    public String message;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 59
         * detail_type : 5
         * name : jkoi
         * title : hrllo
         * place : 忘记了
         * time : 忘记了
         * phone : 5151
         * publish_start : 2017-09-01 18:46:57
         * publish_end : 2017-09-16 18:46:57
         * other_tag : null
         * picture : uploads/17-07-13/71ce677bf14de2da3341a2c97832113f.jpg
         */

        public int id;
        public int detail_type;
        public String name;
        public String title;
        public String place;
        public String time;
        public String phone;
        public String publish_start;
        public String publish_end;
        public Object other_tag;
        public String picture;
    }
}
