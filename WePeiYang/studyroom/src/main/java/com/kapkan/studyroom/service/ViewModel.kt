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

    fun getBuildingList(studyActivity: StudyActivity) {
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.getBuildingList().awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1) {

                    BuildingListData.postValue(it)
                    studyActivity.buildingaryw.clear()
                    studyActivity.wId.clear()
                    studyActivity.buildingaryp.clear()
                    studyActivity.pId.clear()
                    it.data.forEach {
                        if (it.campus_id=="1") {
                            studyActivity.buildingaryw.add(it.building)
                            studyActivity.wId.add(it.building_id)
                        }else{
                            studyActivity.buildingaryp.add(it.building)
                            studyActivity.pId.add(it.building_id)
                        }
                    }
                    studyActivity.initBuilding()
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

    fun checkCollectionavaliable(classroomID:String,day: Int,week: Int,item:CollectionItem,courselist:BooleanArray){
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.getClassWeekInfo(classroomID,week).awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1) {
                    //  classroomWeekInfoLiveData.postValue(it)
                    item.onAvaliableChanged(judgeAvailable(it.data,day,courselist))
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


    private fun judgeAvailable(wd: weekdata, day: Int, courselist:BooleanArray):Boolean{
        return when (day) {
            1 -> {
                judge(wd.`1`,courselist)
            }
            2 -> {
                judge(wd.`2`,courselist)
            }
            3 -> {
                judge(wd.`3`,courselist)
            }
            4 -> {
                judge(wd.`4`,courselist)
            }
            5 -> {
                judge(wd.`5`,courselist)
            }
            else -> true
        }
    }

    private fun judge(classes:String, courselist:BooleanArray):Boolean{
        for (j in 0..5){
            //选了这段时间，判断是否可用
            if (courselist[j]){
                if (classes.substring(2*j,2*j+1) == "1"||(classes.substring(2*j+1,2*j+2)=="1")){
                    return false
                }
            }
        }
        return true
    }


}