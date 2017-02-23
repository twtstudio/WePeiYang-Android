package com.twtstudio.retrox.classroomcore.home.Api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by retrox on 2017/2/23.
 */

public interface ClassroomApi {
    @GET("http://120.27.115.59/test_laravel/public/index.php/api/Classroom/getClassroom")
    Observable<ClassroomQueryBean> getClassroomFromBuliding(@Query("building") int building,@Query("week") int week);
}
