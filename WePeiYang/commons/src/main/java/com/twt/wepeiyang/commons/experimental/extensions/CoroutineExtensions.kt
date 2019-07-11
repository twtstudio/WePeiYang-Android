package com.twt.wepeiyang.commons.experimental.extensions

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred


suspend fun <T> Deferred<T>.awaitAndHandle(handler: suspend (Throwable) -> Unit = {}): T? =
        try {
            await()
        } catch (t: Throwable) {
            handler(t)
            null
        }

val QuietCoroutineExceptionHandler = CoroutineExceptionHandler { _, t -> t.printStackTrace() }