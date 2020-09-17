package com.twt.service.announcement.service

import kotlinx.coroutines.Deferred
import retrofit2.http.*

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
    fun getQuestion(@QueryMap searchMap: Map<String, Any>): Deferred<CommonBody<List<Question>>>

    /*
    获得问题回复/评论
    http://47.93.253.240:10805/api/user/question/get/answer(commit)?question_id=2
    暂时有点问题 type=commit的时候 没法解析msg的unicode编码
    */
    @GET("question/get/{type}")
    fun getAnswer(@Path("type") type: String,
                  @Query("question_id") question_id: Int): Deferred<CommonBody<List<ReplyOrCommit>>>

    /*
    成功点赞或取消返回null，只用解析错误信息
    点赞/取消点赞 问题/评论/回复
    http://47.93.253.240:10805/api/user/question(answer/commit)/like(dislike)
    */
    @FormUrlEncoded
    @POST("{type}/{isLike}")
    fun postThumbUpORDown(@Path("type") type: String,
                          @Path("isLike") isLike: String,
                          @Field("id") id: Int,
                          @Field("user_id") user_id: Int): Deferred<CommonBody<IsLike>>

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

    这里为什么我们要给answer_id
    */
    @FormUrlEncoded
    @POST("answer/commit")
    fun answerCommit(@Field("user_id") user_id: Int,
                     @Field("answer_id") answer_id: Int,
                     @Field("score") score: Int,
                     @Field("commit") commit: String): Deferred<CommonBody<AnswerCommit>>

    /*
    这个应该是正确data为null，只用解析错误时返回的信息
    添加评论
    http://47.93.253.240:10805/api/user/commit/add/question
    */
    @FormUrlEncoded
    @POST("commit/add/question")
    fun addCommit(@Field("question_id") question_id: Int,
                  @Field("user_id") user_id: Int,
                  @Field("contain") contain: String): Deferred<CommonBody<Commit>>

    /*
    ok
    成功返回list，否则不返回
    获得已点赞的问题/评论/回复列表
    http://47.93.253.240:10805/api/user/likes/get/question(commit/answer)?user_id=1

    这里希望能把返回的改成id
    val data = AnnoService.getLikedQCA(type = "question",user_id = 1).await()
    ->  CommonBody(Error_code=0, msg=获得点赞问题列表成功！, data=[Liked(id=0), Liked(id=0)])
    */
    @GET("likes/get/{type}")
    fun getLikedList(@Path("type") type: String,
                     @Query("user_id") user_id: Int): Deferred<CommonBody<List<Liked>>>

    /*ok
    成功返回true或false，否则返回错误信息
    获取问题/评论/回复的点赞状态
    http://47.93.253.240:10805/api/user/likes/get/isLiked/question?user_id=1&id=1
    */
    @GET("likes/get/isLiked/{type}")
    fun getLikedState(@Path("type") type: String,
                      @Query("user_id") user_id: Int,
                      @Query("id") id: Int): Deferred<CommonBody<IsLike>>

    /*
    ok
    通过id获取user信息
    http://47.93.253.240:10805/api/user/userData?user_id=1
    */
    @GET("userData")
    fun getUserImfByUserId(@Query("user_id") user_id: Int): Deferred<CommonBody<UserData>>

    /*
    ok
    通过学号姓名获得user_id
    http://47.93.253.240:10805/api/user/userId?student_id=3344&name=1122
    */
    @GET("userId")
    fun getUserIDByName(@Query("student_id") student_id: String,
                        @Query("name") name: String): Deferred<CommonBody<UserId>>

    companion object : AnnoService by AnnoServiceFactory()
}

data class TagList(
        val tagList: List<Int>
)

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
        val updated_at: String
)

data class ReplyOrCommit(
        val id: Int,
        val admin_id: Int,
        val user_name: String,
        val contain: String,
        val score: Int,
        val commit: String,
        val likes: Int,
        val created_at: String,
        val updated_at: String,
        val question_id: List<String>
)

data class IsLike(
        val is_liked: Boolean,
        val id: List<String>,
        val user_name: List<String>
)


data class AddQuestion(
        val user_id: List<String>,
        val name: List<String>,
        val description: List<String>,
        val tagList: List<String>
)

data class AnswerCommit(
        val user_id: List<String>,
        val answer_id: List<String>,
        val score: List<String>,
        val commit: List<String>
)

data class Liked(
        val id: Int
)

data class Commit(
        val question_id: List<String>,
        val user_id: List<String>,
        val contain: List<String>
)

data class UserData(
        val id: Int,
        val name: String,
        val student_id: String,
        val user_id: List<String>
)

data class UserId(
        val user_id: Int,
        val student_id: List<String>,
        val name: List<String>
)

data class QuestionBody(
        val user_id: Int,
        val name: String,
        val description: String,
        val tagList: List<Int>
)

