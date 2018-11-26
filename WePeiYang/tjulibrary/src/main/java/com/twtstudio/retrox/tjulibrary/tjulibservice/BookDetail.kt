package com.twtstudio.retrox.tjulibrary.tjulibservice

import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

object BookDetail {
    fun getDetail(key: String, libDetailCallBack: (Book, String, TotalNum) -> Unit) {
        launch(UI + QuietCoroutineExceptionHandler) {
            val book = LibraryApi.getBook(key).await()
            val totalNum = LibraryApi.getTotalNum(key).await()
            val url = LibraryApi.getImgUrl(book.data.isbn).await()
            if (url[0].result.isEmpty()) {
                libDetailCallBack(book, "error", totalNum)
            } else {
                libDetailCallBack(book, url[0].result[0].coverlink, totalNum)
            }
        }
    }


}