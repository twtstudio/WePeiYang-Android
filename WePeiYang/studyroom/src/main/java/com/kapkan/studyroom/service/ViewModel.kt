package com.kapkan.studyroom.service

import com.kapkan.studyroom.view.StudyActivity
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewModel {

    fun getTermDate(studyActivity: StudyActivity){
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.getDate().awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1){
                    studyActivity.gotDate(it.data!!.termStart)
                }else{
                    Toasty.info(CommonContext.application, "或许开学时间失败？？？？").show()
                }
            }
        }

    }

    fun login(){
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.login().awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1){
                    loginLiveData.postValue(it.data)
                }else{
                    Toasty.info(CommonContext.application, "哎？登陆失败？").show()
                }
            }
        }
    }

    fun getCollections(){
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.getCollection().awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1){
                    collectionLiveData.postValue(it)
                }else{
                    Toasty.info(CommonContext.application, "没登陆吗？").show()
                }
            }
        }
    }

    fun star(roomId:String){
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.star(roomId).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1){
                    Toasty.info(CommonContext.application, "收藏成功").show()
                }else{
                    Toasty.info(CommonContext.application, "哎？登陆失败？").show()
                }
            }
        }
    }

    fun unStar(roomId:String){
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.unStar(roomId).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1){
                    Toasty.info(CommonContext.application, "已取消收藏").show()
                }else{
                    Toasty.info(CommonContext.application, "哎？登陆失败？").show()
                }
            }
        }
    }

    fun getBuildingList() {
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.getBuildingList().awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1) {
                    BuildingListData.postValue(it)
                } else Toasty.error(CommonContext.application, it.message).show()
            }
        }
    }

    fun getAvailableRoom(day:Int,week:Int){
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.getAvailableRoom(day,week).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1) {
                    AvailableRoomListData.postValue(it)
                } else Toasty.error(CommonContext.application, it.message).show()
            }
        }
    }
}