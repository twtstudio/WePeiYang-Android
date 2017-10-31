package com.twt.service.update;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by retrox on 31/10/2017.
 */

public interface UpdateApi {
    @GET("https://mobile-api.twtstudio.com/api/app/latest-version/2")
    Observable<UpdateBean> checkUpdateInfo();
}
