package com.twtstudio.retrox.tjulibrary.homeitem

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tapadoo.alerter.Alerter
import com.twt.wepeiyang.commons.experimental.bind
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.provider.Book

/**
 * Created by retrox on 26/10/2017.
 */
class BookItemComponent(lifecycleOwner: LifecycleOwner, itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mContext = itemView.context
    private val cover: ImageView = itemView.findViewById(R.id.ic_item_book)
    private val name: TextView = itemView.findViewById(R.id.tv_item_book_name)
    private val returntimeText: TextView = itemView.findViewById(R.id.tv_item_book_return)
    private val bookData = MutableLiveData<Book>()

    init {
        bookData.bind(lifecycleOwner) {
            it?.apply {
                name.text = this.title
                setBookCoverDrawable(book = this)
                returntimeText.text = "应还日期: ${this.returnTime}"
            }
        }
    }

    fun render(): View = itemView

    fun bindBook(book: Book) {
        bookData.value = book
    }

    private fun setBookCoverDrawable(book: Book) {
        var drawable: Drawable = ContextCompat.getDrawable(mContext, R.drawable.lib_book)!!
        val leftDays = book.timeLeft()
        when {
            leftDays > 20 -> DrawableCompat.setTint(drawable, Color.rgb(0, 167, 224)) //blue
            leftDays > 10 -> DrawableCompat.setTint(drawable, Color.rgb(42, 160, 74)) //green
            leftDays > 0 -> {
                if (leftDays < 5) {
                    val act = mContext as? Activity
                    act?.apply {
                        Alerter.create(this)
                                .setTitle("还书提醒")
                                .setBackgroundColor(R.color.assist_color_2)
                                .setText(book.title + "剩余时间不足5天，请尽快还书")
                                .show()
                    }
                }
                DrawableCompat.setTint(drawable, Color.rgb(160, 42, 42)) //red
            }
            else -> drawable = ContextCompat.getDrawable(mContext, R.drawable.lib_warning)!!
        }
        cover.setImageDrawable(drawable)
    }


}