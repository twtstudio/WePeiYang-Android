package com.twtstudio.tjwhm.lostfound.release;

import com.twtstudio.tjwhm.lostfound.base.BaseBean;

import java.util.Map;

import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public interface ReleaseApi {
    @POST("lostfound/{lostOrFound}")
    @FormUrlEncoded
    Observable<BaseBean> updateRelease(@FieldMap Map<String, Object> map,
                                       @Path("lostOrFound") String lostOrFound);

    @POST("lostfound/edit/{lostOrFound}/{id}")
    @FormUrlEncoded
    Observable<BaseBean> updateEdit(@FieldMap Map<String, Object> map,
                                    @Path("lostOrFound") String lostOrFound,
                                    @Path("id") String id);
    @DELETE("lostfound/{id}")
    Observable<BaseBean> delete(@Path("id") String id);
}
