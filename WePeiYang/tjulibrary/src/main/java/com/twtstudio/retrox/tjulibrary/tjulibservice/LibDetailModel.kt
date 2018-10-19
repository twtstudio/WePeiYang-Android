package com.twtstudio.retrox.tjulibrary.tjulibservice

import android.app.Activity
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.ArrayList


interface LibDetailModel {
    interface getBook {
        fun getSearch(key: String)

        fun setSearch(book: Book,url : String,total : TotalNum)
    }

    interface setBook {

        fun setSearchBook(book: Book,url : String,total : TotalNum)
    }


}


class BookDetail(libDetailModel: LibDetailModel.setBook) : LibDetailModel.getBook{
    val libDetailModel = libDetailModel
    override fun getSearch(key: String) {
        launch(UI+ QuietCoroutineExceptionHandler) {
            val book = LibraryApi.getBook(key).await()
            val totalNum = LibraryApi.getTotalNum(key).await()
            val url = ImgApi.getImgUrl(book.data.isbn).await()
            setSearch(book,url[0].result[0].coverlink,totalNum)
        }

    }

    override fun setSearch(book: Book,url : String,total : TotalNum) {
        this.libDetailModel.setSearchBook(book,url,total)
    }

}