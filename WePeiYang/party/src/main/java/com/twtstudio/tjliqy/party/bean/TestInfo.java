package com.twtstudio.tjliqy.party.bean;

/**
 * Created by dell on 2016/7/22.
 */
public class TestInfo {
    private int test_id;
    private String test_name;
    private String test_begintime;
    private String test_attation;
    private String test_filename;
    private String test_filepath;
    private String test_status;
    private String test_isdeleted;

    private int train_id;
    private String train_name;
    private String train_begintime;

    private int has_entry;

    public void setTrain_begintime(String train_begintime) {
        this.train_begintime = train_begintime;
    }

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getTest_begintime() {
        return test_begintime;
    }

    public void setTest_begintime(String test_begintime) {
        this.test_begintime = test_begintime;
    }

    public String getTest_attation() {
        return test_attation;
    }

    public void setTest_attation(String test_attation) {
        this.test_attation = test_attation;
    }

    public String getTest_filename() {
        return test_filename;
    }

    public void setTest_filename(String test_filename) {
        this.test_filename = test_filename;
    }

    public String getTest_filepath() {
        return test_filepath;
    }

    public void setTest_filepath(String test_filepath) {
        this.test_filepath = test_filepath;
    }

    public String getTest_status() {
        return test_status;
    }

    public void setTest_status(String test_status) {
        this.test_status = test_status;
    }

    public String getTest_isdeleted() {
        return test_isdeleted;
    }

    public void setTest_isdeleted(String test_isdeleted) {
        this.test_isdeleted = test_isdeleted;
    }

    public int getTrain_id() {
        return train_id;
    }

    public void setTrain_id(int train_id) {
        this.train_id = train_id;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getTrain_begintime() {
        return train_begintime;
    }

    public int getHas_entry() {
        return has_entry;
    }

    public void setHas_entry(int has_entry) {
        this.has_entry = has_entry;
    }

}
