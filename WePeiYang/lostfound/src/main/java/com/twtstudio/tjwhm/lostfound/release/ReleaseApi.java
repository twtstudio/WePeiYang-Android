package com.twtstudio.tjwhm.lostfound.release;

import com.twtstudio.tjwhm.lostfound.base.BaseBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public interface ReleaseApi {
    @POST("lostfound/lost")
    @FormUrlEncoded
    Observable<BaseBean> updateRelease(@FieldMap Map<String, Object> map);
}
