package com.twt.service.rxsrc.api;

import com.twt.service.rxsrc.model.read.Banner;
import com.twt.service.rxsrc.model.read.Detail;
import com.twt.service.rxsrc.model.read.ReadToken;
import com.twt.service.rxsrc.model.read.Recommended;
import com.twt.service.rxsrc.model.read.Review;
import com.twt.service.rxsrc.model.read.SearchBook;
import com.twt.service.rxsrc.model.read.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jcy on 16-10-21.
 */

public interface ReadApi {

    @GET("token/get")
    Observable<ApiResponse<ReadToken>> getReadToken(@Query("wpy_token") String wpy_token);

    //检查readtoken逻辑，暂时通过http code实现
//    @GET("token/check")
//    Observable<ApiResponse<String>> checkReadToken(@Query())

    @GET("banner/{count}")
    Observable<ApiResponse<List<Banner>>> getBanner(@Path("count") String count);

    @GET("recommended/{count}")
    Observable<ApiResponse<List<Recommended>>> getRecommendedList(@Path("count") String count);

    @GET("hotreview/{count}")
    Observable<ApiResponse<List<Review>>> getReviewList(@Path("count") String count);

    @GET("starreader/{count}")
    Observable<ApiResponse<List<User>>> getStarReaderList(@Path("count") String count);

    @GET("search/{info}")
    Observable<ApiResponse<List<SearchBook>>> getSearchBooks(@Path("info") String info);

    @GET("detail/{id}")
    Observable<ApiResponse<Detail>> getBookDetail(@Path("id") String id);

    // TODO: 16-10-22 略迷
//    @GET("review/{id}/{limit}")
//    Observable<ApiResponse<Review>> getBookReview(@Path("id") String id, @Path("limit") String limit);
//
//    @GET("score/{id}")
//    Observable<ApiResponse<>>

    @GET("addbookshelf/{id}")
    Observable<ApiResponse<Void>> addBookShelf(@Path("id") String id);

    @GET("delbookshelf/{id}")
    Observable<ApiResponse<Void>> delBookShelf(@Path("id") String id);
}
