package com.twtstudio.retrox.tjulibrary.tjulibservice

import android.util.Log
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


interface LibDetailModel {
    interface getBook {
        fun getDetail(key: String)

        fun setDetail(book: Book, url : String, total : TotalNum)
    }

    interface setBook {

        fun setDetailBook(book: Book, url : String, total : TotalNum)
    }


}


class BookDetail(libDetailModel: LibDetailModel.setBook) : LibDetailModel.getBook{
    val libDetailModel = libDetailModel
    override fun getDetail(key: String) {
        launch(UI+ QuietCoroutineExceptionHandler) {
            val book = LibraryApi.getBook(key).await()
            Log.d("detail",book.toString())
            val totalNum = LibraryApi.getTotalNum(key).await()
            val url = LibraryApi.getImgUrl(book.data.isbn).await()
            setDetail(book,url[0].result[0].coverlink,totalNum)
        }

    }

    override fun setDetail(book: Book, url : String, total : TotalNum) {
        this.libDetailModel.setDetailBook(book,url,total)
    }

}