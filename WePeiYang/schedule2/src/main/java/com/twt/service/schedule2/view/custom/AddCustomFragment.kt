package com.twt.service.schedule2.view.custom

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatCheckBox
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.Arrange
import com.twt.service.schedule2.model.Week
import com.twt.service.schedule2.model.custom.CustomCourseManager
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.schedule_frag_add_custom.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import android.content.Context.INPUT_METHOD_SERVICE
import java.lang.NumberFormatException


class AddCustomFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.schedule_frag_add_custom, container, false)

        val fragContext = activity
        val mContext = view.context

//        val imm: InputMethodManager = mContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        val editCourseName: EditText = view.findViewById(R.id.edit_course_name)
//
//        editCourseName.setOnFocusChangeListener { view, b ->
//            imm.hideSoftInputFromWindow(editCourseName.windowToken,0)
//        }

        /**
         * 设置需要选择数字的 numberpicker 和 popwindow
         */
        val editStartTime: TextView = view.findViewById(R.id.edit_start_time_name)
        val editEndTime:TextView = view.findViewById(R.id.edit_end_time_name)
        val editStartWeek: TextView = view.findViewById(R.id.edit_startWeek_name)
        val editEndWeek: TextView = view.findViewById(R.id.edit_endWeek_name)

        val timePop = CustomClassPickerPop(mContext,object : CustomClassPickerPop.ResultHandler {
            override fun handle(startTime: Int, endTime: Int) {
                editStartTime.text  = startTime.toString()
                editEndTime.text = endTime.toString()
            }
        })

        val weekPop = CustomWeekPickerPop(mContext, object : CustomWeekPickerPop.WeekResultHandler{
            override fun weekhandle(startTime: Int, endTime: Int) {
                editStartWeek.text = startTime.toString()
                editEndWeek.text = endTime.toString()
            }
        })

        editStartTime.setOnClickListener {
            timePop.show()
        }
        editEndTime.setOnClickListener {
            timePop.show()
        }
        editStartWeek.setOnClickListener {
            weekPop.show()
        }
        editEndWeek.setOnClickListener {
            weekPop.show()
        }

        /**
         * 设置选择单双周的复选框
         */
        val oddWeekCheckbox = view.findViewById<AppCompatCheckBox>(R.id.odd_week_checkbox)
        val evenWeekCheckbox = view.findViewById<AppCompatCheckBox>(R.id.even_week_checkbox)
        val bothWeekCheckbox = view.findViewById<AppCompatCheckBox>(R.id.both_week_checkbox)
        val checkBoxList: List<CheckBox> = listOf(oddWeekCheckbox, evenWeekCheckbox, bothWeekCheckbox)
        var str = ""
        checkBoxList.forEach {
            it.apply {
                buttonTintList = ColorStateList.valueOf(getColorCompat(R.color.colorPrimary))
                setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        str = buttonView.text.toString()
                        checkBoxList.forEach { it.isChecked = false }
                        it.isChecked = true
                    }
                }
            }
        }

        /**
         * 设置确定添加的按钮和点击监听
         */
        val button: Button = view.findViewById(R.id.ll_course_arrange)
        try {
            button.apply {
                setBackgroundColor(getColorCompat(R.color.colorPrimary))
                setOnClickListener {
                    val courseName: String = edit_course_name.text.toString()
                    val teacherName = edit_course_teacher.text.toString()
                    val week: String = str
                    val startTime: Int = edit_start_time_name.text.toString().trim().toInt()
                    val endTime: Int = edit_end_time_name.text.toString().trim().toInt()
                    val weekday: Int = edit_weekday_name.text.toString().trim().toInt()
                    val room: String = edit_room_name.text.toString()
                    val startWeek: Int = edit_startWeek_name.text.toString().trim().toInt()
                    val endWeek: Int = edit_endWeek_name.text.toString().trim().toInt()

                    val weekPeriod = Week(startWeek, endWeek)
                    val arrange = Arrange(week, startTime, endTime, weekday, room)

                    Toasty.info(fragContext!!, "自定义事件 ${courseName} 添加成功").show()
                    GlobalScope.async (Dispatchers.Default + QuietCoroutineExceptionHandler) {
                        CustomCourseManager.addCustomCourse(courseName, teacherName, listOf(arrange), weekPeriod)
                    }
                }
            }
        } catch (e: NumberFormatException){
            Toasty.info(fragContext!!, "事件信息不可为空").show()
            e.printStackTrace()
        }

        return view
    }
}