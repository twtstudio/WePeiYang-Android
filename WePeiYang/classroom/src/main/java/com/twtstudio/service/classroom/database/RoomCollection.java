package com.twtstudio.service.classroom.database;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.model.FreeRoom2;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by DefaultAccount on 2017/8/22.
 */
@Entity
public class RoomCollection {
    @Id(autoincrement = true)
    private Long id;
    private String room;
    private int heating;
    private int water_dispenser;
    private int power_pack;
    private String state;
    private String uid= CommonPrefUtil.getStudentNumber();
    public boolean collection;
    @Generated(hash = 330329623)
    public RoomCollection(Long id, String room, int heating, int water_dispenser,
            int power_pack, String state, String uid, boolean collection) {
        this.id = id;
        this.room = room;
        this.heating = heating;
        this.water_dispenser = water_dispenser;
        this.power_pack = power_pack;
        this.state = state;
        this.uid = uid;
        this.collection = collection;
    }
    @Generated(hash = 569992977)
    public RoomCollection() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRoom() {
        return this.room;
    }
    public void setRoom(String room) {
        this.room = room;
    }
    public int getHeating() {
        return this.heating;
    }
    public void setHeating(int heating) {
        this.heating = heating;
    }
    public int getWater_dispenser() {
        return this.water_dispenser;
    }
    public void setWater_dispenser(int water_dispenser) {
        this.water_dispenser = water_dispenser;
    }
    public int getPower_pack() {
        return this.power_pack;
    }
    public void setPower_pack(int power_pack) {
        this.power_pack = power_pack;
    }
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public boolean getCollection() {
        return this.collection;
    }
    public void setCollection(boolean collection) {
        this.collection = collection;
    }
    public FreeRoom2.FreeRoom toFreeRoom(){
        FreeRoom2.FreeRoom freeRoom=new FreeRoom2.FreeRoom();
        freeRoom.setRoom(room);
        freeRoom.setCollection(collection);
        freeRoom.setHeating(heating);
        freeRoom.setPower_pack(power_pack);
        freeRoom.setState(state);
        freeRoom.setWater_dispenser(water_dispenser);
        return freeRoom;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
}
