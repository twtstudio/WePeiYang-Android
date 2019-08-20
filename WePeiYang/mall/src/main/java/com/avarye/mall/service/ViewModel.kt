package com.avarye.mall.service

import android.util.Log
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class ViewModel {

    fun login() {
        //先后台登陆再拿数据
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.login().awaitAndHandle {
                Toasty.error(CommonContext.application, "登陆失败").show()
            }?.let {
                if (it.error_code == -1) {
                    MallManager.setLogin(it.data!!)
                } else {
                    Toasty.error(CommonContext.application, it.message).show()
                }
            }

            MallManager.latestSale(1).awaitAndHandle {
                Log.d("login get goods failed", it.message)
            }?.let {
                saleLiveData.postValue(it)
            }
        }
    }

    fun getLatestSale(page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.latestSale(page).awaitAndHandle {
                Log.d("get goods failed", it.message)
            }?.let {
                saleLiveData.postValue(it)
            }
        }
    }

    fun getLatestNeed(page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.latestNeed(page).awaitAndHandle {
                Log.d("get goods failed", it.message)
            }?.let {
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
                if (it[0].page == 0) {
                    Toasty.info(CommonContext.application, "搜索结果为空TvT").show()
                }
                searchLiveData.postValue(it)
                Log.d("search done", "search done")
            }
        }
    }

    /*fun getMyInfo() {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getMyInfo().awaitAndHandle {
                Log.d("get mine failed", "get mine failed")
            }?.let {
                mineLiveData.refresh()//emmmm不对orz
            }
        }
    }*/

    /*fun getMenu() {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getMenu().awaitAndHandle {
            }?.let {
                menuLiveData.refresh()
            }
        }
    }*/

    fun postImg(file: File) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.postImg(file).awaitAndHandle {
                Toasty.error(CommonContext.application, "图片上传失败").show()
            }?.let {
                if (it.result_code == "02001") {
                    imgIdLiveData.postValue(it.id)
                    Toasty.info(CommonContext.application, it.msg)
                } else {
                    Toasty.info(CommonContext.application, it.msg)
                }
            }
        }
    }

    fun postSale(map: Map<String, Any>) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.postSale(map).awaitAndHandle {
                Toasty.error(CommonContext.application, "上传失败").show()
            }?.let {
                //TODO：跳转到详情界面
                Toasty.info(CommonContext.application, it.msg).show()
            }
        }
    }

    fun postNeed(map: Map<String, Any>) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.postNeed(map).awaitAndHandle {
                Toasty.error(CommonContext.application, "上传失败").show()
            }?.let {
                //TODO：跳转到详情界面
                Toasty.info(CommonContext.application, it.msg).show()
            }
        }

    }

}
