package com.example.caokun.fellowsearch.model;

import com.example.caokun.fellowsearch.api.ApiResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by caokun on 2017/2/19.
 */

public interface FellowApi {
    @GET("findFellow")
    Observable<ApiResponse<List<Student>>> getStudent(@Query("province") String province,
                                                      @Query("institute") String institute,
                                                      @Query("major") String major,
                                                      @Query("senior")String senior);
    @GET("getProvinceName")
    Observable<ApiResponse<List<Province>>> getProvince();

    @GET("getInstituteName")
    Observable<ApiResponse<List<Institute>>> getInstitute(@Query("province") String province );

    @GET("getMajorName")
    Observable<ApiResponse<List<Major>>> getMajor(@Query("province") String province,
                                     @Query("institute") String institute);
    @GET("getSeniorName")
    Observable<ApiResponse<List<Senior>>> getSenior(@Query("province") String province);
}
