package com.kapkan.studyroom.service

import com.kapkan.studyroom.items.CollectionItem
import com.kapkan.studyroom.items.Flooritem
import com.kapkan.studyroom.view.StudyActivity
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewModel {

    fun getCollections(studyActivity: StudyActivity){
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.getCollection().awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1){
                    collectionLiveData.postValue(it)
                    //notify
                    studyActivity.checkCollection()
                }else{
                    Toasty.info(CommonContext.application, "没登陆吗？").show()
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
                    //notify
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
                    getCollections()
                }else{
                    Toasty.info(CommonContext.application, it.message).show()
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
                    getCollections()
                }else{
                    Toasty.info(CommonContext.application, it.message).show()
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
            StudyServiceManager.getAvailableRoom(week,day).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1) {
                    AvailableRoomListData.postValue(it)
                } else Toasty.error(CommonContext.application, it.message).show()
            }
        }
    }

    fun getClassroomWeekInfo(classroomID:String,week: Int,item:Flooritem){
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.getClassWeekInfo(classroomID,week).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1) {
                  //  classroomWeekInfoLiveData.postValue(it)
                    item.getWeekInfo(it.data)
                } else Toasty.error(CommonContext.application, it.message).show()
            }
        }
    }



    fun getClassroomWeekInfo(classroomID:String,week: Int,item:CollectionItem){
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.getClassWeekInfo(classroomID,week).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1) {
                    //  classroomWeekInfoLiveData.postValue(it)
                    item.getWeekInfo(it.data)
                } else Toasty.error(CommonContext.application, it.message).show()
            }
        }
    }

    fun getClassroomWeekInfo(classroomID:String,week: Int,item:RoomManager){
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.getClassWeekInfo(classroomID,week).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1) {
                    //  classroomWeekInfoLiveData.postValue(it)
                    item.getWeekInfo(it.data,classroomID)
                } else {
                    Toasty.error(CommonContext.application, it.message).show()
                    item.onGetWeekInfoError(classroomID)
                }
            }
        }
    }
}