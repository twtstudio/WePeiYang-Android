package com.twt.wepeiyang.commons.experimental.extensions

import kotlinx.coroutines.experimental.Deferred

suspend fun <T> Deferred<T>.awaitAndHandle(handler: (Throwable) -> Unit = {}) = run {
    invokeOnCompletion { it?.let(handler) }
    await()
}