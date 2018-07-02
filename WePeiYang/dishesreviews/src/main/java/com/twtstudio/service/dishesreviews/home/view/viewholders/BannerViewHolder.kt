package com.twtstudio.service.dishesreviews.home.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.dish.view.DishActivity
import com.twtstudio.service.dishesreviews.home.model.BannerImageLoader
import com.twtstudio.service.dishesreviews.home.model.HomeDataViewModel
import com.youth.banner.Banner
import com.youth.banner.BannerConfig


/**
 * Created by zhangyulong on 18-3-23.
 */
class BannerViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val IMAGE_NUMBER = 3
    private val BANNER_DELAY_TIME = 5000
    private val banner = itemView.findViewById<Banner>(R.id.banner_ad)
    private val images = arrayOfNulls<String>(IMAGE_NUMBER)
    private val homeDataViewModel = ViewModelProviders.of(lifecycleOwner as Fragment).get(HomeDataViewModel::class.java)
    private val testImages = arrayOf(R.drawable.dishes_reviews_ju, R.drawable.dishes_reviews_lan, R.drawable.dishes_reviews_zhu)
    private val tvTop = itemView.findViewById<TextView>(R.id.tv_top)
    private val tvDish = itemView.findViewById<TextView>(R.id.tv_dish)
    override fun bind() {
        homeDataViewModel.homeBeanLiveData.bindNonNull(lifecycleOwner) { dish ->
            banner.apply {
                setImageLoader(BannerImageLoader())
                for (i in 0..(IMAGE_NUMBER - 1)) {
                    images[i] = dish.top5Food.get(i).food_picture_address
                }
                setImages(images.toList())
                setIndicatorGravity(BannerConfig.CENTER)
                setOnBannerListener {
                    startDishActivity(dish.top5Food.get(it).food_id)
                }
                setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
                    override fun onPageSelected(position: Int) {
                        tvTop.text = "Top" + (position + 1)
                        tvDish.text = dish.top5Food.get(position).food_name
                    }

                    override fun onPageScrollStateChanged(state: Int) = Unit
                })
                setDelayTime(BANNER_DELAY_TIME)
                start()
            }
        }
    }

    private fun startDishActivity(foodId: Int) {
        val intent = Intent(itemView.context, DishActivity::class.java)
        intent.putExtra("FoodId", foodId)
        itemView.context.startActivity(intent)
    }
}