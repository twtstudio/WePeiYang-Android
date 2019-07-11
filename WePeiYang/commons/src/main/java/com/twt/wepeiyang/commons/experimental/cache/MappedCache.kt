package com.twt.wepeiyang.commons.experimental.cache

import kotlinx.coroutines.*


/**
 * Created by rickygao on 2018/3/23.
 */
class MappedCache<V : Any, W : Any>(private val origin: Cache<V>, private val transformer: (V) -> W?) : ReadOnlyCache<W> {
    override fun get(): Deferred<W?> = GlobalScope.async(Dispatchers.Default, CoroutineStart.DEFAULT) { origin.get().await()?.let(transformer) }
}

fun <V : Any, W : Any> Cache<V>.map(transformer: (V) -> W?): Cache<W> = MappedCache<V, W>(this, transformer)