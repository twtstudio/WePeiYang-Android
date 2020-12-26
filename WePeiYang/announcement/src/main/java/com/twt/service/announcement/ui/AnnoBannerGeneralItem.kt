package com.twt.service.announcement.ui

import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import com.twt.service.announcement.R
import kotlinx.android.synthetic.main.anno_item_general_banner.view.*
import org.jetbrains.anko.layoutInflater

/**
 * 主页模块的通用化封装
 */
class BannerGeneralItem(parent: ViewGroup) {
    private val inflater = parent.context.layoutInflater

    val rootView: View = inflater.inflate(R.layout.anno_item_general_banner, parent, false)

    val contentContainer = rootView.anno_fl_content

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