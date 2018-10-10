package com.twt.service.schedule2.view.home

import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.currentUnixTime
import com.twt.service.schedule2.extensions.flatDay
import com.twt.service.schedule2.extensions.getDayOfWeek
import com.twt.service.schedule2.model.Course
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.service.schedule2.view.schedule.ScheduleActivity
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twt.wepeiyang.commons.ui.spanned
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*

class ScheduleHomeItem(val lifecycleOwner: LifecycleOwner) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val homeItem = HomeItem(parent)
            val view = parent.context.linearLayout {
                orientation = LinearLayout.HORIZONTAL
            }.apply {
                layoutParams = FrameLayout.LayoutParams(matchParent, dip(130)).apply {
                    horizontalMargin = dip(16)
                    verticalMargin = dip(16)
                }
            }
            homeItem.apply {
                setContentView(view)
            }
            return ViewHolder(homeItem.rootView, homeItem, view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as ScheduleHomeItem
            TotalCourseManager.getTotalCourseManager().bindNonNull(item.lifecycleOwner) {
                // 定义两个Time 一个是今天 一个是明天
                val currentUnixTime = it.currentUnixTime
                val tomorrowUnixTime = it.currentUnixTime + it.dayOfInt

                // 如果是晚上9点后 就显示明天
                val calendar = Calendar.getInstance()
                val hours = calendar.get(Calendar.HOUR_OF_DAY)
                val displayTomorrow = hours >= 21
                val unixTime = if (displayTomorrow) tomorrowUnixTime else currentUnixTime

                val dateFormat = SimpleDateFormat("yyyy/MM/dd E")
                val date = Date(unixTime * 1000L)
                val dateString = dateFormat.format(date)

                val flatList = it.getCourseByDay(unixTime).flatDay(it.getDayOfWeek(dayUnix = unixTime), forceFill = true).toMutableList()
                val resultList = flatList.flatTwice()

                val count = resultList.count { it.coursename != "空" && it.weekAvailable }

                if (count > 0) {
                    holder.linearLayout.withCourses(resultList)
                    holder.homeItem.setContentView(holder.linearLayout)
                } else {
                    holder.homeItem.contentContainer.apply {
                        removeAllViewsInLayout()
                        textView {
                            text = "今天没有课\n 做点有趣的事情吧！"
                            textSize = 16f
                            gravity = Gravity.CENTER_HORIZONTAL
                        }.apply {
                            layoutParams = FrameLayout.LayoutParams(wrapContent, wrapContent).apply {
                                gravity = Gravity.CENTER
                                margin = dip(16)
                            }
                        }
                    }
                }
                holder.homeItem.apply {
                    itemName.text = dateString
                    val contentTextPrefix = if (displayTomorrow) "<span style=\"color:#DBB86B\";>阁下明天 </span>" else "阁下今天"
                    val contentText = if (count > 0) "有 <span style=\"color:#DBB86B\";>${count}</span> 节课" else "无课"
                    itemContent.text = (contentTextPrefix + contentText).spanned
                    rootView.setOnClickListener {
                        val intent = Intent(it.context, ScheduleActivity::class.java)
                        it.context.startActivity(intent)
                    }
                }
            }
        }

        private class ViewHolder(itemView: View?, val homeItem: HomeItem, val linearLayout: LinearLayout) : RecyclerView.ViewHolder(itemView)

        /**
         * 之前的EmptyCourse是代表一个小节课程的 但是现在在主页 需要变成大课程
         */
        private fun MutableList<Course>.flatTwice(): List<Course> {
            val removeList = mutableSetOf<Course>()
            this.forEachIndexed { index, course ->
                if (course.coursename == "空" && this[index] !in removeList && index != (this.size - 1) && this[index + 1].coursename == "空") {
                    removeList.add(this[index + 1])
                }
            }
            // 处理晚上三节课 剩下一节空课程 那节空课程就tm不要再显示了
            if (this.last().coursename == "空" && this.getOrNull(size - 2)?.coursename != "空") {
                removeList.add(this[size - 1])
            } else if (this.getOrNull(size - 2)?.coursename == "空") {
                // emmm 最后两节都是空课也算了吧
                removeList.add(this[size - 2])
            }
            this.removeAll(removeList)
            return this
        }

        private fun LinearLayout.withCourses(courses: List<Course>) {
            removeAllViewsInLayout()
            courses.forEach {
                val view = this.context.layoutInflater.inflate(R.layout.schedule_item_course, this, false).apply {
                    layoutParams = LinearLayout.LayoutParams(matchParent, matchParent, 1f).apply {
                        horizontalMargin = dip(2)
                    }
                }
                val holder = CourseViewHolder(view)
                holder.bind(it)
                this.addView(view)
            }

        }

        /**
         * copy from Schedule Adapter
         * 虽然我觉得用封装好一点 但是还是不想写了hhh
         */
        class CourseViewHolder(itemView: View) {
            val cardView: CardView = itemView.findViewById(R.id.cv_item_course)
            val textView: TextView = itemView.findViewById(R.id.tv_item_course)

            fun bind(course: Course) {
                if (course.coursename == "空") {
                    cardView.visibility = View.VISIBLE
                    cardView.setContentPadding(4, 4, 4, 4)
                    cardView.removeView(textView)
                    cardView.setCardBackgroundColor(Color.parseColor("#EFEFEF"))
                    val innerCardView = CardView(cardView.context).apply {
                        radius = dip(5).toFloat()
                        cardElevation = 0f
                        layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)
                        addView(textView)
                        val stringSpan = SpannableString("阁下无事").apply {
                            setSpan(TypefaceSpan("sans-serif-medium"), 0, this.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        textView.apply {
                            text = stringSpan
                            textColor = Color.parseColor("#C0C0C0")
                        }
                    }
                    cardView.addView(innerCardView)

                } else {
                    cardView.visibility = View.VISIBLE
                    var text = ""
                    text += course.statusMessage
                    text += "${course.coursename}\n@${course.arrange[0].room} "

                    val roomCluster = course.arrange[0].room.split("楼")

                    val divider = "\n \n"
                    val creditDisplayed = "—— " + course.credit + " ——"
                    val stringSpan = SpannableString(creditDisplayed + divider + course.coursename + divider + roomCluster[0] + "-" + roomCluster[1])
                    val lens = arrayOf(creditDisplayed.length, divider.length, course.coursename.length, divider.length, roomCluster[0].length, 1, roomCluster[1].length)
                    val accu = arrayOf(lens[0], lens[0]+lens[1], lens[0]+lens[1]+lens[2], lens[0]+lens[1]+lens[2]+lens[3], lens[0]+lens[1]+lens[2]+lens[3]+lens[4], lens[0]+lens[1]+lens[2]+lens[3]+lens[4]+lens[5], lens[0]+lens[1]+lens[2]+lens[3]+lens[4]+lens[5]+lens[6])
                    val e = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE

                    stringSpan.setSpan(StyleSpan(Typeface.BOLD), accu[1], accu[2], 0)
                    stringSpan.setSpan(AbsoluteSizeSpan(10, true), 0, accu[0], e)
                    stringSpan.setSpan(AbsoluteSizeSpan(3, true), accu[0], accu[1], e)
                    stringSpan.setSpan(AbsoluteSizeSpan((if (lens[2] > 10) 13 else 15), true), accu[1], accu[2], e)
                    stringSpan.setSpan(AbsoluteSizeSpan(4, true), accu[2], accu[3], e)
                    stringSpan.setSpan(AbsoluteSizeSpan(10, true), accu[3], stringSpan.length, e)

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
                     * 这里暂时不渲染多节角标
                     * 但是保留代码
                     */
                    if (course.next.size > 0 && false) {
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


    }

    override val controller: ItemController
        get() = Controller


}

fun MutableList<Item>.homeScheduleItem(lifecycleOwner: LifecycleOwner) = add(ScheduleHomeItem(lifecycleOwner))