package com.twtstudio.retrox.schedule.model;


import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by retrox on 2017/2/4.
 */

public interface ScheduleApi {

    @GET("classtable")
    Observable<ClassTable> getClassTable();
}
