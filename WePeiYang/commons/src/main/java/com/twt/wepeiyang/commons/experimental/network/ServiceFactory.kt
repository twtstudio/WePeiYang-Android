package com.twt.wepeiyang.commons.experimental.network

import com.twt.wepeiyang.commons.experimental.network.ServiceFactory.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * A service factory can create network service easily using retrofit.
 *
 * Due to @property [BASE_URL] not including a version code, *version codes* should be included in
 * service interfaces.
 *
 * @sample RealAuthService
 * @see Retrofit
 * @author rickygao
 */
object ServiceFactory {

    internal const val TRUSTED_HOST = "open.twt.edu.cn"
    internal const val BASE_URL = "https://$TRUSTED_HOST/api/"

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

/**
 * A common wrapper class of respond data from open.twtstudio.com/api.
 *
 * @see com.twt.wepeiyang.commons.experimental.service.AuthService
 */
data class CommonBody<out T>(
        val error_code: Int,
        val message: String,
        val data: T?
)
