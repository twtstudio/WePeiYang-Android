package com.twtstudio.service.classroom.model;

/**
 * Created by zhangyulong on 7/15/17.
 */

public class FilterBean {
    public String text;
    public boolean changeTextColor;
    public boolean changePaddingColor;
    public boolean isOnClickable;
    public boolean hasClicked=false;
    /*
    tag=1时按教学楼筛选
    tag=2时按时间段筛选
    tag=3时按条件筛选
     */
    public int tag;
    public boolean isTopLine;
    public FilterBean(String text,boolean changePaddingColor,int tag,boolean isOnClickable,boolean isTopLine,boolean changeTextColor){
        this.text=text;
        this.changePaddingColor=changePaddingColor;
        this.tag=tag;
        this.isOnClickable=isOnClickable;
        this.isTopLine=isTopLine;
        this.changeTextColor=changeTextColor;
        if(changePaddingColor) hasClicked=true;
        else hasClicked=false;
    }
}
