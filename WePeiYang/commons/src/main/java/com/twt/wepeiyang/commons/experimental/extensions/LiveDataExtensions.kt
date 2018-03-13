package com.twt.wepeiyang.commons.experimental.extensions

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations

inline fun <T, U> LiveData<T>.map(crossinline func: (T) -> U): LiveData<U> =
        Transformations.map(this, { func(it) })

inline fun <T, U> LiveData<T>.switchMap(crossinline func: (T) -> LiveData<U>): LiveData<U> =
        Transformations.switchMap(this, { func(it) })

inline fun <T> LiveData<T>.bind(lifecycleOwner: LifecycleOwner, crossinline block: (T?) -> Unit) =
        observe(lifecycleOwner, Observer { block(it) })

inline fun <T> LiveData<T>.bindNonNull(lifecycleOwner: LifecycleOwner, crossinline block: (T?) -> Unit) =
        observe(lifecycleOwner, Observer { it?.let(block) })

inline fun <T> LiveData<ConsumableMessage<T>>.consume(lifecycleOwner: LifecycleOwner, from: Int, crossinline block: (T) -> Unit) =
        observe(lifecycleOwner, Observer {
            if (it?.isConsumed == false && it.from == from) {
                it.isConsumed = true
                block(it.message)
            }
        })

inline fun <T> LiveData<ConsumableMessage<T>>.consume(lifecycleOwner: LifecycleOwner, crossinline block: (T) -> Unit) =
        observe(lifecycleOwner, Observer {
            if (it?.isConsumed == false) {
                it.isConsumed = true
                block(it.message)
            }
        })

data class ConsumableMessage<out T>(val message: T, val from: Int = FROM_ANY) {

    var isConsumed: Boolean = false

}

const val FROM_ANY = -1