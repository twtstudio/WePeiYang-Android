package com.twt.service.schedule2.view.custom

import android.graphics.Color
import android.support.constraint.ConstraintLayout.LayoutParams.PARENT_ID
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.getChineseCharacter
import com.twt.service.schedule2.model.custom.CustomCourse
import com.twt.service.schedule2.model.custom.CustomCourseManager
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout

class SingleTextItem(val text: String) : Item {

    var builder: (TextView.() -> Unit)? = null

    var onClick: ((View) -> Unit)? = null

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            lateinit var textView: TextView
            val view = parent.context.constraintLayout {
                textView = textView {
                    text = "课程表设置"
                    id = View.generateViewId()
                    textSize = 16f
                    textColor = Color.BLACK
                }.lparams(width = wrapContent, height = wrapContent) {
                    startToStart = PARENT_ID
                    topToTop = PARENT_ID
                    bottomToBottom = PARENT_ID
                    margin = dip(16)
                }
                imageView {
                    backgroundColor = Color.parseColor("#F5F5F5")
                }.lparams(width = matchParent, height = dip(1)) {
                    bottomToBottom = PARENT_ID
                }
                backgroundColor = Color.WHITE
            }.apply {
                layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
            }
            return ViewHolder(view, textView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as SingleTextItem
            holder as ViewHolder
            holder.textView.text = item.text
            item.builder?.invoke(holder.textView)
            item.onClick?.invoke(holder.itemView)
        }

        private class ViewHolder(itemView: View, val textView: TextView) : RecyclerView.ViewHolder(itemView)

    }

    override val controller: ItemController = Controller

    override fun areItemsTheSame(newItem: Item): Boolean = areContentsTheSame(newItem)

    override fun areContentsTheSame(newItem: Item): Boolean {
        if (newItem !is SingleTextItem) return false
        return newItem.text == this.text
    }
}

class CustomCourseItem(customCourse: CustomCourse) : Item {

    val customCourse: CustomCourse = customCourse
    var clickBlock: ((View) -> Unit)? = null

    override val controller: ItemController
        get() = CustomCourseItem

    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as CustomItemViewHolder
            item as CustomCourseItem
            holder.apply {
                name.text = item.customCourse.name
                teacher.text = item.customCourse.teacher
                room.text = item.customCourse.arrange[0].room
                week.text = item.customCourse.arrange[0].week
                startTime.text = item.customCourse.arrange[0].start.toString()
                endTime.text = item.customCourse.arrange[0].end.toString()
                if (item.customCourse.arrange[0].day in 0..7){
                    weekDay.text = getChineseCharacter(item.customCourse.arrange[0].day)
                } else {
                    weekDay.text = "？？？"
                }
                cardView.setCardBackgroundColor(getColorCompat(R.color.colorPrimary))
            }

            holder.itemView.setOnClickListener {
                holder.itemView.context.alert {
                    title = "删除自定义事件"
                    message = "是否删除该自定义事件（所有时段）：${item.customCourse.name}"
                    positiveButton("删除自定义事件") {
                        GlobalScope.launch(Dispatchers.Default + QuietCoroutineExceptionHandler) {
                            CustomCourseManager.deleteCustomCourse(item.customCourse)
                            Toasty.info(holder.itemView.context.applicationContext, "删除成功").show()
                        }
                    }
                }.show()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.schedule_item_my_custom, parent, false)

            return CustomItemViewHolder(view)
        }

    }

    private class CustomItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.custom_course_name)
        val teacher = itemView.findViewById<TextView>(R.id.custom_course_teacher)
        val room = itemView.findViewById<TextView>(R.id.custom_course_room)
        val week = itemView.findViewById<TextView>(R.id.custom_course_week)
        val startTime = itemView.findViewById<TextView>(R.id.custom_course_start_time)
        val endTime = itemView.findViewById<TextView>(R.id.custom_course_end_time)
        val weekDay = itemView.findViewById<TextView>(R.id.custom_course_weekday)
        val cardView = itemView.findViewById<CardView>(R.id.my_custom_card_view)
    }

}






fun MutableList<Item>.singleText(text: String) = add(SingleTextItem(text))
fun MutableList<Item>.singleText(text: String, builder: (TextView.() -> Unit)) = add(SingleTextItem(text).apply { this.builder = builder })

fun MutableList<Item>.setCustomCourseItem(customCourse: CustomCourse, clickBlock: (View) -> Unit) = add(CustomCourseItem(customCourse).apply { this.clickBlock = clickBlock })