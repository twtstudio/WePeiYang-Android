package com.twt.wepeiyang.commons.experimental.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ServiceFactoryForExam {

    private const val TRUSTED_HOST = "exam.twtstudio.com"
    private const val BASE_URL = "https://$TRUSTED_HOST/api/"

    internal const val APP_KEY = "9GTdynvrCm1EKKFfVmTC"
    internal const val APP_SECRET = "1aVhfAYBFUfqrdlcT621d9d6OzahMI"

    private val loggingInterceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }

    val client = OkHttpClient.Builder()
            .addInterceptor(UserAgentInterceptor.forTrusted)
            .addInterceptor(SignatureInterceptor.forTrusted)
            .addInterceptor(AuthorizationInterceptor.forTrusted)
            .authenticator(RealAuthenticator)
            .retryOnConnectionFailure(false)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor)
            .addNetworkInterceptor(CodeCorrectionInterceptor.forTrusted)
            .build()

    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)
}
