package com.twt.service.mall

import com.twt.service.mall.model.Goods
import com.twt.service.mall.model.Login
import com.twt.service.mall.model.MallApi
import com.twt.service.mall.model.MallApiService
import com.twt.service.mall.view.MallActivity2
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class Presenter(private var view: MallActivity2) {

    var infoGoods : List<Goods>? = null
    var infoLogin : List<Login>? = null

    fun login(token: String) {
        launch(UI) {
             infoLogin = MallApi.login(token).await().data
        }
    }

    fun getGoods() {
        launch(UI) {
            infoGoods = MallApi.latestGoods().await().data
        }
    }

    fun search(key: String) {}

    fun getMenu() {}

    fun sale() {}
    fun need() {}

}