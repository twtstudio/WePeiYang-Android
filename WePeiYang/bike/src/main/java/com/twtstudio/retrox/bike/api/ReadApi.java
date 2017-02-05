package com.twtstudio.retrox.bike.api;


import com.twtstudio.retrox.bike.model.LatestVersion;
import com.twtstudio.retrox.bike.model.read.BookInShelf;
import com.twtstudio.retrox.bike.model.read.Detail;
import com.twtstudio.retrox.bike.model.read.HomeBanner;
import com.twtstudio.retrox.bike.model.read.ReadToken;
import com.twtstudio.retrox.bike.model.read.Recommended;
import com.twtstudio.retrox.bike.model.read.Review;
import com.twtstudio.retrox.bike.model.read.ReviewCallback;
import com.twtstudio.retrox.bike.model.read.SearchBook;
import com.twtstudio.retrox.bike.model.read.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jcy on 16-10-21.
 */

public interface ReadApi {

    @GET("auth/token/get")
    Observable<ApiResponse<ReadToken>> getReadToken(@Query("wpy_token") String wpy_token);

    //检查readtoken逻辑，暂时通过http code实现
//    @GET("token/check")
//    Observable<ApiResponse<String>> checkReadToken(@Query())

    @GET("banner/{count}")
    Observable<ApiResponse<List<HomeBanner>>> getBanner(@Path("count") String count);

    @GET("recommended/{count}")
    Observable<ApiResponse<List<Recommended>>> getRecommendedList(@Path("count") String count);

    @GET("hotreview/{count}")
    Observable<ApiResponse<List<Review>>> getReviewList(@Path("count") String count);

    @GET("starreader/{count}")
    Observable<ApiResponse<List<User>>> getStarReaderList(@Path("count") String count);

    @GET("book/search/{info}")
    Observable<ApiResponse<List<SearchBook>>> searchBooks(@Path("info") String info);

    @GET("book/detail/{id}?include=review,starreview,holding")
    Observable<ApiResponse<Detail>> getBookDetail(@Path("id") String id);

    // TODO: 16-10-22 略迷
//    @GET("review/{id}/{limit}")
//    Observable<ApiResponse<Review>> getBookReview(@Path("id") String id, @Path("limit") String limit);
//
//    @GET("score/{id}")
//    Observable<ApiResponse<>>

    @GET("book/bookshelf/get")
    Observable<ApiResponse<List<BookInShelf>>> getBookShelf();

    @GET("book/addbookshelf/{id}")
    Observable<ApiResponse<Object>> addBookShelf(@Path("id") String id);

    @GET("book/delbookshelf")
    Observable<ApiResponse<Object>> delBookShelf(@Query("id[]") String[] id);

    @GET("review/addlike/{id}")
    Observable<ApiResponse<Object>> addLike(@Path("id") String id);

    @GET("review/dellike/{id}")
    Observable<ApiResponse<Object>> delLike(@Path("id") String id);

    @GET("review/get")
    Observable<ApiResponse<List<Review>>> getMyReview();

    /**
     * 微北洋的升级接口
     * @return
     */
    @GET("http://mobile.twt.edu.cn/api/v1/version/com.twt.service")
    Observable<LatestVersion> checkLatestVersion();

    @FormUrlEncoded
    @POST("book/review")
    Observable<ApiResponse<ReviewCallback>> addReview(@Field("id") String id, @Field("content") String content, @Field("score") float score);

    @GET("http://api.interlib.com.cn/interlibopac/websearch/metares")
    Observable<ResponseBody> getBookCover(@Query("cmdACT") String act, @Query("isbns") String isbn, @Query("callback") String callback);
}