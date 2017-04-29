package com.twtstudio.retrox.bike.api;


import com.twtstudio.retrox.bike.model.BikeAnnouncement;
import com.twtstudio.retrox.bike.model.BikeAuth;
import com.twtstudio.retrox.bike.model.BikeCard;
import com.twtstudio.retrox.bike.model.BikeRecord;
import com.twtstudio.retrox.bike.model.BikeStation;
import com.twtstudio.retrox.bike.model.BikeUserInfo;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jcy on 2016/8/6.
 */

public interface BikeApi {

    @FormUrlEncoded
    @POST("user/auth")
    Observable<BikeApiResponse<BikeAuth>> getBikeToken(@Field("wpy_tk") String wpy_token);

    @FormUrlEncoded
    @POST("user/card")
    Observable<BikeApiResponse<List<BikeCard>>> getBikeCard(@Field("idnum") String idnum);

    @FormUrlEncoded
    @POST("user/bind")
    Observable<BikeApiResponse<String>> bindBikeCard(@Field("id") String cardId, @Field("sign") String cardSign);

    @FormUrlEncoded
    @POST("user/unbind")
    Observable<BikeApiResponse<Void>> unbindBikeCard(@Field("fake") String fake);

    @GET("station/status")
    Observable<BikeApiResponse<List<BikeStation>>> getStationStaus(@Query("station") String id);

    @FormUrlEncoded
    @POST("user/info")
    Observable<BikeApiResponse<BikeUserInfo>> getUserInfo(@Field("fake") String fake);

    @GET("station/status")
    Observable<BikeApiResponse<List<BikeStation>>> cacheStationStaus();

    @GET("announcement")
    Observable<BikeApiResponse<List<BikeAnnouncement>>> getBikeAnnouncement();

    @FormUrlEncoded
    @POST("user/record")
    Observable<BikeApiResponse<List<BikeRecord>>> getBikeRecord(@Field("month") String month);
}
