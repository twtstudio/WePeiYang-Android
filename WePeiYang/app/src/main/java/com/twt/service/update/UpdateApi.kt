package com.twt.service.update

import retrofit2.http.GET
import rx.Observable

/**
 * Created by retrox on 31/10/2017.
 */

interface UpdateApi {
    @GET("https://mobile-api.twt.edu.cn/api/app/latest-version/2")
    fun checkUpdateInfo(): Observable<UpdateBean>
}
