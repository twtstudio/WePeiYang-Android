package com.twt.service.schedule2.view.custom

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.Arrange
import com.twt.service.schedule2.model.Week
import com.twt.service.schedule2.model.custom.CustomCourseManager
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.android.synthetic.main.schedule_frag_add_custom.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class AddCustomFragment: Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.schedule_frag_add_custom,container,false)

        val button:Button = view.findViewById(R.id.ll_course_arrange)
        button.setOnClickListener {
            var week: String = edit_week_name.text.toString()
            var startTime: Int = edit_start_time_name.text.toString().trim().toInt()
            var endTime: Int = edit_end_time_name.text.toString().trim().toInt()
            var weekday: Int = edit_weekday_name.text.toString().trim().toInt()
            var room: String = edit_room_name.text.toString()
            var startWeek: Int = edit_startWeek_name.text.toString().trim().toInt()
            var endWeek: Int = edit_endWeek_name.text.toString().trim().toInt()

            var courseName: String = edit_course_name.text.toString()
            var teacherName = edit_course_teacher.text.toString()
            var weekPeriod = Week(startWeek,endWeek)
            var arrange = Arrange (week,startTime,endTime,weekday,room)

            async(CommonPool + QuietCoroutineExceptionHandler) {
                CustomCourseManager.addCustomCourse(courseName,teacherName, listOf(arrange),weekPeriod)
                Toast.makeText(view.context, "自定义事件：${courseName}添加成功", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

}