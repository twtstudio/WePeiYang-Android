package com.twtstudio.service.dishesreviews.evaluate.model

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.twtstudio.service.dishesreviews.model.DishesEvaluateBean
import com.twtstudio.service.dishesreviews.model.DishesService
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

object EvaluateProvider {
    val dishesEvaluateLiveData = MutableLiveData<DishesEvaluateBean>()
    fun postEvaluate(fields: Map<String, Any>, context: Context): MutableLiveData<DishesEvaluateBean> {
        launch(UI) {
            dishesEvaluateLiveData.value = DishesService.evaluate(fields).await().data
        }.invokeOnCompletion {
            it?.let {
                Toasty.error(context, "网络错误 ${it.message}！${it.message?.javaClass.toString()}").show()
            }
        }
        return dishesEvaluateLiveData
    }
}