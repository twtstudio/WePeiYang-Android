package com.twt.service.schedule2.view.custom

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatCheckBox
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.customPopUpWindow
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
import java.lang.NumberFormatException

class AddCustomFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.schedule_frag_add_custom, container, false)

        val fragContext = activity

        lateinit var courseName: String
        lateinit var teacherName: String
        lateinit var week: String
        var startTime = 0
        var endTime = 0
        var weekday = 0
        lateinit var room: String
        var startWeek = 0
        var endWeek = 0
        lateinit var weekPeriod: Week
        lateinit var arrange: Arrange

        /**
         * 隐藏软键盘
         */
        fun hideSoftInput() =
                (fragContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(fragContext.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        /**
         * 设置需要选择数字的 numberpicker 和 popwindow
         */
        val editStartTime: TextView = view.findViewById(R.id.edit_start_time_name)
        val editEndTime: TextView = view.findViewById(R.id.edit_end_time_name)
        val editStartWeek: TextView = view.findViewById(R.id.edit_startWeek_name)
        val editEndWeek: TextView = view.findViewById(R.id.edit_endWeek_name)

        editStartTime.setOnClickListener {
            hideSoftInput()
            editStartTime.text = "1"
            customPopUpWindow(context!!, "请选择节数", "节", 12, editStartTime, editEndTime)
        }

        editEndTime.setOnClickListener {
            hideSoftInput()
            customPopUpWindow(context!!, "请选择节数", "节", 12, editStartTime, editEndTime)
        }
        editStartWeek.setOnClickListener {
            hideSoftInput()
            editStartWeek.text = "1"
            customPopUpWindow(context!!, "请选择周数", "周", 25, editStartWeek, editEndWeek)
        }
        editEndWeek.setOnClickListener {
            hideSoftInput()
            customPopUpWindow(context!!, "请选择周数", "周", 25, editStartWeek, editEndWeek)
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
                    hideSoftInput()
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
            button.setBackgroundColor(getColorCompat(R.color.colorPrimary))
            button.setOnClickListener {
                courseName = edit_course_name.text.toString()
                teacherName = edit_course_teacher.text.toString()
                week = str
                room = edit_room_name.text.toString()
                when {
                    edit_start_time_name.text.toString().isNotEmpty()
                            &&edit_end_time_name.text.toString().isNotEmpty()
                            &&edit_weekday_name.text.toString().isNotEmpty()
                            &&edit_startWeek_name.text.toString().isNotEmpty()
                            &&edit_endWeek_name.text.toString().isNotEmpty() ->
                    {
                        startTime = edit_start_time_name.text.toString().trim().toInt()
                        endTime = edit_end_time_name.text.toString().trim().toInt()
                        weekday = edit_weekday_name.text.toString().trim().toInt()
                        startWeek = edit_startWeek_name.text.toString().trim().toInt()
                        endWeek = edit_endWeek_name.text.toString().trim().toInt()
                    }
                    else -> Toasty.error(fragContext!!, "添加失败，请补全自定义的时间信息").show()
                }
                weekPeriod = Week(startWeek, endWeek)
                arrange = Arrange(week, startTime, endTime, weekday, room)

                when {
                    courseName.isEmpty() -> Toasty.error(fragContext!!, "请输入事件名称").show()
                    teacherName.isEmpty() -> Toasty.error(fragContext!!, "请输入当事人名称").show()
                    room.isEmpty() -> Toasty.error(fragContext!!, "请输入地点").show()
                    week.isEmpty() -> Toasty.error(fragContext!!, "请选择单双周").show()
                    startTime >= endTime || startWeek >= endWeek || weekday > 7 -> Toasty.error(fragContext!!, "添加失败，请检查自定义的时间信息").show()
                    weekday <= 0 -> Toasty.error(fragContext!!, "添加失败，请检查自定义的时间信息").show()
                    else -> {
                        Toasty.info(fragContext!!, "自定义事件 ${courseName} 添加成功").show()
                        GlobalScope.async(Dispatchers.Default + QuietCoroutineExceptionHandler) {
                            CustomCourseManager.addCustomCourse(courseName, teacherName, listOf(arrange), weekPeriod)
                        }
                    }
                }
            }
        } catch (e: NumberFormatException) {
            Toasty.info(fragContext!!, "事件信息不可为空").show()
            e.printStackTrace()
        }
        return view
    }
}