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


class BookDetail(private val libDetailModel: LibDetailModel.setBook) : LibDetailModel.getBook{
    override fun getDetail(key: String) {
        launch(UI+ QuietCoroutineExceptionHandler) {
            val book = LibraryApi.getBook(key).await()
            val totalNum = LibraryApi.getTotalNum(key).await()
            val url = LibraryApi.getImgUrl(book.data.isbn).await()
            if (url[0].result.isEmpty()){
                setDetail(book,"error",totalNum)
                    }else{
                setDetail(book,url[0].result[0].coverlink,totalNum)
            }
        }
    }

    override fun setDetail(book: Book, url : String, total : TotalNum) {
        this.libDetailModel.setDetailBook(book,url,total)
    }

}