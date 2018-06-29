package com.twtstudio.service.dishesreviews.canteen.model


import android.arch.lifecycle.ViewModel
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twtstudio.service.dishesreviews.model.DishesCanteenBean
import com.twtstudio.service.dishesreviews.model.DishesService

/**
 * Created by SGXM on 2018/5/18.
 */
class CanteenDishInfoViewModel : ViewModel() {
    lateinit var liveData: RefreshableLiveData<DishesCanteenBean, CacheIndicator>
    fun getDishes(canteenId: Int): RefreshableLiveData<DishesCanteenBean, CacheIndicator> {
        val dishBeanLocalData = Cache.hawk<DishesCanteenBean>("Canteen" + canteenId)
        val dishBeanRemote = Cache.from {
            DishesService.getCanteenInfo(canteenId)
        }.map(CommonBody<DishesCanteenBean>::data)
        liveData = RefreshableLiveData.use(dishBeanLocalData, dishBeanRemote)
        return liveData
    }
}