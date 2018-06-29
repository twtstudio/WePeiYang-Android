package com.twtstudio.service.dishesreviews.home.model

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.twtstudio.service.dishesreviews.extensions.DISH_IMAGE_BASE_URL
import com.youth.banner.loader.ImageLoader

class BannerImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        Glide.with(context)
                .load(DISH_IMAGE_BASE_URL + path)
                .fitCenter()
                .into(imageView)
        imageView!!.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
}