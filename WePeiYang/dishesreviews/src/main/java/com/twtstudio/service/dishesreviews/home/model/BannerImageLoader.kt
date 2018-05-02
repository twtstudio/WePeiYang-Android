package com.twtstudio.service.dishesreviews.home.model

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

class BannerImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        Glide.with(context)
                .load(path)
                .fitCenter()
                .into(imageView)
        imageView!!.apply {
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
    }
}