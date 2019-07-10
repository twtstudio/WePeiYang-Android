package com.twt.service.schedule2.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.twt.service.schedule2.R
import com.twt.wepeiyang.commons.ui.popup.TopPopupWindow
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class CustomNumberPickerPop(context: Context) : TopPopupWindow(context) {
    @SuppressLint("SetTextI18n")
    override fun createContentView(parent: ViewGroup?): View {
        val view = context.layoutInflater.inflate(R.layout.schedule_number_picker_pop, parent, false).apply {
            layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent).apply {
                gravity = Gravity.BOTTOM
            }

        }
        return view
    }


    init {
        isDismissOnClickBack = true
        isDismissOnTouchBackground = true
    }
}