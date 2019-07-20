package com.avarye.mall.service

import android.util.Log
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewModel {

    //先后台登陆再拿数据
    fun login() {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.login().awaitAndHandle {
                Toasty.error(CommonContext.application, "login failed").show()
            }?.let {
                if (it.error_code == -1) {
                    MallManager.setLogin(it.data!!)
                    Toasty.info(CommonContext.application, "login succeed").show()
                    Log.d("login done", MallManager.getToken())
                } else {
                    Toasty.error(CommonContext.application, "token有问题吧").show()
                }
            }

            MallManager.latestSale(1).awaitAndHandle {
                Log.d("login get goods failed", it.message)
            }?.let {
                saleLiveData.postValue(it)
                Toasty.info(CommonContext.application, "init succeed").show()
                Log.d("login goods done", it[0].page.toString())
            }
        }
    }

    fun getLatestSale(page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.latestSale(page).awaitAndHandle {
                Log.d("get goods failed", it.message)
            }?.let {
                saleLiveData.postValue(it)
                Log.d("get goods done", it[0].page.toString())
            }
        }
    }

    fun getLatestNeed(page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.latestNeed(page).awaitAndHandle {
                Log.d("get goods failed", it.message)
            }?.let {
                Toasty.info(CommonContext.application, "need succeed").show()
                needLiveData.postValue(it)
                Log.d("get goods done", it[0].page.toString())
            }
        }
    }


    fun search(key: String, page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.search(key, page).awaitAndHandle {
                Log.d("search failed", "search failed")
            }?.let {
//                Toasty.info(CommonContext.application, "search succeed").show()
                searchLiveData.postValue(it)
                Log.d("search done", "search done")
            }
        }
    }

    fun getMyInfo() {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getMyInfo().awaitAndHandle {
                Log.d("get mine failed", "get mine failed")
            }?.let {
                mineLiveData.refresh()
            }
        }
    }

    fun getMenu() {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getMenu().awaitAndHandle {
            }?.let {
                menuLiveData.refresh()
            }
        }
    }

    fun addSale() {}
    fun addNeed() {}

}
