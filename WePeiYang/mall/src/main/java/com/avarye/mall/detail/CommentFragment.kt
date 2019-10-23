package com.avarye.mall.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import com.avarye.mall.R

class CommentFragment : BottomSheetDialogFragment() {
    lateinit var behavior: BottomSheetBehavior<View>

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.mall_dialog_comment, null)
        dialog.setContentView(view)

        behavior = BottomSheetBehavior.from(view)

        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

}