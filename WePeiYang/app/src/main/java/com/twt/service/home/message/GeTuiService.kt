package com.twt.service.home.message

import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

internal const val GETUI_BASE_URL ="47.106.129.155"
interface GeTuiService{
    @FormUrlEncoded
    @POST("http://$GETUI_BASE_URL/api/register")
    fun register(@FieldMap params : Map<String,String>): Deferred<CommonBody<String>>
    companion object :GeTuiService by ServiceFactory()
}

internal fun postRegister(uesr_name :String,real_name :String, student_number:String,client_id: String,cover :Int ,school :Int,major: Int,callback: suspend (String) -> (Unit)){
    launch(UI){
        val params = mapOf<String,String>(
                "user_name" to uesr_name,
                "real_name" to real_name,
                "student_number" to student_number,
                "client_id" to client_id,
                "cover" to cover.toString(),
                "school" to school.toString(),
                "major" to major.toString()
        )
        GeTuiService.register(params).awaitAndHandle { callback(it.message.orEmpty())}?.let {

            callback(it.message) }
    }
}

