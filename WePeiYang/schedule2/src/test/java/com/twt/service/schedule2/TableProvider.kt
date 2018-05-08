package com.twt.service.schedule2

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.twt.service.schedule2.extensions.termStart
import com.twt.service.schedule2.model.AbsClasstableProvider
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.CommonClassTable
import com.twt.service.schedule2.model.audit.AuditCourse
import com.twt.service.schedule2.model.audit.AuditCourseManager
import com.twt.service.schedule2.model.audit.convertToCourse
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import org.junit.Assert

object TableProvider {
    val gson: Gson = Gson()
    val classtableType = object : TypeToken<CommonBody<Classtable>>() {}.type
//    val auditCourseType = object : TypeToken<CommonBody<JsonObject>>() {}.type
    val dayOfInt = 86400L

    val classtable: CommonBody<Classtable>
    val tjuClassTable: CommonClassTable
    val auditClasstable: AbsClasstableProvider

    init {
        classtable = gson.fromJson(ConstantData.Schedule2, classtableType)
        Assert.assertNotNull(classtable.data)
        tjuClassTable = CommonClassTable(classtable = classtable.data!!)

        val auditClassTableResponse: Body = gson.fromJson(ConstantData.auditCourseJson1,Body::class.java)
        val auditCourseList = auditClassTableResponse.data!!.map { it.convertToCourse() }
        auditClasstable = CommonClassTable(Classtable(courses = auditCourseList,termStart = termStart))

        print("table init ")
    }
}

data class Body(
        val error_code: Int,
        val message: String,
        val data: List<AuditCourse>?
)