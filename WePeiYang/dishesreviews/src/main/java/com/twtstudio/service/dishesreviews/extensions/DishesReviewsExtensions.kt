package com.twtstudio.service.dishesreviews.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

val DISH_IMAGE_BASE_URL = "https://open.twtstudio.com/api/v1/food/picture?pictureAddress="
fun ImageView.displayImage(context: Context, path: String?, scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER) {
    Glide.with(context)
            .load(DISH_IMAGE_BASE_URL + path)
            .fitCenter()
            .into(this)
    this.apply {
        this.scaleType = scaleType
    }
}