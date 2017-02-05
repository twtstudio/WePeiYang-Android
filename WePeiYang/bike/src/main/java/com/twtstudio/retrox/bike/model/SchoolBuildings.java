package com.twtstudio.retrox.bike.model;

import java.util.List;

/**
 * Created by jcy on 2016/8/4.
 */

public class SchoolBuildings {

    /**
     * current_week : 23
     * buildings : [{"id":1,"name":"4"},{"id":2,"name":"12"},{"id":3,"name":"15"},{"id":4,"name":"18"},{"id":5,"name":"19"},{"id":6,"name":"21"},{"id":7,"name":"23"},{"id":8,"name":"24"},{"id":9,"name":"25"},{"id":10,"name":"26"},{"id":11,"name":"31"},{"id":12,"name":"32"},{"id":13,"name":"33"},{"id":14,"name":"37"},{"id":15,"name":"43"},{"id":16,"name":"44"},{"id":17,"name":"45"},{"id":18,"name":"46"},{"id":19,"name":"47"},{"id":20,"name":"48"},{"id":21,"name":"50"},{"id":22,"name":"51"},{"id":23,"name":"55"},{"id":24,"name":"西阶"},{"id":25,"name":"综合"}]
     */

    public int current_week;
    /**
     * id : 1
     * name : 4
     */

    public List<BuildingsBean> buildings;

    public static class BuildingsBean {
        public int id;
        public String name;
    }

}
