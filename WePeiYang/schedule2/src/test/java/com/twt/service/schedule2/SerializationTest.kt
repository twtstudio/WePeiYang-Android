package com.twt.service.schedule2

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twt.service.schedule2.model.Classtable
import com.twt.wepeiyang.commons.experimental.network.CommonBody

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import java.lang.reflect.Type

import org.hamcrest.Matchers.*
import org.hamcrest.CoreMatchers.*
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.hamcrest.MatcherAssert.assertThat
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(Parameterized::class)
class SerializationTest(val jsonString: String) {

    private lateinit var gson: Gson
    val classtableType = object : TypeToken<CommonBody<Classtable>>() {}.type

    @Before
    fun init() {
        gson = Gson()
    }

    @Test
    fun testSerial() {
        val classtable: CommonBody<Classtable> = gson.fromJson(jsonString, classtableType)
        assertNotNull(classtable.data)
        val data = classtable.data!!
        assertNotNull(data.term)
        assertNotNull(data.courses)
        val courses = data.courses
        assertThat(courses.size, greaterThan(0))
        assertNotNull(courses[0].arrangeBackup)
        courses[0].arrange.addAll(courses[0].arrangeBackup)
        assertNotNull(courses[0].coursename)
        assertNotNull(courses[0].arrange)
        assertNotNull(courses[0].arrange[0])
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun testWithManyScheduleData(): Collection<Any> {
            return listOf(ConstantData.Schedule1, ConstantData.Schedule2)
        }
    }

}