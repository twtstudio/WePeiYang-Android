package course.labs.classroomquery.Model;

import java.util.List;

/**
 * Created by Administrator on 2017/2/19.
 */

public class CollectedRoom2 {
    private int errorcode;
    private String msg;
    private List<CollectedRoom> data;
    public List<CollectedRoom> getData(){
        return  this.data;
    }
    public class CollectedRoom {
        private int id;
        private String location_building;
        private String location_room;
        private String class_type;
        private String seating_num;
        private String is_classroom;
        private String created_at;
        private String updated_at;


        private int heating;
        private int water_dispenser;
        private int power_pack;
        private String state;


        public String getClassroom(){
            return location_building+location_room;
        }

        public boolean getHeating(){
            return heating>0?true:false;
        }
        public boolean getWater(){
            return  water_dispenser>0?true:false;
        }
        public boolean getPower(){
            return power_pack>0?true:false;
        }
        public String getState(){
            return this.state;
        }
    }



}
