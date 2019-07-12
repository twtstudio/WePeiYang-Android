package com.avarye.mall

import android.util.Log
import com.avarye.mall.service.MallManager
import com.avarye.mall.service.menuLiveData
import com.avarye.mall.service.mineLiveData
import com.avarye.mall.view.MallActivity
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainPresenter(private var view: MallActivity) {

    //先后台登陆再拿数据
    //这玩意咋滴用LiveData…orz
    fun login() {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.login().awaitAndHandle {
                Toasty.error(view, "没网？")
            }?.let {
                if (it.error_code == -1) {
                    MallManager.setLogin(it.data!!)
                    Toasty.info(view, "login succeed").show()
                    Log.d("login done", MallManager.getToken())
                } else {
                    Toasty.error(view, "token有问题吧").show()
                }
            }

            MallManager.latestSale(1).awaitAndHandle {
                Log.d("login get goods failed", it.message)
            }?.let {
                MallManager.addGoods(it)
                view.bindSale(MallManager.getGoods())
                Toasty.info(view, "init succeed").show()
                Log.d("login goods done", it[0].page.toString())
            }
        }
    }

    fun getLatestSale(page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.latestSale(page).awaitAndHandle {
                Log.d("get goods failed", it.message)
            }?.let {
                MallManager.addGoods(it)
                view.bindSale(MallManager.getGoods())
                Log.d("get goods done", it[0].page.toString())
            }
        }
    }

    fun getLatestNeed(page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.latestNeed(page).awaitAndHandle {
                Log.d("get goods failed", it.message)
            }?.let {
                MallManager.addNeed(it)
                view.bindNeed(MallManager.getNeed())
                Log.d("get goods done", it[0].page.toString())
            }
        }
    }


    fun search(key: String, page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.search(key, page).awaitAndHandle {
                Log.d("search failed", "search failed")
            }?.let {
                MallManager.addGoods(it)
                view.bindSale(MallManager.getGoods())
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
