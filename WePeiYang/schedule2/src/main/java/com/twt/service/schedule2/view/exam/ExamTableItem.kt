package com.twt.service.schedule2.view.exam

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.exam.ExamTableBean
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.layoutInflater
import java.text.SimpleDateFormat
import java.util.*

class ExamTableItem(val exam: ExamTableBean) : Item {
    override val controller: ItemController
        get() = Controller

    override fun areContentsTheSame(newItem: Item): Boolean {
        return exam == (newItem as? ExamTableItem)?.exam
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return exam == (newItem as? ExamTableItem)?.exam
    }

    companion object Controller : ItemController {
        private var currentTime: String = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("zh_CN")).format(Date())

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.schedule_exam_item_info, parent, false)
            return ViewHolder(view, view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as ExamTableItem
            val exam = item.exam
            holder.apply {
                name.text = exam.name
                location.text = "${exam.location}"
                arrange.text = exam.arrange
                if (exam.ext.isEmpty() && exam.state == "正常") {
                    ext.visibility = View.GONE
                } else {
                    ext.text = "${exam.state}: ${exam.ext}"
                }
                val examTime = "${exam.date} ${exam.arrange}"
                if (currentTime > examTime) {
                    rootView.alpha = 0.3f
                }

                rootView.setOnClickListener {
                    try {
                        val (start, end) = exam.parseToDatePair()
                        addEvent(it.context, exam.name, exam.location, start.time, end.time)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        if (e is IllegalStateException) {
                            Toasty.error(it.context, e.message.toString()).show()
                        }
                    }

                }
            }
        }

        private fun addEvent(context: Context, title: String, location: String, begin: Long, end: Long) {
            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, title)
                putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }

        class ViewHolder(itemView: View, val rootView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.tv_name)
            val location: TextView = itemView.findViewById(R.id.tv_location)
            val arrange: TextView = itemView.findViewById(R.id.tv_arrange)
            val ext: TextView = itemView.findViewById(R.id.tv_ext)
        }

    }
}

fun MutableList<Item>.examTableItem(exam: ExamTableBean) = add(ExamTableItem(exam))
