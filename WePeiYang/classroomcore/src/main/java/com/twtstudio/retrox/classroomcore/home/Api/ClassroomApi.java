package com.twtstudio.retrox.classroomcore.home.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by retrox on 2017/2/23.
 */

public interface ClassroomApi {
    @GET("https://open.twtstudio.com/api/v1/Classroom/getClassroom")
    Observable<ClassroomQueryBean> getClassroomFromBuliding(@Query("building") int building,@Query("week") int week);
}
