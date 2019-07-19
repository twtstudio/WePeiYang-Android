package com.twt.service.schedule2.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.twt.service.schedule2.R
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.ui.popup.TopPopupWindow
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.schedule_class_picker_pop.view.*
import org.jetbrains.anko.*

class CustomClassPickerPop(context: Context, resultHandler:ResultHandler) : TopPopupWindow(context,true) {
    val mHandler = resultHandler
    var startTime: Int = 1
    var endTime: Int = 1

    @SuppressLint("SetTextI18n")
    override fun createContentView(parent: ViewGroup?): View {
        val view = context.layoutInflater.inflate(R.layout.schedule_class_picker_pop, parent, false).apply {
            layoutParams = LayoutParams(matchParent, wrapContent).apply {
                gravity = Gravity.BOTTOM
                backgroundColor = Color.WHITE
            }
            val numberPicker1 = findViewById<NumberPicker>(R.id.custom_number_picker1)
            val numberPicker2 = findViewById<NumberPicker>(R.id.custom_number_picker2)
            /**
             * 设置开始和结束时间的 number_picker
             */
            numberPicker1.apply {
                minValue = 1
                maxValue = 12
                value = numberPicker1.value
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                setOnValueChangedListener { numberPicker, oldVal, newVal ->
                    startTime = newVal
                    mHandler.handle(startTime,endTime)
//                    Toasty.info(mContext, "您选择的时间是: ${newVal}").show()
                }
            }

            numberPicker2.apply {
                minValue = numberPicker1.value
                maxValue = 12
                value = numberPicker2.value
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                setOnValueChangedListener { numberPicker, oldVal, newVal ->
                    endTime = newVal
                    mHandler.handle(startTime,endTime)
//                    Toasty.info(mContext, "您选择的时间是: ${newVal}").show()
                }
            }

            pop_title.textColor = getColorCompat(R.color.colorPrimary)
            indicator.textColor = getColorCompat(R.color.colorPrimary)
            class_tip.textColor = getColorCompat(R.color.colorPrimary)
            divider_line.backgroundColor = getColorCompat(R.color.colorPrimary)
        }
        return view
    }

    init {
        isDismissOnClickBack = true
        isDismissOnTouchBackground = true
    }

    interface ResultHandler{
        fun handle(startTime:Int, endTime: Int)
    }
}