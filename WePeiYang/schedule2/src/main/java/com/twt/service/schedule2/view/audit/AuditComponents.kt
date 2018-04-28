package com.twt.service.schedule2.view.audit

import android.graphics.Color
import android.support.constraint.ConstraintLayout.LayoutParams.PARENT_ID
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.findConflictList
import com.twt.service.schedule2.extensions.getChineseCharacter
import com.twt.service.schedule2.model.audit.AuditCourse
import com.twt.service.schedule2.model.audit.AuditPopluar
import com.twt.service.schedule2.model.audit.convertToCourse
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.service.schedule2.view.adapter.Item
import com.twt.service.schedule2.view.adapter.ItemController
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout

class ButtonItem(val popluar: AuditPopluar) : Item {

    var builder: (Button.() -> Unit)? = null

    companion object Controller : ItemController {

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            lateinit var button: Button
            val view = parent.context.constraintLayout {
                button = button {
                    text = "按钮"
                    id = View.generateViewId()
                    // 设置无边框
                    val typedValue = TypedValue()
                    context.theme.resolveAttribute(android.R.attr.borderlessButtonStyle, typedValue, true)
                    backgroundResource = typedValue.resourceId
                }.lparams(width = wrapContent, height = wrapContent) {
                    startToStart = PARENT_ID
                    topToTop = PARENT_ID
                    bottomToBottom = PARENT_ID
                    leftMargin = dip(16)
                    verticalMargin = dip(14)
                }
            }.apply {
                layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
            }
            return ViewHolder(view, button)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as ButtonItem
            holder.button.text = item.popluar.course.name
            item.builder?.invoke(holder.button)
        }

        private class ViewHolder(val itemView: View, val button: Button) : RecyclerView.ViewHolder(itemView)
    }

    override val controller: ItemController
        get() = Controller

}

fun MutableList<Item>.buttonItem(auditPopluar: AuditPopluar) = add(ButtonItem(auditPopluar))
fun MutableList<Item>.buttonItem(auditPopluar: AuditPopluar, builder: (Button.() -> Unit)) = add(ButtonItem(auditPopluar).apply { this.builder = builder })

class AuditCourseItem(val auditCourse: AuditCourse) : Item {

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.schedule_item_audit_course, parent, false)
            return ViewHolder(itemView = view)
        }

        /**
         * 只会处理 AuditCourse infos[0] 需要在适配器外层对infos进行copy展开
         */
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as AuditCourseItem
            holder as ViewHolder
            val cachedScheduleManager = TotalCourseManager.getTotalCourseManager().value
            val auditCourse = item.auditCourse.copy(infos = listOf(item.auditCourse.infos[0])) // 只要第一个
            val course = auditCourse.convertToCourse()
            holder.apply {
                auditName.text = auditCourse.courseName
                auditCollege.text = auditCourse.college
                auditTeacher.text = "${auditCourse.infos[0].teacher}  ${auditCourse.infos[0].teacherType} "
                auditWeek.text = "第${course.week.start}-${course.week.end}周 ${course.arrange[0].week}"
                auditTime.text = "周${getChineseCharacter(course.arrange[0].day)} ${course.arrange[0].start}-${course.arrange[0].end}节"
                auditLocation.text = course.arrange[0].room
                val conflictCourse = cachedScheduleManager?.getCourseByWeek(3)?.findConflictList(course, course.arrange[0].day)?.toMutableList()
                conflictCourse?.removeAll { it.coursename == course.coursename }

                if (conflictCourse != null && conflictCourse.isNotEmpty()) {
                    auditConflict.text = "[冲突课程：${conflictCourse[0].coursename}]"
                    auditConflict.textColor = Color.parseColor("#B2430E")
                } else {
                    auditConflict.text = "[没有冲突]"
                    auditConflict.textColor = Color.parseColor("#17A04D")
                }
            }
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val auditName: TextView = itemView.findViewById(R.id.tv_audit_name)
            val auditTeacher: TextView = itemView.findViewById(R.id.tv_audit_teacher)
            val auditCollege: TextView = itemView.findViewById(R.id.tv_audit_college)
            val auditWeek: TextView = itemView.findViewById(R.id.tv_audit_week)
            val auditLocation: TextView = itemView.findViewById(R.id.tv_audit_location)
            val auditTime: TextView = itemView.findViewById(R.id.tv_audit_time)
            val auditConflict: TextView = itemView.findViewById(R.id.tv_audit_conflict)
        }

    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        if (newItem is AuditCourseItem) {
            return newItem.auditCourse == auditCourse
        }
        return false
    }


    override val controller: ItemController
        get() = Controller

}

fun MutableList<Item>.auditCourseItem(auditCourse: AuditCourse) = add(AuditCourseItem(auditCourse))