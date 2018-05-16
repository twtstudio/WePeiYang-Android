package com.twtstudio.service.dishesreviews

import android.support.multidex.MultiDexApplication
import com.twt.wepeiyang.commons.experimental.CommonContext

class DishesReviewsApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        CommonContext.apply {
            registerApplication(this@DishesReviewsApp)
        }
    }
}