package com.twtstudio.service.classroom.model;

/**
 * Created by asus on 2017/1/23.
 */


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ClassRoomApi {
    @GET("Classroom/showCollection")
    Observable<CollectedRoom2> getAllCollectedClassroom(@Query("userId") String token, @Query("week") int week);

    @GET("Classroom/getClassroom")
    Observable<FreeRoom2> getFreeClassroom(@Query("building") int buiding, @Query("week") int week,@Query("day") int day, @Query("time") int time, @Query("userId") String token);

    @GET("Classroom/roomCollection")
    Observable<ClassRoomApiReaponse<collectApiResponse>> collect(@Query("building") String buiding, @Query("userId") String token);

    @GET("Classroom/removeCollection")
    Observable<ClassRoomApiReaponse> cancelCollect(@Query("userId") String token, @Query("building") String building);


}
