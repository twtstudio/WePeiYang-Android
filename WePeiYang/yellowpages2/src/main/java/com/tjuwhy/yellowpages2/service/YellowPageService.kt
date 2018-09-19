package com.tjuwhy.yellowpages2.service

import com.tjuwhy.yellowpages2.utils.FirstLetterUtil
import com.tjuwhy.yellowpages2.utils.Selector
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.http.*

interface YellowPageService {

    @GET("v1/yellowpage/data3")
    fun getPhone(): Deferred<PhoneBean>

    @GET("v1/yellowpage/collection")
    fun getCollectionList(): Deferred<List<CollectionBean>>

    @GET("v1/yellowpage/updateCollection")
    fun updateCollection(@Query("id") tid: Int): Deferred<UpDateBean>

    @POST("v1/yellowpage/query")
    @FormUrlEncoded
    fun search(@Field("query") keyword: String): Deferred<List<SearchBean>>

    companion object : YellowPageService by ServiceFactory()
}

fun getPhone(callback: suspend (RefreshState<Unit>) -> Unit = {}) {
    launch(UI) {
        YellowPageService.getPhone().awaitAndHandle {
            callback(RefreshState.Failure(it))
        }?.let { phoneBean ->
            val childData = mutableListOf<Array<SubData>>()
            phoneBean.category_list.map { category ->
                var childIndex = 0
                val list = category.department_list
                        .map { SubData(it.department_name, it.department_attach.toInt(), childIndex++, thirdId = it.id) }
                        .sortedBy { Selector(it.title) }
                var num = 0
                var oldChar = ';'
                val result = mutableListOf<SubData>()
                list.forEach {
                    val newChar = FirstLetterUtil.getFirstLetter(it.title)[0].toUpperCase()
                    if (oldChar != newChar) {
                        result.add(num++, SubData(type = ITEM_CHAR, firstChar = newChar))
                        oldChar = newChar
                    }
                    result.add(num++, it)
                }
                childData.add(result.toTypedArray())
            }
            YellowPagePreference.phoneBean = phoneBean
            YellowPagePreference.subArray = childData.toTypedArray()
            callback(RefreshState.Success(Unit))
        }
    }
}

fun getUserCollection(callback: suspend (RefreshState<Unit>) -> Unit = {}) {
    launch(UI) {
        YellowPageService.getCollectionList().awaitAndHandle {
            callback(RefreshState.Failure(it))
        }?.let { collectionList ->
            var childIndex = 0
            val list = collectionList
                    .map { SubData(it.item_name, 0, childIndex++, ITEM_COLLECTION, it.item_phone, true, it.id.toInt()) }
                    .sortedBy { Selector(it.title) }
            var num = 0
            var oldChar = ';'
            val result = mutableListOf<SubData>()
            list.forEach {
                val newChar = FirstLetterUtil.getFirstLetter(it.title)[0].toUpperCase()
                if (oldChar != newChar) {
                    result.add(num++, SubData(type = ITEM_CHAR, firstChar = newChar))
                    oldChar = newChar
                }
                result.add(num++, it)
            }
            YellowPagePreference.collectionList = result.toTypedArray()
            callback(RefreshState.Success(Unit))
        }
    }
}

fun update(id: Int, callback: suspend (RefreshState<Unit>, String) -> Unit) {
    launch(UI) {
        YellowPageService.updateCollection(id).awaitAndHandle {
            callback(RefreshState.Failure(it), it.toString())
        }?.let { updateBean ->
            var childIndex = 0
            YellowPagePreference.collectionList = updateBean.data
                    .map { SubData(it.item_name, 0, childIndex++, ITEM_COLLECTION, it.item_phone, true, it.id.toInt()) }
                    .sortedBy { Selector(it.title) }
                    .toTypedArray()
            callback(RefreshState.Success(Unit), updateBean.status)
        }
    }
}

fun search(keyword: String, callback: suspend (RefreshState<Unit>, List<SearchBean>?) -> Unit) {
    launch(UI) {
        YellowPageService.search(keyword).awaitAndHandle {
            callback(RefreshState.Failure(it), null)
        }?.let {
            callback(RefreshState.Success(Unit), it)
        }
    }
}