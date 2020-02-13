package com.example.studyroom.service

object StudyServiceManager{

    fun getBuildingList() = SelfStudyApi.getBuildingList()

    fun getAvailableRoom(week:Int,day:Int) = SelfStudyApi.getAvaliableRoom(week,day)

    fun getAvailableRoombyCourse(week:Int,day:Int,course:Int) = SelfStudyApi.getAvaliableRoombyClass(week,day,course)

    fun getCollection() = SelfStudyApi.getCollectionList()

}