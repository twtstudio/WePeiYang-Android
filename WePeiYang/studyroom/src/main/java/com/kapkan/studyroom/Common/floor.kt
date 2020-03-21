package com.kapkan.studyroom.Common

import com.kapkan.studyroom.service.Data

class Floor(floor:String,classrooms:List<Data>,aclassrooms:List<Data>){
    val floor:String = floor
    var classrooms:List<Data> = classrooms
    var aclassrooms = aclassrooms
}