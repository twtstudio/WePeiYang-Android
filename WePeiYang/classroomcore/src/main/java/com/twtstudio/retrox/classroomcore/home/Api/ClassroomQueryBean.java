package com.twtstudio.retrox.classroomcore.home.Api;

import java.util.List;

/**
 * Created by retrox on 2017/2/23.
 */

public class ClassroomQueryBean {

    /**
     * errorcode : 0
     * msg :
     * data : [{"room":"33楼104","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼105","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼108","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼109","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼112","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼113","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼114","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼116","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼122","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼123","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼125","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼127","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼129","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼130","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼133","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼134","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼139","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼140","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼170","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼172","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼174","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼176","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼181","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼186","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼187","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼189","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"已收藏"},{"room":"33楼190","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼191","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼192","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼201","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼204","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼205","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼211","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼213","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼214","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼216","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼218","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼220","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼222","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼225","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼227","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼228","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼231","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼232","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼301","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼304","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼305","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼310","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"}]
     */

    public int errorcode;
    public String msg;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * room : 33楼104
         * heating : 0
         * water_dispenser : 0
         * power_pack : 2
         * state : 空闲
         * collection : 未收藏
         */

        public String room;
        public int heating;
        public int water_dispenser;
        public int power_pack;
        public String state;
        public String collection;
    }
}
