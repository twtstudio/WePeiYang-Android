package com.twtstudio.service.dishesreviews.dish.model

import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twtstudio.service.dishesreviews.model.DishesFoodBean
import com.twtstudio.service.dishesreviews.model.DishesService

object DishesFoodProvider {
    fun getDishesFood(foodId: Int): RefreshableLiveData<DishesFoodBean, CacheIndicator> {
        val dishBeanLocalData = Cache.hawk<DishesFoodBean>("DishesFood")
        val dishBeanRemote = Cache.from {
            DishesService.getFood(foodId)
        }.map(CommonBody<DishesFoodBean>::data)
        val dishesBeanLiveData = RefreshableLiveData.use(dishBeanLocalData, dishBeanRemote)
        return dishesBeanLiveData
    }

}