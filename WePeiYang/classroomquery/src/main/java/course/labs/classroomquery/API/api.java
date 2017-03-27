package course.labs.classroomquery.API;

/**
 * Created by asus on 2017/1/23.
 */


import java.util.List;

import course.labs.classroomquery.Model.CollectedRoom2;
import course.labs.classroomquery.Model.FreeRoom2;
import course.labs.classroomquery.Model.collectApiResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface api {
       @GET("Classroom/showCollection")
      Observable<CollectedRoom2>getAllCollectedClassroom(@Query("userId")String token, @Query("week")int week);

       @GET("Classroom/getClassroom")
       Observable<FreeRoom2>getFreeClassroom(@Query("building")int buiding, @Query("week")int week, @Query("time")int time, @Query("userId")String token);

       @GET("Classroom/roomCollection")
       Observable<APIReaponse<collectApiResponse>>collect(@Query("building") String buiding, @Query("userId") String token);
       @GET("Classroom/removeCollection")
       Observable<APIReaponse>cancelCollect(@Query("userId") String token, @Query("building") String building);
}
