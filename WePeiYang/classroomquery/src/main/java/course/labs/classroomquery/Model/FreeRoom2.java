package course.labs.classroomquery.Model;

import java.util.List;

/**
 * Created by Administrator on 2017/2/10.
 */

public class FreeRoom2 {


        /**
         * errorcode : 0
         * msg :
         * data : [{"room":"33楼104","heating":0,"water_dispenser":0,"power_pack":2,"state":"上课中","collection":"未收藏"},{"room":"33楼105","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼108","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼109","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼112","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼113","heating":0,"water_dispenser":0,"power_pack":2,"state":"上课中","collection":"未收藏"},{"room":"33楼114","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼122","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼123","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼125","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼127","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼129","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼130","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼133","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼134","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼139","heating":0,"water_dispenser":0,"power_pack":2,"state":"上课中","collection":"已收藏"},{"room":"33楼140","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼170","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼172","heating":0,"water_dispenser":0,"power_pack":2,"state":"上课中","collection":"未收藏"},{"room":"33楼181","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼186","heating":0,"water_dispenser":0,"power_pack":2,"state":"上课中","collection":"未收藏"},{"room":"33楼187","heating":0,"water_dispenser":0,"power_pack":2,"state":"上课中","collection":"未收藏"},{"room":"33楼189","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"已收藏"},{"room":"33楼190","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼191","heating":0,"water_dispenser":0,"power_pack":2,"state":"上课中","collection":"未收藏"},{"room":"33楼192","heating":0,"water_dispenser":0,"power_pack":2,"state":"上课中","collection":"未收藏"},{"room":"33楼201","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼204","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼205","heating":0,"water_dispenser":0,"power_pack":2,"state":"上课中","collection":"未收藏"},{"room":"33楼211","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼213","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼214","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼216","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼218","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼220","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼222","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼225","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼227","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼228","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼231","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼232","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼301","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼304","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼305","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲","collection":"未收藏"},{"room":"33楼310","heating":0,"water_dispenser":0,"power_pack":2,"state":"空闲"}]
         */

        private int errorcode;
        private String msg;
        private List<FreeRoom> data;
        public List<FreeRoom> getData(){
            return  this.data;
        }
        public  class FreeRoom {
            /**
             * room : 33楼104
             * heating : 0
             * water_dispenser : 0
             * power_pack : 2
             * state : 上课中
             * collection : 未收藏
             */

            private String room;
            private int heating;
            private int water_dispenser;
            private int power_pack;
            private String state;
            public String collection;
            public String getRoom(){
                return  room;
            }
            public boolean getHeating(){
                return heating>0?true:false;
            }
            public boolean getWater_dispenser(){
                return  water_dispenser>0?true:false;
            }
            public boolean getPower_pack(){
                return power_pack>0?true:false;
            }
            public String getState(){
                System.out.println("state:"+state);
                return  state;
            }
            public boolean isCollected(){
                System.out.println("Collected:"+collection);
                if(collection==null||collection.equals("未收藏")){
                    return  false;
                }
                else{
                    return  true;
                }
            }
            public void setRoom(String room){
                this.room = room;
            }
        }


}
