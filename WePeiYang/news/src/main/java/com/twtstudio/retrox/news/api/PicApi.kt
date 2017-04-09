package com.twtstudio.retrox.news.api

import com.twtstudio.retrox.news.api.bean.FangcunBean
import com.twtstudio.retrox.news.api.bean.GalleryIndexBean
import com.twtstudio.retrox.news.api.bean.GalleryPhotoBean
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by retrox on 09/04/2017.
 */

interface PicApi {

    /**
     * 方寸流年API
     * @id 类型 随身拍：1 单反：2
     * @from 从哪个id开始降序排列
     */
    @Headers("Accept: application/json")
    @GET("https://photograph.twtstudio.com/list/{id}")
    fun getFangcunPic(@Path("id") id: Int = 1, @Query("from") from: Int = 0): Observable<FangcunBean>

    /**
     * 主站图解获取索引的API
     */
    @GET("http://www.twt.edu.cn/mapi/galleries/index")
    fun getGalleryIndex(): Observable<List<GalleryIndexBean>>

    /**
     * 主站图解索引内部组图API
     */
    @GET("http://www.twt.edu.cn/mapi/galleries/{id}/photos")
    fun getGalleryPhotos(@Path("id") id: Int ): Observable<List<GalleryPhotoBean>>

}
