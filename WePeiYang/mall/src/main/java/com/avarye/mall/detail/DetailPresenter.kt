package com.avarye.mall.detail

import com.avarye.mall.service.MallManager
import com.avarye.mall.service.detailLiveData
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailPresenter(private val view: DetailActivity) {
    lateinit var id: String

    fun getId(id: String) {
        this.id = id
    }


    fun getDetail() {
        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getDetailAsync(id).awaitAndHandle {
                Toasty.error(view, "详情获取失败")

            }?.let {
                detailLiveData.postValue(it)
                /*Type mismatch: inferred type is Detail but Sale! was expected
:mall:compileDebugKotlin FAILED
:app:buildInfoGeneratorCommonDebug*/
            }
        }
    }

    fun getInfo() {
/*        GlobalScope.launch(Dispatchers.Main) {
            MallManager.getSellerInfoAsync(id).awaitAndHandle {
                Toasty.error(view, "详情获取失败")
            }?.let {
                sellerLiveData.postValue(it)

            }
        }*/
    }
/*
    fun goodscollect() {
         GlobalScope.launch(Dispatchers.Main){
             MallManager.favAsync(id).awaitAndHandle {
                 Toasty.error(view,"收藏失败")
                 //收藏的时候要传gid可是没找到emm，大概说的也是商品id？传一下试试
             }?.let {
                 //collectLiveData.postValue(it)
             }
         }
    }*/




}