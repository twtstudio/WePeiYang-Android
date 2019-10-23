package com.twt.service.schedule2.model.custom

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.persistence.room.*
import com.twt.service.schedule2.model.*
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.cache.RefreshableLiveData
import kotlinx.coroutines.*;

@Entity(primaryKeys = ["name", "teacher"], tableName = "table_custom_course")
data class CustomCourse(
        val name: String,
        val teacher: String,
        val ext: String,/*自定义课程的额外信息*/
        val arrange: List<Arrange>,
        val week: Week
)

@Dao
interface CustomCourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourses(vararg courses: CustomCourse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourse(course: CustomCourse)

    /**
     * @return Although usually not necessary, you can have this method return an int value instead, indicating the number of rows removed from the database.
     */
    @Delete
    fun deleteCourses(vararg course: CustomCourse): Int

    @Query("SELECT * FROM table_custom_course")
    fun loadAllCourses(): List<CustomCourse>

    @Query("SELECT * FROM table_custom_course")
    fun loadAllCustomCoursesLiveData(): LiveData<List<CustomCourse>>

}

val customCourseLiveData = object :RefreshableLiveData<List<CustomCourse>,CacheIndicator>() {
    override fun observe(owner: LifecycleOwner, observer: Observer<List<CustomCourse>>) {
        super.observe(owner, observer)
        CustomCourseManager.getAllCustomCoureseLiveData().observe(owner,observer)
    }

    override fun refresh(vararg indicators: CacheIndicator, callback: suspend (RefreshState<CacheIndicator>) -> Unit) {
        if (indicators == CacheIndicator.REMOTE) {
           GlobalScope.async(Dispatchers.Default) {
                try {
                    CustomCourseManager.refreshCustomClasstable()
                    callback(RefreshState.Success(CacheIndicator.REMOTE))
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(RefreshState.Failure(e))
                }
            }
        }
    }

    override fun onActive() {
        refresh(CacheIndicator.REMOTE)
    }

    override fun cancel() {
        // no need to impl
    }

}



