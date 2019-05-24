package com.twt.service.theory.model

import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import retrofit2.http.GET

interface TheoryApi{
    //single sign in
//    @GET("v1/library/login/studentLogin")
//    // check
//    @GET("v1/library/login/")
//
//
//    @GET()


   companion object : TheoryApi by ServiceFactory()

}