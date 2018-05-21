package com.twt.wepeiyang.commons.ui.rec

import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import com.twt.wepeiyang.commons.R
import org.jetbrains.anko.layoutInflater

/**
 * 主页模块的通用化封装
 */
class HomeItem(parent: ViewGroup) {
    private val inflater = parent.context.layoutInflater
    val rootView: View = inflater.inflate(R.layout.item_base_home_item, parent, false)
    val itemName = rootView.tv_name_home_item
    val itemContent = rootView.tv_content_home_item
    val contentContainer = rootView.fl_content

    fun setContentView(@LayoutRes layout: Int) {
        val layoutView = inflater.inflate(layout, contentContainer, false)
        contentContainer.removeAllViewsInLayout()
        contentContainer.addView(layoutView)
    }

    fun setContentView(view: View) {
        contentContainer.removeAllViewsInLayout()
        contentContainer.addView(view)
    }
}