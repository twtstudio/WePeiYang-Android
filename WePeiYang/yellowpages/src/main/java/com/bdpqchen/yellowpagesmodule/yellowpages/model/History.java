package com.bdpqchen.yellowpagesmodule.yellowpages.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by bdpqchen on 17-3-4.
 */

@Entity
public class History {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "text")
    private String text;

    @Property(nameInDb = "is_reported")
    private int isReported;

    @Property(nameInDb = "total_of_found_data")
    private int totalOfFoundData;

    @Property(nameInDb = "is_deleted")
    private int isDeleted;

    @Generated(hash = 1332729953)
    public History(Long id, String text, int isReported, int totalOfFoundData,
            int isDeleted) {
        this.id = id;
        this.text = text;
        this.isReported = isReported;
        this.totalOfFoundData = totalOfFoundData;
        this.isDeleted = isDeleted;
    }

    @Generated(hash = 869423138)
    public History() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIsReported() {
        return this.isReported;
    }

    public void setIsReported(int isReported) {
        this.isReported = isReported;
    }

    public int getTotalOfFoundData() {
        return this.totalOfFoundData;
    }

    public void setTotalOfFoundData(int totalOfFoundData) {
        this.totalOfFoundData = totalOfFoundData;
    }

    public int getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }


}
