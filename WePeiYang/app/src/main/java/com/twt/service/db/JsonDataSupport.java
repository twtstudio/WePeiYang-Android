package com.twt.service.db;

import android.database.sqlite.SQLiteDatabase;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * Created by sunjuntao on 16/1/25.
 */
public class JsonDataSupport extends DataSupport {
    @Column
    private String name;
    @Column
    private String data;

    private String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private String getData() {
        return data;
    }

    private void setData(String data) {
        this.data = data;
    }

    public static void setMain(String main) {
        SQLiteDatabase database = Connector.getDatabase();
        JsonDataSupport jsonDataSupport = new JsonDataSupport();
        jsonDataSupport.setName("main");
        jsonDataSupport.setData(main);
        jsonDataSupport.save();
        database.close();
    }

    public static String getMain() {
        SQLiteDatabase database = Connector.getDatabase();
        String result = null;
        List<JsonDataSupport> list = DataSupport.where("name = ?", "main").find(JsonDataSupport.class);
        if (list.size() > 0) {
            result = list.get(0).getData();
        }
        database.close();
        return result;
    }

    public static void setGPA(String gpa) {
        SQLiteDatabase database = Connector.getDatabase();
        JsonDataSupport jsonDataSupport = new JsonDataSupport();
        jsonDataSupport.setName("gpa");
        jsonDataSupport.setData(gpa);
        jsonDataSupport.save();
        database.close();
    }

    public static String getGPA() {
        SQLiteDatabase database = Connector.getDatabase();
        String result = null;
        List<JsonDataSupport> list = DataSupport.where("name = ?", "gpa").find(JsonDataSupport.class);
        if (list.size() > 0) {
            result = list.get(0).getData();
        }
        database.close();
        return result;
    }

    public static void setSchedule(String schedule) {
        SQLiteDatabase database = Connector.getDatabase();
        JsonDataSupport jsonDataSupport = new JsonDataSupport();
        jsonDataSupport.setName("schedule");
        jsonDataSupport.setData(schedule);
        jsonDataSupport.save();
        database.close();
    }

    public static String getSchedule() {
        SQLiteDatabase database = Connector.getDatabase();
        String result = null;
        List<JsonDataSupport> list = DataSupport.where("name = ?", "schedule").find(JsonDataSupport.class);
        if (list.size() > 0) {
            result = list.get(0).getData();
        }
        database.close();
        return result;
    }

    public static void setImportantNews(String importantNews) {
        SQLiteDatabase database = Connector.getDatabase();
        JsonDataSupport jsonDataSupport = new JsonDataSupport();
        jsonDataSupport.setName("importantnews");
        jsonDataSupport.setData(importantNews);
        jsonDataSupport.save();
        database.close();
    }

    public static String getImportantNews() {
        SQLiteDatabase database = Connector.getDatabase();
        String result = null;
        List<JsonDataSupport> list = DataSupport.where("name = ?", "importantnews").find(JsonDataSupport.class);
        if (list.size() > 0) {
            result = list.get(0).getData();
        }
        database.close();
        return result;
    }

    public static void setViewPoint(String viewPoint) {
        SQLiteDatabase database = Connector.getDatabase();
        JsonDataSupport jsonDataSupport = new JsonDataSupport();
        jsonDataSupport.setName("viewpoint");
        jsonDataSupport.setData(viewPoint);
        jsonDataSupport.save();
        database.close();
    }

    public static String getViewPoint() {
        SQLiteDatabase database = Connector.getDatabase();
        String result = null;
        List<JsonDataSupport> list = DataSupport.where("name = ?", "viewpoint").find(JsonDataSupport.class);
        if (list.size() > 0) {
            result = list.get(0).getData();
        }
        database.close();
        return result;
    }

    public static void setAssociationsNews(String associationsNews) {
        SQLiteDatabase database = Connector.getDatabase();
        JsonDataSupport jsonDataSupport = new JsonDataSupport();
        jsonDataSupport.setName("associationnews");
        jsonDataSupport.setData(associationsNews);
        jsonDataSupport.save();
        database.close();
    }

    public static String getAssociationNews() {
        SQLiteDatabase database = Connector.getDatabase();
        String result = null;
        List<JsonDataSupport> list = DataSupport.where("name = ?", "associationnews").find(JsonDataSupport.class);
        if (list.size() > 0) {
            result = list.get(0).getData();
        }
        database.close();
        return result;
    }

    public static void setCollegeNews(String collegeNews) {
        SQLiteDatabase database = Connector.getDatabase();
        JsonDataSupport jsonDataSupport = new JsonDataSupport();
        jsonDataSupport.setName("collegenews");
        jsonDataSupport.setData(collegeNews);
        jsonDataSupport.save();
        database.close();
    }

    public static String getCollegeNews() {
        SQLiteDatabase database = Connector.getDatabase();
        String result = null;
        List<JsonDataSupport> list = DataSupport.where("name = ?", "collegenews").find(JsonDataSupport.class);
        if (list.size() > 0) {
            result = list.get(0).getData();
        }
        database.close();
        return result;
    }

}
