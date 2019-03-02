package com.twt.service.schedule2.view.exam

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.exam.ExamTableBean
import com.twt.service.schedule2.model.exam.ExamTableLocalAdapter
import com.twt.service.schedule2.model.exam.addEvent
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.mta.mtaExpose
import com.twt.wepeiyang.commons.ui.rec.*
import com.twt.wepeiyang.commons.ui.view.ColorCircleView
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*


class ExamTableHomeLittleItem(val exam: ExamTableBean) : Item {
    override val controller: ItemController
        get() = Controller

    override fun areContentsTheSame(newItem: Item): Boolean {
        return exam == (newItem as? ExamTableHomeLittleItem)?.exam
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return exam == (newItem as? ExamTableHomeLittleItem)?.exam
    }

    companion object Controller : ItemController {
        private var currentTime: String = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("zh_CN")).format(Date())

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.schedule_exam_home_item, parent, false)
            return ViewHolder(view, view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as ExamTableHomeLittleItem
            val exam = item.exam
            holder.apply {
                name.text = exam.name
                info.text = "${exam.location}#${exam.seat}  ${exam.arrange}"
                val (days, hours) = exam.calETA()
                eta.text = "${exam.date}  ETA: ${days}天${hours}小时"
                if (exam.ext.isEmpty() && exam.state == "正常") {
                    ext.visibility = View.GONE
                } else {
                    ext.text = "${exam.state}: ${exam.ext}"
                }
                val examTime = "${exam.date} ${exam.arrange}"
                if (currentTime > examTime) {
                    rootView.alpha = 0.3f
                }

                val totalHours = days * 24 + hours
                when (totalHours) {
                    in 0..36 -> colorCircleView.color = Color.parseColor("#FF5D64") // red
                    in 36..96 -> colorCircleView.color = Color.parseColor("#FFC017") // yellow
                    else -> colorCircleView.color = Color.parseColor("#3BCBFF") //blue
                }

                rootView.setOnClickListener {
                    try {
                        val (start, end) = exam.parseToDatePair()
                        addEvent(it.context, "${exam.name} ${exam.type}", "${exam.location}#${exam.seat}", start.time, end.time, exam)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        if (e is IllegalStateException) {
                            Toasty.error(it.context, e.message.toString()).show()
                        }
                    }

                }
            }
        }


        class ViewHolder(itemView: View, val rootView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.tv_name)
            val info: TextView = itemView.findViewById(R.id.tv_exam_info)
            val eta: TextView = itemView.findViewById(R.id.tv_eta)
            val ext: TextView = itemView.findViewById(R.id.tv_ext)
            val colorCircleView: ColorCircleView = itemView.findViewById(R.id.color_circle_exam)
        }

    }
}

fun MutableList<Item>.examTableHomeLittleItem(exam: ExamTableBean) = add(ExamTableHomeLittleItem(exam))

class ExamTableHomeItem() : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val homeItem = HomeItem(parent)
            val view = RecyclerView(parent.context)
            view.apply {
                layoutManager = LinearLayoutManager(parent.context)
                itemAnimator = DefaultItemAnimator()
                layoutParams = FrameLayout.LayoutParams(matchParent, matchParent).apply {
                    horizontalPadding = dip(16)
                }
            }
            homeItem.apply {
                itemName.text = "EXAM"
                itemContent.visibility = View.VISIBLE
                setContentView(view)
            }
            return MyViewHolder(homeItem.rootView, homeItem, view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as MyViewHolder
            holder.homeItem.apply {
                itemContent.text = "查看考表"
                listOf(imgGo, itemContent).forEach {
                    it.setOnClickListener {
                        it.context.startActivity<ExamTableActivity>()
                        mtaClick("schedule_首页ExamItem进入考表")
                    }
                }
            }
            val currentTime: String = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("zh_CN")).format(Date())
            launch(UI + QuietCoroutineExceptionHandler) {
                mtaExpose("schedule_主页考表刷新成功")
                val list = ExamTableLocalAdapter.getExamMap().await().values.toList().sortedBy {
                    it.date + it.arrange
                }.filter {
                    val examTime = "${it.date} ${it.arrange}"
                    currentTime <= examTime
                }
                holder.recyclerView.refreshAll {
                    list.forEach { exam ->
                        examTableHomeLittleItem(exam)
                    }
                    when {
                        list.isEmpty() -> lightText("暂未查询到考试安排，舒服")
                        list.first().calETA().first > 1 -> lightText("问题不大")
                        list.first().calETA().first <= 1 -> lightText("我是微北洋，我慌得一批")
                    }
                }
            }

        }

        private class MyViewHolder(itemView: View, val homeItem: HomeItem, val recyclerView: RecyclerView) : RecyclerView.ViewHolder(itemView)


    }
}

fun MutableList<Item>.examTableHomeItem() = add(ExamTableHomeItem())