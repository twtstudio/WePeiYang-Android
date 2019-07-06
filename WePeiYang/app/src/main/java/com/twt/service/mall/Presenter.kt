package com.twt.service.mall

import com.twt.service.mall.service.*
import com.twt.service.mall.view.MallActivity2
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class Presenter(private var view: MallActivity2) {

    var infoGoods: List<Goods>? = null
    var infoLogin: List<Login>? = null
    var infoSearch: List<SchGoods>? = null
    var infoMine: List<MyInfo>? = null


    fun getLatest(token: String) {//TODO:待封装个manager...
        launch(UI) {
            MallApi.login(token).awaitAndHandle {
                view.notify("login failed")
            }?.let {
                infoLogin = it.data
                view.notify("login succeed")
            }

            MallApi.latestGoods().awaitAndHandle {
                view.notify("get goods failed")
            }?.let {
                infoGoods = it
                view.refresh()
            }
        }
    }

    fun reLogin(token: String) {
        launch(UI) {
            MallApi.login(token).awaitAndHandle {
                view.notify("login failed")
            }?.let {
                if (it.error_code == -1) {
                    //MyLoginSuccess -> infoLogin
                    infoLogin = it.data
                    view.notify("login succeed")
                }
            }
        }
    }

    fun search(key: String) {
        launch(UI) {
            MallApi.schGoods(key, 1).awaitAndHandle {
                view.notify("search failed")
            }?.let {
                infoSearch = it
                view.notify("search succeed")
            }
        }
    }

    fun getMyInfo(/*cookies*/) {
        launch(UI) {
            MallApi.getMyInfo().awaitAndHandle {
                view.notify("get my info failed")
            }?.let {
                infoMine = it
                view.notify("get my info succeed")
            }
        }
    }

    fun getMenu() {}

    fun addSale() {}
    fun addNeed() {}

}