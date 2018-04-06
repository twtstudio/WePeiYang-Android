package com.twt.service.schedule2

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.CommonClassTable
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import org.junit.Assert

object TableProvider {
    val gson: Gson = Gson()
    val classtableType = object : TypeToken<CommonBody<Classtable>>() {}.type
    val dayOfInt = 86400L

    val classtable: CommonBody<Classtable>
    val tjuClassTable: CommonClassTable

    init {
        classtable = gson.fromJson(ConstantData.Schedule2, classtableType)
        Assert.assertNotNull(classtable.data)
        tjuClassTable = CommonClassTable(classtable = classtable.data!!)
        print("table init ")
    }
}