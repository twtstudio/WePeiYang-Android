package com.avarye.mall

import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import android.util.Log
import com.avarye.mall.service.*
import com.avarye.mall.view.MallActivity
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Presenter(private var view: MallActivity) {

    //先后台登陆再拿数据
    //这玩意咋滴用LiveData…orz
    fun login() {
        GlobalScope.launch(Dispatchers.Main) {
            MallApi.login(Utils.getToken()).awaitAndHandle {
                Toasty.error(view, "没网？")
            }?.let {
                if (it.error_code == -1) {
                    Utils.setLogin(it.data)
                    Toasty.info(view, "login succeed").show()
                    Log.d("login done", Utils.getToken())
                } else {
                    Toasty.error(view, "token有问题吧").show()
                }
            }

            MallApi.latestGoods(Utils.toReqBody(1)).awaitAndHandle {
                Log.d("login get goods failed", it.message)
            }?.let {
                Utils.addGoods(it)
                view.bindLatest(view, Utils.getGoods())
                Log.d("login goods done", it[0].page.toString())
            }
        }
    }

    fun getLatest(page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallApi.latestGoods(Utils.toReqBody(page)).awaitAndHandle {
                Log.d("get goods failed", it.message)
            }?.let {
                Utils.addGoods(it)
                view.bindLatest(view, Utils.getGoods())
                Log.d("get goods done", it[0].page.toString())
            }
        }
    }


    fun search(key: String, page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallApi.schGoods(Utils.toReqBody(key), Utils.toReqBody(page)).awaitAndHandle {
                Log.d("search failed", "search failed")
            }?.let {
                Utils.addSchGoods(it)
                view.bindSearch(view, Utils.getSchGoods())
                Log.d("search done", "search done")
            }
        }
    }

    fun getMyInfo() {
        GlobalScope.launch(Dispatchers.Main) {
            MallApi.getMyInfo().awaitAndHandle {
                Log.d("get mine failed", "get mine failed")
            }?.let {
                mineLiveData.refresh()
            }
        }
    }

    fun getMenu() {
        GlobalScope.launch(Dispatchers.Main) {
            MallApi.getMenu().awaitAndHandle {
            }?.let {
                menuLiveData.refresh()
            }
        }
    }

    fun addSale() {}
    fun addNeed() {}

}