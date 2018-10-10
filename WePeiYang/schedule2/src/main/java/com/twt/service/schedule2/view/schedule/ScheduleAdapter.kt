package com.twt.service.schedule2.view.schedule

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.util.DiffUtil
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.Course
import com.twt.service.schedule2.model.SchedulePref
import com.twt.wepeiyang.commons.experimental.CommonContext


/**
 * 使用GridLayoutManager (HORIZONTAL) span = 12
 */
class ScheduleAdapter(val context: Context) : RecyclerView.Adapter<ScheduleAdapter.CourseViewHolder>() {
    val courseList = mutableListOf<Course>()
    private var firstColumnSize = 1
    private val firstRowIndexList = mutableListOf<Int>()
    var displayType = ScheduleDisplayType.SEVENDAYS // Schedule的显示模式 是五天模式还是七天模式

    /**
     * 外部的点击事件统一传递
     */
    var clickListener: (Course) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.schedule_item_course, parent, false)
        if (viewType == ScheduleDisplayType.SEVENDAYS) {
            view.flatViewHolder(7, parent)
        } else {
            view.flatViewHolder(5, parent)
        }
        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.bind(course)
        holder.cardView.setOnClickListener {
            clickListener.invoke(course)
        }
    }

    /**
     * 传入二维列表
     * 内置DiffUtil的处理
     */
    fun refreshCourseListFlat(courses: List<List<Course>>) {
        val courseListOld: List<Course> = ArrayList<Course>(courseList) // 拷贝之前的列表
        // 一些刷新之前的恢复操作
        courseList.removeAll { true }
        firstRowIndexList.removeAll { true }
        firstColumnSize = 1

        if (courses.isNotEmpty()) {
            firstColumnSize = courses[0].size
        }
        courses.forEach {
            if (it.isNotEmpty()) {
                firstRowIndexList.add(courseList.size)
            }
            courseList.addAll(it)
        }
        courseList.forEach {
            it.refreshStatusMessage()
        }
        // 到这里 数据源已经被更改了 所以我们需要用DiffUtil来处理一下
        val diffResult = DiffUtil.calculateDiff(
                CourseListDiffCallback(oldItems = courseListOld, newItems = courseList))
        displayType = when (firstRowIndexList.size) {
            5 -> ScheduleDisplayType.FIVEDAYS
            7 -> ScheduleDisplayType.SEVENDAYS
            else -> ScheduleDisplayType.SEVENDAYS
        }
        // 如果关闭了课程表自动折叠
        if (!SchedulePref.autoCollapseSchedule) {
            displayType = ScheduleDisplayType.SEVENDAYS
        }
        diffResult.dispatchUpdatesTo(this)

    }


    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cv_item_course)
        val textView: TextView = itemView.findViewById(R.id.tv_item_course)

        fun bind(course: Course) {
            if (course.coursename == "空") {
                cardView.visibility = View.INVISIBLE
            } else {
                cardView.visibility = View.VISIBLE
                var text = ""
                text += course.statusMessage
                text += "${course.coursename}\n@${course.arrange[0].room} "

                val roomCluster = course.arrange[0].room.split("楼")

                val divider = "\n \n"
                val creditDisplayed = "- " + course.credit + " -"
                val stringSpan = SpannableString(creditDisplayed + divider + course.coursename + divider + roomCluster[0] + "-" + roomCluster[1])
                val lens = arrayOf(creditDisplayed.length, divider.length, course.coursename.length, divider.length, roomCluster[0].length, 1, roomCluster[1].length)
                val accu = arrayOf(lens[0], lens[0]+lens[1], lens[0]+lens[1]+lens[2], lens[0]+lens[1]+lens[2]+lens[3], lens[0]+lens[1]+lens[2]+lens[3]+lens[4], lens[0]+lens[1]+lens[2]+lens[3]+lens[4]+lens[5], lens[0]+lens[1]+lens[2]+lens[3]+lens[4]+lens[5]+lens[6])
                val e = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE

                stringSpan.setSpan(StyleSpan(Typeface.BOLD), accu[1], accu[2], 0)
                stringSpan.setSpan(AbsoluteSizeSpan(8, true), 0, accu[0], e)
                stringSpan.setSpan(AbsoluteSizeSpan(3, true), accu[0], accu[1], e)
                stringSpan.setSpan(AbsoluteSizeSpan((if (lens[2] > 6) 13 else 15), true), accu[1], accu[2], e)
                stringSpan.setSpan(AbsoluteSizeSpan(4, true), accu[2], accu[3], e)
                stringSpan.setSpan(AbsoluteSizeSpan(8, true), accu[3], stringSpan.length, e)


                /**
                 * 因为Rec的view是存在着缓存 在后面私自addView后 就会加到缓存里面去
                 * 但是不知道谁会取出这个缓存 使用就会存在蜜汁多节课程角标的问题
                 * 因此我们查看Cardview的子view 来处理这个问题
                 */
                if (cardView.childCount > 1) {
                    for (i in 0 until cardView.childCount) {
                        val view = cardView.getChildAt(i)
                        if (view is FrameLayout) {
                            cardView.removeView(view)
                            Log.e("ScheduleAdapter", "duplicated slantedTextview")
                        }
                    }
                }
                /**
                 * 渲染多节角标
                 */
                if (course.next.size > 0) {
                    var fuckConflict = false
                    course.next.forEach {
                        if (it.statusMessage?.contains("冲突") == true) {
                            fuckConflict = true
                        }
                    }
                    val view = LayoutInflater.from(cardView.context).inflate(R.layout.schedule_item_course_slant, cardView, false)
                    // 第三方的SlantedtextView的文字绘制存在bug 导致无法使用Bitmap
                    val slantedTextView: com.twt.service.schedule2.view.schedule.SlantedTextView = view.findViewById(R.id.tv_course_slant)
                    if (fuckConflict) {
                        slantedTextView.setSlantedBackgroundColor(Color.parseColor("#33000000"))
                        slantedTextView.text = "冲突"
                    } else {
                        slantedTextView.setSlantedBackgroundColor(Color.parseColor("#33ffffff"))
                        slantedTextView.text = "多节"
                    }
                    cardView.addView(view)
                }
                textView.text = stringSpan
                if (course.weekAvailable) {
                    textView.setTextColor(Color.WHITE)
                    cardView.setCardBackgroundColor(CommonContext.application.resources.getColor(course.courseColor))
                } else {
                    cardView.setCardBackgroundColor(CommonContext.application.resources.getColor(R.color.schedule_background_gray))
                    textView.setTextColor(CommonContext.application.resources.getColor(R.color.schedule_gray))
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return displayType
    }


    /**
     * 均分各个块的宽度 来做到水平填充
     */
    private fun View.flatViewHolder(spanSize: Int, parent: ViewGroup) {
        val availableWidth = parent.width - parent.paddingStart
        val perItemWith = availableWidth / spanSize
        val layoutParams: GridLayoutManager.LayoutParams = this.layoutParams as GridLayoutManager.LayoutParams
        layoutParams.width = perItemWith - layoutParams.marginStart - layoutParams.marginEnd
        this.layoutParams = layoutParams

    }

    /**
     * 五天填充还是七天填充
     */
    class ScheduleDisplayType {
        companion object {
            val FIVEDAYS = 0
            val SEVENDAYS = 1
        }
    }

}

class CourseSpanSizeLookup(val courses: List<Course>) : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        val span = courses[position].arrange[0].let { it.end - it.start + 1 }
        return span
    }
}

class CourseListDiffCallback(val oldItems: List<Course>, val newItems: List<Course>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
//        Log.e("Diff","CourseItemSame?: ${oldItem.coursename} - ${newItem.coursename} = ${oldItem == newItem}")
        return oldItem == newItem
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        val arrangeEqual = oldItem.arrange == newItem.arrange
        val nextListEqual = oldItem.next == newItem.next
        val bodyEqual = oldItem == newItem
//        Log.e("Diff","CourseContentSame?: ${oldItem.coursename} - ${newItem.coursename} = ${(arrangeEqual && nextListEqual && bodyEqual)}")
        return (arrangeEqual && nextListEqual && bodyEqual)
    }

}

