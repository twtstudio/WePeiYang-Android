package xyz.rickygao.gpa2.ext

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations

/**
 * Created by rickygao on 2017/11/14.
 */
fun <T, U> LiveData<T>.map(func: (T) -> U): LiveData<U> = Transformations.map(this, func)

fun <T, U> LiveData<T>.switchMap(func: (T) -> LiveData<U>): LiveData<U> = Transformations.switchMap(this, func)