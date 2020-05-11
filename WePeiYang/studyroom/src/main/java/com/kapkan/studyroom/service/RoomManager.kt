package com.kapkan.studyroom.service

import com.kapkan.studyroom.view.BuildingListActivity
import com.kapkan.studyroom.view.StudyActivity

class RoomManager {
    var availableclassrooms:ArrayList<Classroom> = ArrayList()
    lateinit var classrooms: ArrayList<Classroom>
    var flag:Int = 0
    var week:Int = 0
    var day:Int = 0
    lateinit var courselist: BooleanArray
    //false--Building
    //true -- study
    var activityflag = false
    lateinit var buildingListActivity: BuildingListActivity
    lateinit var studyActivity: StudyActivity

    fun getAvailableRoomList(viewModel: ViewModel,courselist:BooleanArray,day:Int,week:Int,classrooms:ArrayList<Classroom>,buildingListActivity: BuildingListActivity){
        flag = classrooms.size
        this.week = week
        this.day = day
        this.courselist = courselist
        this.buildingListActivity =buildingListActivity
        this.classrooms= classrooms
        classrooms.forEach {
            viewModel.getClassroomWeekInfo(it.classroom_id,week,this)
        }
    }

    fun getAvailableRoomList(viewModel: ViewModel,courselist:BooleanArray,day:Int,week:Int,classrooms:ArrayList<Classroom>,studyActivity: StudyActivity){
        flag = classrooms.size
        this.week = week
        this.day = day
        this.courselist = courselist
        this.studyActivity =studyActivity
        classrooms.forEach {
            viewModel.getClassroomWeekInfo(it.classroom_id,week,this)
        }
    }

    fun refresh(viewModel: ViewModel,courselist:ArrayList<Boolean>,day:Int,week:Int,classrooms:ArrayList<Classroom>){
        flag = 0
        this.week = day
        this.day = week
        courselist.clear()
    }

    fun judgeAvailable(wd: weekdata):Boolean{
        var available:Boolean =true
           available = when{
                day == 0 -> {
                    judge(wd.`1`)
                }
                day == 1 -> {
                    judge(wd.`2`)
                }
                day == 2 -> {
                    judge(wd.`3`)
                }
                day == 3 -> {
                    judge(wd.`4`)
                }
                day == 4 -> {
                    judge(wd.`5`)
                }
               else -> true
            }
        return available
    }

    fun judge(classes:String):Boolean{
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

    fun getWeekInfo(wd: weekdata,id:String){
        if (judgeAvailable(wd)){
            availableclassrooms.add(classrooms.find { it.classroom_id == id }!!)
        }
        flag--
        if (flag==0)finish()
    }

    fun onGetWeekInfoError(id: String){
        flag--
        if (flag==0)finish()
    }

    fun finish(){
        if (activityflag){
            studyActivity.getCollection(availableclassrooms)
        }else{
            buildingListActivity.receiveArooms(availableclassrooms)
        }

    }
}