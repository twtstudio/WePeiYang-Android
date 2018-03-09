package com.twt.wepeiyang.commons.experimental

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations

/**
 * Created by rickygao on 2017/11/14.
 */
fun <T, U> LiveData<T>.map(func: (T) -> U): LiveData<U> = Transformations.map(this, func)

fun <T, U> LiveData<T>.switchMap(func: (T) -> LiveData<U>): LiveData<U> = Transformations.switchMap(this, func)

fun <T> LiveData<T>.bind(lifecycleOwner: LifecycleOwner, block: (T?) -> Unit) = observe(lifecycleOwner, Observer(block))

fun <T> LiveData<ConsumableMessage<T>>.consume(lifecycleOwner: LifecycleOwner, from: Int = com.twt.wepeiyang.commons.experimental.ConsumableMessage.Companion.ANY, block: (T?) -> Unit) =
        observe(lifecycleOwner, Observer {
            if (it?.consumed == false && (com.twt.wepeiyang.commons.experimental.ConsumableMessage.Companion.ANY == from || it.from == from)) {
                block(it.message)
                it.consumed = true
            }
        })

data class ConsumableMessage<out T>(val message: T, val from: Int = ANY, var consumed: Boolean = false) {
    companion object {
        const val ANY = -1
    }
}