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
    搜索问题
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
    通过user_id获得userData
    http://47.93.253.240:10805/api/user/userData?user_id=1
     */
    @GET("userData")
    fun getUserDataById(@Query("user_id") user_id: Int): Deferred<CommonBody<UserData>>

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

    /*
    获取自己发布的问题
    http://47.93.253.240:10805/api/user/question/get/myQuestion?user_id=1&limits=0
     */
    @GET("question/get/myQuestion")
    fun getMyQuestion(
            @Query("user_id") user_id: Int,
            @Query("limits") limits: Int = 0): Deferred<CommonBody<List<Question>>>

    /*
    删除问题
    http://47.93.253.240:10805/api/user/question/delete
     */
    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("question/delete")
    fun deleteQuestion(
            @Field("user_id") user_id: Int,
            @Field("question_id") question_id: Int): Deferred<CommonBody<Any>>

    /*
    通过questionID获取问题
    http://47.93.253.240:10805/api/user/question/get/byId?id=199&user_id=2
     */
    @GET("question/get/byId")
    fun getDetailQuestion(@Query("id") id: Int, @Query("user_id") user_id: Int): Deferred<CommonBody<Question>>



    companion object : AnnoService by AnnoServiceFactory()
}

data class UserData(
        val id: Int,
        val name: String,
        val student_id: String,
        val my_question_num: Int,
        val my_solved_question_num: Int,
        val my_liked_question_num: Int,
        val my_commit_num: Int
)

data class Tag(
        val id: Int,
        val name: String,
        val tag_description: String,
        val children: List<Tag>
) : Serializable

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
        val thumbImg: String,
        val tags: List<Tag>,
        val is_liked: Boolean
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
        val question_id: Int,
        val campus: Int
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
