package com.twtstudio.retrox.tjulibrary.homeitem

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.databinding.ObservableField
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat

import com.kelin.mvvmlight.base.ViewModel
import com.tapadoo.alerter.Alerter
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.provider.Book

/**
 * Created by retrox on 2017/2/21.
 */

class BookItemViewModel(private val mContext: Context, book: Book) : ViewModel {

    val name = MutableLiveData<String>()

    val retrunTime = MutableLiveData<String>()

    val obBookDrawable = MutableLiveData<Drawable>()

    init {
        name.value = book.title
        retrunTime.value = "应还日期: ${book.returnTime}"
        setBookDrawable(book)

    }

    private fun setBookDrawable(book: Book) {
        var drawable = ContextCompat.getDrawable(mContext, R.drawable.lib_book)
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
            else -> drawable = ContextCompat.getDrawable(mContext, R.drawable.lib_warning)
        }
        obBookDrawable.value = drawable
    }
}
