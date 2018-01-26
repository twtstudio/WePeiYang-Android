package com.twtstudio.retrox.bike.extension

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

/**
 * Created by retrox on 26/10/2017.
 */

/**
 * LiveData 自动绑定的kotlin拓展 再也不同手动指定重载了hhh
 */
fun <T> LiveData<T>.bind(lifecycleOwner: LifecycleOwner, block : (T?) -> Unit) {
    this.observe(lifecycleOwner,android.arch.lifecycle.Observer<T>{
        block(it)
    })
}