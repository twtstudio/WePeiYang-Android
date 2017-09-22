package com.twtstudio.tjwhm.lostfound.release;

import com.twtstudio.tjwhm.lostfound.base.BaseBean;
import com.twtstudio.tjwhm.lostfound.base.CallbackBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @Multipart
    @POST("lostfound/{lostOrFound}")
    Observable<BaseBean> updateReleaseWithPic(@Path("lostOrFound") String lostOrFound,
                                              @Part List<MultipartBody.Part> partList
//                                              @Part("title") String title,
//                                              @Part("time") String time,
//                                              @Part("place")String place,
//                                              @Part("detail_type")int detail_type,
//                                              @Part("card_number")String card_number,
//                                              @Part("card_name")String card_name,
    );

    @POST("lostfound/edit/{lostOrFound}/{id}")
    @FormUrlEncoded
    Observable<BaseBean> updateEdit(@FieldMap Map<String, Object> map,
                                    @Path("lostOrFound") String lostOrFound,
                                    @Path("id") String id);

    @POST("lostfound/edit/{lostOrFound}/{id}")
    @Multipart
    Observable<BaseBean> updateEditWithPic(@Path("lostOrFound") String lostOrFound,
                                               @Path("id") String id,
                                               @Part List<MultipartBody.Part> partList);

    @DELETE("lostfound/{id}")
    Observable<BaseBean> delete(@Path("id") String id);
}
