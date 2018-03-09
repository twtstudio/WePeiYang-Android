package com.twt.wepeiyang.commons.experimental

import kotlinx.coroutines.experimental.Deferred
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by rickygao on 2018/3/5.
 */
class CoroutineCallAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {
        @JvmStatic
        @JvmName("create") // compatible with Java
        operator fun invoke() = CoroutineCallAdapterFactory()
    }

    override fun get(
            returnType: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Deferred::class.java) return null
        TODO("To be implemented soon.")
    }
}