package course.labs.classroomquery.API;

import android.util.Log;

import rx.functions.Func1;

/**
 * Created by asus on 2017/1/27.
 */
public class apiTransformer<T>  implements Func1<APIReaponse<T>, T> {

    @Override

    public T call(APIReaponse<T> tAPIResponse) {

        if (tAPIResponse.getError_code() != 0) {

            throw new ApiException(tAPIResponse);

        }
        if(tAPIResponse.getData()!=null){
            Log.i("Response Data","not null");
        }
        else{
            Log.i("Respose Date","is null");
        }

        return tAPIResponse.getData();

    }
}
