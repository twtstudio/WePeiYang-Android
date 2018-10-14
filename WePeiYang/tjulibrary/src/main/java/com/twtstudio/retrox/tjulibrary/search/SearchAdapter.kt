package com.twtstudio.retrox.tjulibrary.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.twtstudio.retrox.tjulibrary.tjulibservice.SearchBook
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.tjulibservice.Img
import com.twtstudio.retrox.tjulibrary.tjulibservice.LibraryApi
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class SearchAdapter(var searchBook: MutableList<SearchBook>, val context: Context) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lib_item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchBookTemp = searchBook[position]
        holder.searchName.text = searchBookTemp.booktitle
        holder.searchNum.text = searchBookTemp.number
        holder.searchPublishName.text = searchBookTemp.bookpublish
        launch(UI + QuietCoroutineExceptionHandler) {
            val image: Img = LibraryApi.getImg(searchBookTemp.bookID).await()
            image.img_url?.apply {
                Glide.with(this@SearchAdapter.context)
                        .load(this)
                        .into(holder.searchImage)
            }
        }
    }

    override fun getItemCount(): Int {
        return searchBook.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // 初始化控件
        val searchImage: ImageView = view.findViewById(R.id.search_img)
        val searchName: TextView = view.findViewById(R.id.search_name)
        val searchNum: TextView = view.findViewById(R.id.borrow_num)
        val searchPublishName: TextView = view.findViewById(R.id.publish_name)
    }

}