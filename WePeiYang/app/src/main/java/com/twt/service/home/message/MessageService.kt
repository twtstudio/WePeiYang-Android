package com.twt.service.home.message

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET

interface MessageService{
    @GET("v1/app/message")
    fun getMessage():Deferred<CommonBody<MessageBean>>
    companion object : MessageService by ServiceFactory()
}


data class MessageBean(
    val version: Int,
    val title: String,
    val message: String
)