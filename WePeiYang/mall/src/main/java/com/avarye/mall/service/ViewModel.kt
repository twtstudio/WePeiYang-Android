package com.avarye.mall.service

import android.content.Intent
import android.util.Log
import android.view.View
import com.avarye.mall.detail.DetailActivity
import com.avarye.mall.post.PostActivity
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_activity_detail.*
import kotlinx.android.synthetic.main.mall_activity_post.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 所有view的viewModel
 */
class ViewModel {

    fun login() {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.loginAsync().awaitAndHandle {
                Toasty.error(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1) {
                    loginLiveData.postValue(it.data)
                    mineLiveData.refresh(CacheIndicator.REMOTE, callback = { state ->
                        when (state) {
                            is RefreshState.Success -> Toasty.success(CommonContext.application, "已刷新").show()
                            is RefreshState.Failure -> Toasty.error(CommonContext.application, "刷新失败").show()
                        }
                    })
                } else {
                    Toasty.error(CommonContext.application, it.message + "不星 怕要崩").show()
                }
            }

        }
    }

    /**
     * 初始化商城模块时调用
     * 先后台登陆再获得最新发布数据
     */
    fun init() {
        GlobalScope.launch(Dispatchers.Main) {
            Log.d("token!!", MallManager.getToken())//测试用

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
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                saleLiveData.postValue(it)
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
            }
        }
    }

    fun search(key: String, page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.searchAsync(key, page).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                selectLiveData.postValue(it)
            }
        }
    }

    /**
     * 通过分类菜单获得筛选数据
     */
    fun getSelect(category: String, which: Int, page: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.selectAsync(category, which, page).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                selectLiveData.postValue(it)
            }
        }

    }

    fun getDetail(id: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getDetailAsync(id).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                detailLiveData.postValue(it)
            }
        }
    }

    fun getSellerSale(id: String, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getSellerSaleAsync(gid = id, token = token).awaitAndHandle {
                Toasty.info(CommonContext.application, "没拿到用户信息T-T哭了\n${it.message}").show()
            }?.let {
                sellerLiveData.postValue(it)
            }
        }
    }

    fun getSellerNeed(id: String, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getSellerNeedAsync(nid = id, token = token).awaitAndHandle {
                Toasty.info(CommonContext.application, "没拿到用户信息T-T哭了\n${it.message}").show()
            }?.let {
                sellerLiveData.postValue(it)
            }
        }
    }

    /*fun postImg(file: File, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.postImgAsync(file, token).awaitAndHandle {
                Toasty.error(CommonContext.application, "图片上传失败").show()
            }?.let {
                if (it.result_code == "00201") {
//                    imgIdLiveData.postValue(it.id)
                    Toasty.info(CommonContext.application, it.id.toString()).show()
                } else {
                    Toasty.info(CommonContext.application, it.msg).show()
                }
            }
        }
    }*/

    fun postSale(map: Map<String, Any>, token: String, activity: PostActivity) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.postSaleAsync(map, token).awaitAndHandle {
                Toasty.error(CommonContext.application, "上传失败").show()
            }?.let {
                if (it.result_code == "00201") {
                    val intent = Intent(activity, DetailActivity::class.java)
                            .putExtra(MallManager.ID, it.id)
                            .putExtra(MallManager.TYPE, MallManager.SALE)
                            .putExtra("flag", true)
                    activity.startActivity(intent)
                } else {
                    Toasty.info(activity, it.msg).show()
                    activity.pb_post.visibility = View.INVISIBLE
                    activity.tv_post_button.isClickable = true
                }
            }
        }
    }

    fun postNeed(map: Map<String, Any>, token: String, uid: String, activity: PostActivity) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.postNeedAsync(map, token).awaitAndHandle {
                Toasty.error(CommonContext.application, "上传失败").show()
            }?.let {
                if (it.result_code == "00201") {
                    //需求没detail接口 可恶啊 我回头再要
                    MallManager.getUserInfoAsync(uid).awaitAndHandle {
                    }?.let { user ->
                        detailLiveData.postValue(user.needs_list!![0])
                    }
                    val intent = Intent(activity, DetailActivity::class.java)
                            .putExtra(MallManager.ID, it.id)
                            .putExtra(MallManager.TYPE, MallManager.NEED)
                            .putExtra("flag", true)
                    activity.startActivity(intent)
                } else {
                    Toasty.info(activity, it.msg).show()
                    activity.pb_post.visibility = View.INVISIBLE
                    activity.tv_post_button.isClickable = true
                }
            }
        }

    }

    fun fav(id: String, token: String, activity: DetailActivity) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.favAsync(id, token).awaitAndHandle {
                Toasty.error(CommonContext.application, it.message.toString()).show()
            }?.let {
                when (it.result_code) {
                    "00021" -> {
                        Toasty.success(CommonContext.application, it.msg).show()
                        activity.iv_detail_faved.visibility = View.VISIBLE
                        activity.iv_detail_fav.visibility = View.GONE
                    }
                    else -> Toasty.info(CommonContext.application, it.msg).show()
                }
            }
        }
    }

    /**
     * 获得我的发布列表、我的需求列表
     */
    fun getMyList(id: String, which: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getUserInfoAsync(id).awaitAndHandle {
                Toasty.info(CommonContext.application, it.toString()).show()
            }?.let {
                when (which) {
                    MallManager.W_SALE -> myListLiveData.postValue(it.goods_list)
                    MallManager.W_NEED -> myListLiveData.postValue(it.needs_list)
                }
            }
        }
    }

    /**
     * 获得我的收藏列表
     */
    fun getFavList(token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getFavListAsync(token).awaitAndHandle {
            }?.let {
                myListLiveData.postValue(it)
            }
        }
    }

    /**
     * 删除发布商品
     */
    fun deleteSale(gid: String, token: String, uid: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.deleteSaleAsync(gid, token).awaitAndHandle {
            }?.let {
                when (it.result_code) {
                    "01003" -> {
                        Toasty.success(CommonContext.application, it.msg).show()
                        getMyList(uid, MallManager.W_SALE)//重新拿一遍数据
                    }
                    else -> Toasty.info(CommonContext.application, it.msg).show()
                }
            }
        }
    }

    /**
     * 删除发布需求
     */
    fun deleteNeed(nid: String, token: String, uid: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.deleteNeedAsync(nid, token).awaitAndHandle {
            }?.let {
                when (it.result_code) {
                    "01003" -> {
                        Toasty.success(CommonContext.application, it.msg).show()
                        getMyList(uid, MallManager.W_NEED)//重新拿一遍数据
                    }
                    else -> Toasty.info(CommonContext.application, it.msg).show()
                }
            }
        }
    }

    /**
     * ListActivity里的取消收藏
     */
    fun deFav(gid: String, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.deFavAsync(gid, token).awaitAndHandle {
            }?.let {
                when (it.result_code) {
                    "00021" -> {
                        Toasty.success(CommonContext.application, it.msg).show()
                        getFavList(token)//重新拿一遍数据
                    }
                    else -> Toasty.info(CommonContext.application, it.msg).show()
                }
            }
        }
    }

    /**
     * DetailActivity里的取消收藏
     */
    fun deFav(gid: String, token: String, activity: DetailActivity) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.deFavAsync(gid, token).awaitAndHandle {
            }?.let {
                when (it.result_code) {
                    "00021" -> {
                        Toasty.success(CommonContext.application, it.msg).show()
                        activity.iv_detail_faved.visibility = View.GONE
                        activity.iv_detail_fav.visibility = View.VISIBLE
                    }
                    else -> Toasty.info(CommonContext.application, it.msg).show()
                }
            }
        }
    }

    /**
     * 修改个人信息
     */
    fun changeMyInfo(phone: String, email: String, qq: String, campus: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.changeMyInfoAsync(phone, email, qq, campus).awaitAndHandle {
            }?.let {
                when (it.result_code) {
                    "50001" -> Toasty.success(CommonContext.application, it.msg).show()
                    "50002" -> Toasty.info(CommonContext.application, it.msg + "\n是不是啥都没改啊老哥").show()
                    else -> Toasty.info(CommonContext.application, it.msg).show()
                }
                mineLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
            }
        }

    }
}
