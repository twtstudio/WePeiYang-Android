package com.avarye.mall.service

import android.util.Log
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class ViewModel {

    fun login() {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.loginAsync().awaitAndHandle {
                Toasty.error(CommonContext.application, "登陆失败").show()
            }?.let {
                if (it.error_code == -1) {
                    loginLiveData.postValue(it.data)
                    Toasty.info(CommonContext.application, it.message).show()
                } else {
                    Toasty.error(CommonContext.application, it.message).show()
                }
            }

            mineLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
        }
    }

    fun init() {
        //先后台登陆再拿数据
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.loginAsync().awaitAndHandle {
                Toasty.error(CommonContext.application, "登陆失败").show()
            }?.let {
                if (it.error_code == -1) {
                    loginLiveData.postValue(it.data)
                } else {
                    Toasty.error(CommonContext.application, it.message).show()
                }
            }

            MallManager.latestSaleAsync(1).awaitAndHandle {
                Log.d("login get goods failed", it.message)
            }?.let {
                saleLiveData.postValue(it)
                Toasty.success(CommonContext.application, "done").show()
            }
        }
    }

    fun getLatestSale(page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.latestSaleAsync(page).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                saleLiveData.postValue(it)
            }
        }
    }

    fun getLatestNeed(page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.latestNeedAsync(page).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                needLiveData.postValue(it)
                Log.d("get goods done", it[0].page.toString())
            }
        }
    }

    fun search(key: String, page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.searchAsync(key, page).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                searchLiveData.postValue(it)
            }
        }
    }

    fun getSelect(category: String, which: Int, page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.selectAsync(category, which, page).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                selectLiveData.postValue(it)
            }
        }

    }


    fun postImg(file: File, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.postImgAsync(file, token).awaitAndHandle {
                Toasty.error(CommonContext.application, "图片上传失败").show()
            }?.let {
                if (it.result_code == "00201") {
                    imgIdLiveData.postValue(it.id)
                    Toasty.info(CommonContext.application, it.id.toString()).show()
                } else {
                    Toasty.info(CommonContext.application, it.msg).show()
                }
            }
        }
    }

    fun postSale(map: Map<String, Any>, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.postSaleAsync(map, token).awaitAndHandle {
                Toasty.error(CommonContext.application, "上传失败").show()
            }?.let {
                //TODO：跳转到详情界面
                Toasty.info(CommonContext.application, it.msg).show()
            }
        }
    }

    fun postNeed(map: Map<String, Any>, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.postNeedAsync(map, token).awaitAndHandle {
                Toasty.error(CommonContext.application, "上传失败").show()
            }?.let {
                //TODO：跳转到详情界面
                Toasty.info(CommonContext.application, it.msg).show()
            }
        }

    }

    fun getMyList(uid: String, which: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getUserInfoAsync(uid).awaitAndHandle {
                Toasty.info(CommonContext.application, it.toString()).show()
            }?.let {
                when (which) {
                    MallManager.W_SALE -> myListLiveData.value = it.goods_list
                    MallManager.W_NEED -> myListLiveData.value = it.needs_list
                }
                Toasty.success(CommonContext.application, "加载成功").show()
            }
        }
    }

    fun getFavList(token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getFavListAsync(token).awaitAndHandle {
                Toasty.info(CommonContext.application, "fav: ${it.message.toString()}").show()
            }?.let {
                myFavLiveData.postValue(it)
            }
        }
    }

    fun deleteSale(gid: String, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.deleteSaleAsync(gid, token).awaitAndHandle {
            }?.let {
                Toasty.info(CommonContext.application, it.msg).show()
            }
        }
    }

    fun deFav(gid: String, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.deFavAsync(gid, token).awaitAndHandle {
            }?.let {
                Toasty.info(CommonContext.application, it.msg).show()
            }
        }
    }

    fun changeMyInfo(phone: String, email: String, qq: String, campus: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.changeMyInfoAsync(phone, email, qq, campus).awaitAndHandle {
            }?.let {
                Toasty.info(CommonContext.application, it.msg).show()
            }
            mineLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
        }
    }

}
