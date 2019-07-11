package com.twt.service.home.message

import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.http.*


interface GeTuiService{
    @FormUrlEncoded
    @POST("register")
    fun register(@FieldMap params : Map<String,String>): Deferred<CommonBody<String>>
    @GET("record/{student_number}")
    fun getRecordMessage(@Path("student_number") student_number: String) : Deferred<RecordMessage>
    @PUT("record/read/{id}")
    fun putId(@Path("id") id :Int) : Deferred<CommonBody<String>>
    companion object :GeTuiService by MessageServiceFactory()

}

internal fun postRegister(uesr_name :String,real_name :String, student_number:String,client_id: String,cover :Int ,school :Int,major: Int,callback: suspend (String) -> (Unit)){
    GlobalScope.launch(Dispatchers.Main){
        val params = mapOf<String,String>(
                "user_name" to uesr_name,
                "real_name" to real_name,
                "student_number" to student_number,
                "client_id" to client_id,
                "cover" to cover.toString(),
                "school" to school.toString(),
                "major" to major.toString()
        )
        GeTuiService.register(params).awaitAndHandle { callback(it.message.orEmpty())}?.let { callback(it.message) }
    }
}
internal fun getRecordMessage(student_number: String ,callback: suspend (RefreshState<Unit>,RecordMessage?) -> Unit) {
    GlobalScope.launch(Dispatchers.Main){
        GeTuiService.getRecordMessage(student_number).awaitAndHandle {
            callback(RefreshState.Failure(it),null)
        }?.let{
            callback(RefreshState.Success(Unit),it)
        }
    }
}
internal fun putId(id: Int,callback: suspend (String) -> (Unit)){
    GlobalScope.launch(Dispatchers.Main){
        GeTuiService.putId(id).awaitAndHandle { callback(it.message.orEmpty()) }?.let { callback(it.message) }
    }
}

data class RecordMessage(
    val error_code: Int,
    val info: List<Info>,
    val message: String
)

data class Info(
    val content: String,
    val created_at: String,
    val id: Int,
    val read: Int,
    val title: String
)