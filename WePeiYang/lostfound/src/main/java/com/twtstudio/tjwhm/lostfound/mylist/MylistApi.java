package com.twtstudio.tjwhm.lostfound.mylist;


import com.twtstudio.tjwhm.lostfound.base.BaseBean;
import com.twtstudio.tjwhm.lostfound.base.CallbackBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tjwhm on 2017/7/7.
 **/

public interface MylistApi {
    @GET("lostfound/user/{lostOrFound}")
    Observable<MylistBean> loadMylistData
            (@Path("lostOrFound")String lostOrFound, @Query("page")String page);

    @GET("lostfound/inverse/{id}")
    Observable<CallbackBean> turnStatus(@Path("id") String id);
}
