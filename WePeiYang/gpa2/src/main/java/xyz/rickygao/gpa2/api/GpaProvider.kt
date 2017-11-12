package xyz.rickygao.gpa2.api

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.orhanobut.hawk.Hawk
import com.twt.wepeiyang.commons.network.RetrofitProvider
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Created by rickygao on 2017/11/9.
 */

object GpaProvider {
    private val api = RetrofitProvider.getRetrofit().create(GpaApi::class.java)
    val gpaLiveData by lazyOf(MutableLiveData<GpaBean>())

    const private val HAWK_KEY_GPA = "GPA"
    fun updateGpaLiveData(useCache: Boolean = true) {
        async(UI) {
            val remote = bg {
                api.getGpa().toBlocking().single()?.data
            }

            if (useCache) {
                bg {
                    Hawk.get<GpaBean?>(HAWK_KEY_GPA, null)
                }.await()?.let {
                    gpaLiveData.value = it
                }
            }

            remote.await()?.takeIf { it != gpaLiveData.value }?.let {
                gpaLiveData.value = it
                Hawk.put<GpaBean>(HAWK_KEY_GPA, it)
            }

        }
    }

}

fun <T, U> LiveData<T>.map(func: (T) -> U): LiveData<U> {
    return Transformations.map(this, func)
}

fun <T, U> LiveData<T>.switchMap(func: (T) -> LiveData<U>): LiveData<U> {
    return Transformations.switchMap(this, func)
}