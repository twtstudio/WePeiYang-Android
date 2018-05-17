package com.twtstudio.service.dishesreviews

import android.support.multidex.MultiDexApplication
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twtstudio.retrox.auth.view.LoginActivity

//模块单独运行时使用
class DishesReviewsApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        CommonContext.apply {
            registerApplication(this@DishesReviewsApp)
            registerActivity("login", LoginActivity::class.java)
        }
    }
}