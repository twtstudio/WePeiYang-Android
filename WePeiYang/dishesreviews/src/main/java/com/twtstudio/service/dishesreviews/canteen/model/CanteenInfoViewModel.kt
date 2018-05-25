package com.twtstudio.service.dishesreviews.canteen.model


import android.arch.lifecycle.ViewModel
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twtstudio.service.dishesreviews.model.DishesCanteenBean
import com.twtstudio.service.dishesreviews.model.DishesService

/**
 * Created by SGXM on 2018/5/18.
 */
class CanteenInfoViewModel : ViewModel() {
    lateinit var canteenLiveData: RefreshableLiveData<DishesCanteenBean, CacheIndicator>
    fun getCanteenInfo(canteenId: Int): RefreshableLiveData<DishesCanteenBean, CacheIndicator> {
        val CanteenInfoLocalData = Cache.hawk<DishesCanteenBean>("DishesFood" + canteenId)
        val CanteenInfoRemote = Cache.from { DishesService.getCanteenInfo(canteenId) }.map { it -> it.data }
        canteenLiveData = RefreshableLiveData.use(CanteenInfoLocalData, CanteenInfoRemote)
        return canteenLiveData
    }
}
