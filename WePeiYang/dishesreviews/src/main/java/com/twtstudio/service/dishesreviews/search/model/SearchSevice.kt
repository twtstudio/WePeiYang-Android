package com.twtstudio.service.dishesreviews.search.model

import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twtstudio.service.dishesreviews.model.DishesSearchBean
import com.twtstudio.service.dishesreviews.model.DishesService

object SearchSevice {
    fun search(keyWord: String): RefreshableLiveData<DishesSearchBean, CacheIndicator> {
        val searchBeanLocalData = Cache.hawk<DishesSearchBean>("DishesSearch")
        val searchBeanRemote = Cache.from {
            DishesService.searchFood(keyWord)
        }.map(CommonBody<DishesSearchBean>::data)
        val dishesBeanLiveData = RefreshableLiveData.use(searchBeanLocalData, searchBeanRemote)
        return dishesBeanLiveData
    }
}