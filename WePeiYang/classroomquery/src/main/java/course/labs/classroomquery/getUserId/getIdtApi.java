package course.labs.classroomquery.getUserId;

import java.util.function.ObjDoubleConsumer;

import course.labs.classroomquery.Model.userId;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/2/19.
 */

public interface getIdtApi {
    @GET("v2/auth/self/")
    Observable<userId>getUserId(@Query("token")String token);
}
