package com.twt.wepeiyang.commons.experimental.cache

import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

/**
 * Created by rickygao on 2018/3/23.
 */
interface Cache<V : Any> {
    fun get(): Deferred<V?>
    fun set(value: V): Deferred<Unit>

    companion object
}

interface ReadOnlyCache<V : Any> : Cache<V> {
    override fun set(value: V): Deferred<Unit> =
            throw IllegalStateException("ReadOnlyCache cannot be set a value.")
}

fun <V : Any> Cache.Companion.from(generator: () -> Deferred<V>): Cache<V> = object : ReadOnlyCache<V> {
    override fun get(): Deferred<V?> = generator()
}

fun <V : Any> Cache.Companion.retrofit(generator: () -> Call<V>): Cache<V> = object : ReadOnlyCache<V> {
    override fun get(): Deferred<V?> =
            CompletableDeferred<V>().apply {
                val call = generator()

                invokeOnCompletion {
                    if (isCancelled) {
                        call.cancel()
                    }
                }

                call.enqueue(object : Callback<V> {
                    override fun onFailure(call: Call<V>, t: Throwable) {
                        completeExceptionally(t)
                    }

                    override fun onResponse(call: Call<V>, response: Response<V>) {
                        if (response.isSuccessful) {
                            complete(response.body()!!)
                        } else {
                            completeExceptionally(HttpException(response))
                        }
                    }
                })

            }
}

fun <V : Any> Cache.Companion.hawk(key: String): Cache<V> = object : Cache<V> {
    override fun get(): Deferred<V?> = async(CommonPool) { Hawk.get<V>(key) }

    override fun set(value: V): Deferred<Unit> =
            async(CommonPool) { if (!Hawk.put<V>(key, value)) throw RuntimeException("Failed to set value $value for key $key.") }

}