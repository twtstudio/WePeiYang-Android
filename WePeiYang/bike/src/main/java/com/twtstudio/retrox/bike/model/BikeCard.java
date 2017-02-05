package com.twtstudio.retrox.bike.model;

/**
 * Created by jcy on 2016/8/7.
 */

public class BikeCard {

    /**
     * id : f992415
     * sign : Wjm4eUfj+/lRl3Si4DBTifguG323ZzGAP/F/Yh9ZC5I=
     * record : {"id":"2414068","dep":"229","dep_dev":"2","dep_time":"1469322135","arr":"221","arr_dev":"7","arr_time":"1469322244","duration":"109","fee":"0.00"}
     */

    public String id;
    public String sign;
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
