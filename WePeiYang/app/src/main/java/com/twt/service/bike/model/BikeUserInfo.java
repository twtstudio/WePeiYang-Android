package com.twt.service.bike.model;

import java.util.List;

/**
 * Created by jcy on 2016/8/13.
 */

public class BikeUserInfo {

    /**
     * name : 冀辰阳
     * status : 1
     * duration : 236213
     * balance : 9.40
     * recent : [[5,0],[6,0],[7,0],[8,0],[9,0],[10,0],[11,0]]
     * record : {"id":"2414068","dep":"229","dep_dev":"2","dep_time":"1469322135","arr":"221","arr_dev":"7","arr_time":"1469322244","duration":"109","fee":"0.00"}
     */

    public String name;
    public int status;
    public String duration;
    public String balance;
    /**
     * id : 2414068
     * dep : 229
     * dep_dev : 2
     * dep_time : 1469322135
     * arr : 221
     * arr_dev : 7
     * arr_time : 1469322244
     * duration : 109
     * fee : 0.00
     */

    public RecordBean record;
    public List<List<String>> recent;

    public static class RecordBean {
        public String id;
        public String dep;
        public String dep_dev;
        public String dep_time;
        public String arr;
        public String arr_dev;
        public String arr_time;
        public String duration;
        public String fee;
    }


}
