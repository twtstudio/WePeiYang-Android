package com.twt.wepeiyang.commons.experimental

import okhttp3.*
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.util.*

/**
 * Created by rickygao on 2018/3/5.
 */
internal typealias ParamsMap = Map<String, String>

// generate timestamp as a context
internal inline fun <T> usingTimeStamp(crossinline block: (String) -> T): T = Calendar.getInstance().timeInMillis.toString().let(block)

internal val ParamsMap.timeStampAndSignature: Pair<String, String>
    get() = usingTimeStamp { t ->
        toSortedMap().apply {
            // put timestamp into params map
            put("t", t)
        }.asSequence().joinToString( // join params in dict order
                separator = "",
                prefix = ServiceFactory.APP_KEY,
                postfix = ServiceFactory.APP_SECRET
        ) { (name, value) -> name + value }.let {
            t to String(Hex.encodeHex(DigestUtils.sha1(it))).toUpperCase()
        }
    }

internal val HttpUrl.queryMap: ParamsMap
    get() = (0 until querySize()).associate {
        queryParameterName(it) to queryParameterValue(it)
    }

internal val FormBody.fieldMap: ParamsMap
    get() = (0 until size()).associate {
        name(it) to value(it)
    }

// hack FormBody to enable newBuilder just like many other immutable classes
internal val FormBody.newBuilder: FormBody.Builder
    get() = (0 until size()).associateTo(mutableMapOf()) {
        encodedName(it) to encodedValue(it)
    }.asSequence().fold(FormBody.Builder()) { builder, (encodedName, encodedValue) ->
        builder.addEncoded(encodedName, encodedValue)
    }

internal val HttpUrl.signed: HttpUrl
    get() = queryMap.timeStampAndSignature.let { (t, sign) ->
        newBuilder().addQueryParameter("t", t)
                .addQueryParameter("sign", sign)
                .addQueryParameter("app_key", ServiceFactory.APP_KEY)
                .build()
    }

internal val RequestBody.signed: RequestBody
    get() = when (this) {
        is FormBody -> fieldMap.timeStampAndSignature.let { (t, sign) ->
            newBuilder.add("t", t)
                    .add("sign", sign)
                    .add("app_key", ServiceFactory.APP_KEY)
                    .build()
        }
        else -> this
    }

internal val Request.signed: Request
    get() = when (method()) {
        "GET" -> newBuilder().url(url().signed).get().build()
        "POST" -> newBuilder().post(body()?.signed).build() // the lint fucks down?
        else -> this
    }

internal object SignatureInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
            chain.proceed(if (chain.request().trusted) chain.request().signed else chain.request())
}