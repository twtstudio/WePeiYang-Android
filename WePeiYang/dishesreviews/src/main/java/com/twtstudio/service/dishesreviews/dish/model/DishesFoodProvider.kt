package com.twtstudio.service.dishesreviews.dish.model


import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twtstudio.service.dishesreviews.model.DishesCollectBean
import com.twtstudio.service.dishesreviews.model.DishesFoodBean
import com.twtstudio.service.dishesreviews.model.DishesService
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

object DishesFoodProvider {
    val dishBeanLiveData = MutableLiveData<DishesFoodBean>()
    val dishCollectLiveData = MutableLiveData<DishesCollectBean>()
    val dishDeleteCollectLiveData = MutableLiveData<CommonBody<String>>()
    lateinit var dishesBeanLiveData: RefreshableLiveData<DishesFoodBean, CacheIndicator>
    fun getDishesFood(foodId: Int): RefreshableLiveData<DishesFoodBean, CacheIndicator> {
        val dishBeanLocalData = Cache.hawk<DishesFoodBean>("DishesFood")
        val dishBeanRemote = Cache.from {
            DishesService.getFood(foodId)
        }.map(CommonBody<DishesFoodBean>::data)
        dishesBeanLiveData = RefreshableLiveData.use(dishBeanLocalData, dishBeanRemote)
        return dishesBeanLiveData
    }

    fun getDishesFood(foodId: Int, context: Context): MutableLiveData<DishesFoodBean> {
        launch(UI) {
            dishBeanLiveData.value = DishesService.getFood(foodId).await().data
        }.invokeOnCompletion {
            it?.let {
                Toasty.error(context, "网络错误 ${it.message}！${it.message?.javaClass.toString()}").show()
            }
        }
        return dishBeanLiveData
    }

    fun collectFood(foodId: Int, userId: Int, context: Context): MutableLiveData<DishesCollectBean> {
        launch(UI) {
            dishCollectLiveData.value = DishesService.collectFood(foodId, userId).await().data
        }.invokeOnCompletion {
            it?.let {
                Toasty.error(context, "网络错误 ${it.message}！${it.message?.javaClass.toString()}").show()
            }

        }
        return dishCollectLiveData
    }

    fun deletCollectedFood(foodId: Int, userId: Int, context: Context): MutableLiveData<CommonBody<String>> {
        launch(UI) {
            dishDeleteCollectLiveData.value = DishesService.deleteCollectedFood(foodId, userId).await()
        }.invokeOnCompletion {
            it?.let {
                Toasty.error(context, "网络错误 ${it.message}！${it.message?.javaClass.toString()}").show()
            }
        }
        return dishDeleteCollectLiveData
    }
}