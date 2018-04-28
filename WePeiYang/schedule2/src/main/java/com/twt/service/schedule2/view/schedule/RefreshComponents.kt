package com.twt.service.schedule2.view.schedule

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.orhanobut.hawk.Hawk
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.RefreshCallback
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.school.classtableCacheKey
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.service.schedule2.view.adapter.CourseDetailViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*

class CourseRefreshStatusComponent(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val statusTextView: TextView = itemView.findViewById(R.id.tv_refresh_status)
    val refreshTimeTextView: TextView = itemView.findViewById(R.id.tv_refresh_time)
    val cacheStatusTextView: TextView = itemView.findViewById(R.id.tv_refresh_cache)
    val startTimeTextView: TextView = itemView.findViewById(R.id.tv_refresh_start)

    fun bind(classtable: Classtable) {
        if (classtable.courses.isEmpty()) {
            refreshTimeTextView.text = "课程表无数据 请尝试刷新课程表"
            cacheStatusTextView.text = "刷新无效请点击下方帮助"
            return
        }
        refreshTimeTextView.text = "办公网刷新：${classtable.updatedAt}"
        cacheStatusTextView.text = "是否来自服务器缓存：${classtable.cache}"
        startTimeTextView.text = "学期开始时间：${getDateString(classtable.termStart)}"
    }

    private fun getDateString(unixTime: Long): String {
        val dateFormat = SimpleDateFormat("yy/MM/dd HH:mm")
        val string = dateFormat.format(Date(unixTime * 1000L))
        return string
    }

    companion object {
        fun create(inflater: LayoutInflater, container: ViewGroup): CourseRefreshStatusComponent {
            val view = inflater.inflate(R.layout.schedule_item_refresh_info, container, false)
            return CourseRefreshStatusComponent(view)
        }
    }
}

fun createRefreshList(refreshCallback: RefreshCallback = {}): List<Any> {
    val list = mutableListOf<Any>()
    val classtable: Classtable = Hawk.get(classtableCacheKey) ?: Classtable(courses = listOf())
    list.add(classtable)
    list.add(CourseDetailViewModel(R.drawable.ic_schedule_refresh, "刷新全部课表", {
        TotalCourseManager.getTotalCourseManager(refreshTju = true, refreshAudit = true, refreshCustom = true, refreshCallback = refreshCallback)
    }))
    list.add("其他刷新设置")
    list.add(CourseDetailViewModel(R.drawable.ic_schedule_refresh, "仅刷新办公网"){
        TotalCourseManager.getTotalCourseManager(refreshTju = true, refreshAudit = false, refreshCustom = false, refreshCallback = refreshCallback)
    })
    list.add(CourseDetailViewModel(R.drawable.ic_schedule_refresh, "仅刷新蹭课"){
        TotalCourseManager.getTotalCourseManager(refreshTju = false, refreshAudit = true, refreshCustom = false, refreshCallback = refreshCallback)
    })
    list.add(CourseDetailViewModel(R.drawable.ic_schedule_refresh, "仅刷新自定义课程"){
        TotalCourseManager.getTotalCourseManager(refreshTju = false, refreshAudit = false, refreshCustom = true, refreshCallback = refreshCallback)
    })
    list.add("重要帮助")
    list.add(CourseDetailViewModel(R.drawable.ic_schedule_info, "课程表刷新帮助(重要)/FAQ",{
        it.context.alert {
            title = "课程表刷新帮助/FAQ"
            message = "暂时还没有做刷新成功的提示... 但是课表确实是会刷新\n\n" +
                    "由于TJU办公网服务的不稳定 所以即使刷新失败 我们也会加载本地的缓存，所以不要害怕刷丢数据\n\n" +
                    "[办公网更新时间] 因为办公网可能会烂 我们为了保证稳定性，所以会在访问办公网失败的时候使用服务器缓存，" +
                    "但是可能你在某个时候变更了办公网选课，因此关注一下这个时间来避免自己的新选的课没有出现的情况（就是看看刷新时间也没有是选课之后）\n\n" +
                    "[是否来自服务器缓存] 结合办公网更新时间来看，字段为true表示，上次刷新的数据来着天外天服务器的缓存数据（就是说并没有拉取到办公网的数据）" +
                    "，此时要留意办公网更新时间是不是和自己的选课有出入，避免丢课\n\n" +
                    "[刷不出来怎么办] 多刷几次。 然后想想自己最近有没有修改办公网密码什么的，如果修改了密码请重新绑定。 然后还不行的话，看看办公网是不是崩了... " +
                    "虽然我们全力保证办公网崩溃的时候，返回缓存数据，但是如果你是第一次登录就不存在缓存之类... 无奈"
        }.show()
    }))
    return list
}

