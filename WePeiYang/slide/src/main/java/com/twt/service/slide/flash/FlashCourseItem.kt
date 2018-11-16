package com.twt.service.slide.flash

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.twt.service.slide.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

data class FlashCourseData(val courseName: String, val courseTime: String, val courseLocation: String, val courseColor: Int)

class FlashCourseItem(val data: FlashCourseData) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view: View = parent.context.layoutInflater.inflate(R.layout.flash_item_course, parent, false)
            return ViewHolder(
                    view,
                    rootView = view,
                    courseTimeTextView = view.findViewById(R.id.tv_flash_course_time),
                    courseCardView = view.findViewById(R.id.card_flash_course),
                    courseNameTextView = view.findViewById(R.id.tv_flash_course_name),
                    courseLocationTextView = view.findViewById(R.id.tv_flash_course_location)
                    )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as FlashCourseItem
            holder as ViewHolder

            val data = item.data
            holder.apply {
                courseTimeTextView.text = data.courseTime
                courseCardView.setCardBackgroundColor(data.courseColor)
                courseNameTextView.text = data.courseName
                courseLocationTextView.text = data.courseLocation
            }
        }

        class ViewHolder(
                itemView: View?,
                val rootView: View,
                val courseTimeTextView: TextView,
                val courseCardView: CardView,
                val courseNameTextView: TextView,
                val courseLocationTextView: TextView
        ) : RecyclerView.ViewHolder(itemView)

    }
}

fun MutableList<Item>.flashCourse(data: FlashCourseData) = add(FlashCourseItem(data))