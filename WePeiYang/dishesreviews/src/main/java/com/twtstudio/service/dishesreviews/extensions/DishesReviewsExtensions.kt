package com.twtstudio.service.dishesreviews.extensions

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
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

fun ImageView.displayImageFromLocal(context: Context?, path: String?, scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE,
                                    errorImgResID: Int = R.color.white, placeholderResId: Int = R.drawable.dishes_reviews_placeholder) {
    Glide.with(context)
            .load(path)
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

fun getRealFilePath(context: Context, uri: Uri?): String? {
    if (null == uri) return null
    val scheme = uri!!.getScheme()
    var data: String? = null
    if (scheme == null)
        data = uri!!.getPath()
    else if (ContentResolver.SCHEME_FILE == scheme) {
        data = uri!!.getPath()
    } else if (ContentResolver.SCHEME_CONTENT == scheme) {
        val cursor = context.contentResolver.query(uri!!, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (index > -1) {
                    data = cursor.getString(index)
                }
            }
            cursor.close()
        }
    }
    return data
}