package com.twtstudio.service.dishesreviews.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.displayImage(context: Context, path: Any) {
    Glide.with(context)
            .load(path)
            .fitCenter()
            .into(this)
    this.apply {
        scaleType = ImageView.ScaleType.CENTER_INSIDE
    }
}