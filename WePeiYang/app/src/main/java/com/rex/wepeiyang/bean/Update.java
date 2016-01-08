package com.rex.wepeiyang.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sunjuntao on 16/1/3.
 */
public class Update implements Serializable{
    public String name;
    public String versionShort;
    public String changelog;
    public String install_url;
    public String build;
}
