package com.twtstudio.service.dishesreviews.home.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.view.View
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.home.model.BannerImageLoader
import com.youth.banner.Banner
import com.youth.banner.BannerConfig

/**
 * Created by zhangyulong on 18-3-23.
 */
class ADViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val IMAGE_NUMBER = 3
    private val BANNER_DELAY_TIME = 5000
    private val banner = itemView.findViewById<Banner>(R.id.banner_ad)
    private val images = arrayOfNulls<String>(IMAGE_NUMBER)
    private val testImages = arrayOf(R.drawable.dishes_reviews_ju, R.drawable.dishes_reviews_lan, R.drawable.dishes_reviews_zhu)

    init {
        banner.apply {
            setImageLoader(BannerImageLoader())
            setImages(testImages.toMutableList())
            setIndicatorGravity(BannerConfig.CENTER)
            setOnBannerListener {

            }
            setDelayTime(BANNER_DELAY_TIME)
            start()
        }
    }

    override fun bind() {

    }

}