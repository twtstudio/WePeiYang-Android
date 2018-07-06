package com.twtstudio.service.dishesreviews.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twtstudio.service.dishesreviews.R
import org.jetbrains.anko.coroutines.experimental.asReference

val DISH_IMAGE_BASE_URL = "https://open.twtstudio.com/api/v1/food/picture?pictureAddress="
fun ImageView.displayImage(context: Context?, path: String?, scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE,
                           errorImgResID: Int = R.color.white, placeholderResId: Int = R.drawable.dishes_reviews_placeholder) {
    Glide.with(context)
            .load(DISH_IMAGE_BASE_URL + path)
            .asBitmap()
            .placeholder(placeholderResId)
            .error(errorImgResID)
            .into(this)
    this.apply {
        this.scaleType = scaleType
    }
}

fun Context.failureCallback(success: String? = "加载成功", error: String? = "发生错误", refreshing: String? = "加载中"): suspend (RefreshState<*>) -> Unit =
        with(this.asReference()) {
            {
                when (it) {
                    is RefreshState.Failure -> if (error != null) es.dmoral.toasty.Toasty.error(this(), "$error ${it.throwable.message}！${it.javaClass.name}").show()
                }
            }
        }
