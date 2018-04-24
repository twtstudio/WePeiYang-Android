package com.twt.service.schedule2.view.custom

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout.LayoutParams.PARENT_ID
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout


class SingleTextTitleUI(val parent: ViewGroup, val itemView: ViewGroup = FrameLayout(parent.context)) : AnkoComponent<Context>, RecyclerView.ViewHolder(itemView) {

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {

        constraintLayout {

            val titleText = textView {
                text = "课程表设置"
                id = View.generateViewId()
                textSize = 16f
                textColor = Color.WHITE
            }.lparams(width = wrapContent, height = wrapContent) {
                startToStart = PARENT_ID
                topToTop = PARENT_ID
                bottomToBottom = PARENT_ID
                margin = dip(16)
            }

        }
    }
}
