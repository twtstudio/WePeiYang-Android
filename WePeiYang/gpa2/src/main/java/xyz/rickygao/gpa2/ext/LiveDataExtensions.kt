package xyz.rickygao.gpa2.ext

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

fun <T> LiveData<ConsumableMessage<T>>.consume(lifecycleOwner: LifecycleOwner, block: (T?) -> Unit) =
        observe(lifecycleOwner, Observer {
            when (it?.consumed) {
                false -> {
                    block(it.message)
                    it.consumed = true
                }
            }
        })

data class ConsumableMessage<out T>(val message: T, var consumed: Boolean = false)