package com.twt.service.mall

import android.util.Log
import com.twt.service.mall.service.*
import com.twt.service.mall.view.MallActivity2
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class Presenter(private var view: MallActivity2) {
    var infoGoods: List<Goods>? = null
    var infoLogin: Login? = null
    var infoSearch: List<SchGoods>? = null
    var infoMine: MyInfo? = null
    var infoMenu: List<Menu>? = null


    //TODO:待封装个manager...
    fun getLatest(page: Int) {//先后台登陆再拿数据
        launch(UI) {
            MallApi.login(Utils.getToken()).awaitAndHandle {
                view.notify("login failed")
            }?.let {
                infoLogin = it.data
                view.notify("login succeed")
//                Log.d("login done", Utils.getToken())
//                Log.d("login done", "login done")
            }

            MallApi.latestGoods(Utils.toReqBody(page)).awaitAndHandle {
                view.notify("get goods failed")
//                Log.d("login get goods failed", it.message)
            }?.let { item ->
                Log.d("login goods done", "get goods done")
                infoGoods = item
                Log.d("login goods done", item[0].campus)
                //挨个拿图片
                for (i in 1 until infoGoods!!.size) {
                    MallApi.getImage(infoGoods!![i].id).awaitAndHandle {
                        view.notify("get image $i failed")
                        Log.d("login get image $i failed", infoGoods!![i].id)
                    }?.let {
                        infoGoods!![i].img = it.toString()
                    }
                }
                view.refreshView(infoGoods!!)
            }
        }
    }

    fun search(key: String, page: Int) {
        launch(UI) {
            MallApi.schGoods(Utils.toReqBody(key), Utils.toReqBody(page)).awaitAndHandle {
                view.notify("search failed")
            }?.let {
                infoSearch = it
                view.notify("search succeed")
            }
        }
    }

    fun getMyInfo() {
        launch(UI) {
            MallApi.getMyInfo().awaitAndHandle {
                view.notify("get my info failed")
            }?.let {
                infoMine = it
                view.notify("get my info succeed")
            }
        }
    }

    fun getMenu() {
        launch(UI) {
            MallApi.getMenu().awaitAndHandle {
                view.notify("get menu failed")
            }?.let {
                infoMenu = it
                view.notify("get menu succeed")
            }
        }
    }

    fun addSale() {}
    fun addNeed() {}

}