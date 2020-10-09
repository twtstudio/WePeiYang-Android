package com.twt.service.announcement.service

import android.content.Context
import android.util.Log
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.*
import java.io.Serializable

interface AnnoService {
    /*
    获取整个tag树
    http://47.93.253.240:10805/api/user/tag/get/all
    */
    @GET("tag/get/all")
    fun getTagTree(): Deferred<CommonBody<List<Tag>>>

    /*
    没有searchMap的话返回所有数据，否则根据传递的map筛选
    搜索问题
    http://47.93.253.240:10805/api/user/question/search?search_string=水&tagList=[2]
    两个参数都可空，此时返回全部question
    tagList -> listof<Int>(2)
    */
    @JvmSuppressWildcards
    @GET("question/search")
    fun getQuestion(@QueryMap searchMap: Map<String, Any>): Deferred<CommonBody<QuestionData>>

    /*
    获得问题回复/评论
    http://47.93.253.240:10805/api/user/question/get/answer(commit)?question_id=2
    暂时有点问题 type=commit的时候 没法解析msg的unicode编码
    */
    @GET("question/get/answer")
    fun getAnswer(
            @Query("question_id") question_id: Int,
            @Query("user_id") user_id: Int): Deferred<CommonBody<List<Reply>>>

    @GET("question/get/commit")
    fun getCommit(
            @Query("question_id") question_id: Int,
            @Query("user_id") user_id: Int): Deferred<CommonBody<List<Comment>>>

    /*
    成功点赞或取消返回null，只用解析错误信息
    点赞/取消点赞 问题/评论/回复
    http://47.93.253.240:10805/api/user/question(answer/commit)/like(dislike)
    */
    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("{type}/{isLike}")
    fun postThumbUpOrDown(@Path("type") type: String,
                          @Path("isLike") isLike: String,
                          @Field("id") id: Int,
                          @Field("user_id") user_id: Int): Deferred<CommonBody<Int>>

    /*
     成功添加问题返回null，只用解析错误信息
     添加问题
     http://47.93.253.240:10805/api/user/question/add
     这个地方上传List。。。
     */
    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("question/add")
    fun addQuestion(@FieldMap questions: Map<String, Any>): Deferred<CommonBody<AddQuestion>>


    /*
    成功提交返回null，只用解析错误信息
    评价回复 (给官方回复打分)
    http://47.93.253.240:10805/api/user/answer/commit
    */
    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("answer/commit")
    fun evaluateAnswer(@Field("user_id") user_id: Int,
                       @Field("answer_id") answer_id: Int,
                       @Field("score") score: Int,
                       @Field("commit") commit: String): Deferred<CommonBody<Any>>

    /*
    这个应该是正确data为null，只用解析错误时返回的信息
    添加评论
    http://47.93.253.240:10805/api/user/commit/add/question
    */
    @FormUrlEncoded
    @POST("commit/add/question")
    fun addCommit(@Field("question_id") question_id: Int,
                  @Field("user_id") user_id: Int,
                  @Field("contain") contain: String): Deferred<CommonBody<Any>>

    /*
    成功返回list，否则不返回
    获得已点赞的问题/评论/回复列表
    http://47.93.253.240:10805/api/user/likes/get/question(commit/answer)?user_id=1
    */
    @GET("likes/get/question")
    fun getLikedQuestions(@Query("user_id") user_id: Int): Deferred<CommonBody<List<Question>>>

    @GET("likes/get/answer")
    fun getLikedAnswers(@Query("user_id") user_id: Int): Deferred<CommonBody<List<Reply>>>

    @GET("likes/get/commit")
    fun getLikedCommits(@Query("user_id") user_id: Int): Deferred<CommonBody<List<Comment>>>

    /*
    成功返回true或false，否则返回错误信息
    获取问题/评论/回复的点赞状态
    http://47.93.253.240:10805/api/user/likes/get/isLiked/question?user_id=1&id=1
    */
    @GET("likes/get/isLiked/{type}")
    fun getLikedState(@Path("type") type: String,
                      @Query("user_id") user_id: Int,
                      @Query("id") id: Int): Deferred<CommonBody<IsLike>>

    /*
    通过学号姓名获得user_id
    http://47.93.253.240:10805/api/user/userId?student_id=3344&name=1122
    */
    @GET("userId")
    fun getUserIDByName(@Query("student_id") student_id: String,
                        @Query("name") name: String): Deferred<CommonBody<UserId>>

    /*
    上传图片
    http://47.93.253.240:10805/api/user/image/add
     */
    @Multipart
    @POST("image/add")
    fun postPictures(
            @Part("user_id") user_id: Int,
            @Part newImg: MultipartBody.Part,
            @Part("question_id") question_id: Int): Deferred<CommonBody<picUrl>>

    /**
     * 发送点赞请求
     * @param type 点赞对象的类型，可以是"question"或"answer"或"commit"
     * @param up 是否已经点赞(脑瘫命名)，如果是true的话
     * @param id 点赞对象的id
     * @param userId 这个不用废话，就是[AnnoPreference.myId]
     * @param context 传入上下文
     * @param onRefresh 刷新事件
     */
    suspend fun sendLikeRequest(type: String, up: Boolean, id: Int, userId: Int, context: Context, onRefresh: (likeCount: Int, likeState: Boolean) -> Unit) {
        AnnoService.postThumbUpOrDown(
                type,
                when (up) {
                    true -> "dislike"
                    false -> "like"
                },
                id,
                userId
        ).awaitAndHandle() {
            Toasty.error(context, "点赞状态更新失败").show()
        }?.data?.let {
            onRefresh(it, !up)
            Log.d("tranced", "这里是点赞测试$it")
        }
    }

    companion object : AnnoService by AnnoServiceFactory()
}


data class Tag(
        val id: Int,
        val name: String,
        val children: List<Tag>
)

data class Question(
        val id: Int,
        val name: String,
        val description: String,
        val user_id: Int,
        val solved: Int,
        val no_commit: Int,
        val likes: Int,
        val created_at: String,
        val updated_at: String,
        val username: String,
        val msgCount: Int,
        val url_list: List<String>,
        val thumb_url_list: List<String>,
        val thumbImg: String
) : Serializable

data class Reply(
        val id: Int,
        val admin_id: Int,
        val user_name: String,
        val contain: String,
        val score: Int,
        val commit: String,
        val likes: Int,
        val created_at: String,
        val updated_at: String
) : Serializable

data class Comment(
        val id: Int,
        val contain: String,
        val user_id: Int,
        val likes: Int,
        val created_at: String,
        val updated_at: String,
        val username: String,
        val is_liked: Boolean
) : Serializable

data class IsLike(
        val is_liked: Boolean
)


data class AddQuestion(
        val user_id: List<String>,
        val name: List<String>,
        val description: List<String>,
        val tagList: List<String>,
        val question_id: Int
)

data class UserId(
        val user_id: Int
)


data class QuestionData(
        val data: List<Question>,
        val to: Int,
        val total: Int,
        val next_page_url: String
)

data class picUrl(
        val url: String
)
