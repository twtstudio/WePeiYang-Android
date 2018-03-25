package com.twt.wepeiyang.commons.experimental.cache

import android.arch.lifecycle.LiveData
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by rickygao on 2018/3/23.
 */
enum class CacheIndicator {
    LOCAL, REMOTE
}

abstract class RefreshableLiveData<V, I> : LiveData<V>() {
//    protected var state : State<I>?
//        get() = stateLiveData.value?.message
//        set(value) {
//            stateLiveData.value = if (value == null) null else ConsumableMessage(value)
//        }
//    private val mutableState = MutableLiveData<ConsumableMessage<State<I>>>()
//    val stateLiveData: LiveData<ConsumableMessage<State<I>>> = mutableState

    abstract fun refresh(vararg indicators: I, callback: suspend (State<CacheIndicator>) -> Unit = {})
    abstract fun cancel()

    companion object
}

sealed class State<I> {
    class Success<I>(val indicator: I) : State<I>()
    class Failure<I>(val throwable: Throwable) : State<I>()
    class Refreshing<I> : State<I>()
}

fun <V : Any> RefreshableLiveData.Companion.use(local: Cache<V>, remote: Cache<V>) =
        object : RefreshableLiveData<V, CacheIndicator>() {

            private var running: Job? = null

            override fun refresh(vararg indicators: CacheIndicator, callback: suspend (State<CacheIndicator>) -> Unit) {
                if (running?.isActive == true) return
                running = launch(UI) {

                    callback(State.Refreshing())

                    val handler: suspend (Throwable) -> Unit = { callback(State.Failure(it)) }

                    val localDeferred =
                            if (CacheIndicator.LOCAL in indicators)
                                local.get() else null

                    val remoteDeferred =
                            if (CacheIndicator.REMOTE in indicators)
                                remote.get() else null

                    val localValue = localDeferred?.awaitAndHandle(handler)?.also {
                        value = it
                        callback(State.Success(CacheIndicator.LOCAL))
                    }

                    remoteDeferred?.awaitAndHandle(handler)?.takeIf { localValue != it }?.also {
                        value = it
                        callback(State.Success(CacheIndicator.REMOTE))

                        local.set(it).awaitAndHandle(handler)
                    }

                }.apply { invokeOnCompletion { running = null } }
            }

            override fun cancel() {
                running?.cancel()
            }

            override fun onActive() {
                refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
            }

            override fun onInactive() {
                cancel()
            }

        }