package com.twtstudio.service.dishesreviews.evaluate

import android.widget.CheckedTextView

/**
 * Created by SGXM on 2018/5/18.
 */
class SingleSelector {
    private var singlSelectedCTVs = mutableListOf<CheckedTextView>()

    private var selectPos = 0
    fun add(view: CheckedTextView) {
        singlSelectedCTVs.add(view)
    }

    fun setListener(block: (CheckedTextView) -> Unit) {
        singlSelectedCTVs.withIndex().forEach {
            val index = it.index
            it.value.setOnClickListener {
                if (it is CheckedTextView) {
                    if (selectPos != index) {
                        singlSelectedCTVs[selectPos].isChecked = false
                        it.isChecked = true
                        block(it)
                        selectPos = index
                    }
                }
            }
        }
    }
}