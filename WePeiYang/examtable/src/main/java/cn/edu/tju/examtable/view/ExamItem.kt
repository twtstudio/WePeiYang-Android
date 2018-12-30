package cn.edu.tju.examtable.view

import android.support.v7.widget.RecyclerView
import android.transition.Visibility
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.edu.tju.examtable.R
import cn.edu.tju.examtable.service.ExamBean
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater
import java.text.SimpleDateFormat
import java.util.*

class ExamItem(val exam: ExamBean) : Item {
    override val controller: ItemController
        get() = Controller

    override fun areContentsTheSame(newItem: Item): Boolean {
        return exam == (newItem as? ExamItem)?.exam
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return exam == (newItem as? ExamItem)?.exam
    }

    companion object Controller : ItemController {
        var currentTime: String = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("zh_CN")).format(Date())

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.exam_item_info, parent, false)
            return ViewHolder(view, view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as ExamItem
            val exam = item.exam
            holder.apply {
                name.text = exam.name
                location.text = "${exam.location}#${exam.seat}"
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

fun MutableList<Item>.examItem(exam: ExamBean) = add(ExamItem(exam))