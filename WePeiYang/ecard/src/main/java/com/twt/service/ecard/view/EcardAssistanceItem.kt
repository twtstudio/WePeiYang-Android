package com.twt.service.ecard.view

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.ecard.R
import com.twt.service.ecard.model.*
import com.twt.wepeiyang.commons.ui.rec.*
import org.jetbrains.anko.layoutInflater

class EcardAssistanceItem(private val problem: ProblemBean, val isShowText: Boolean, val isShowEnd: Boolean, val isShowChart: Boolean) : Item {
    override val controller: ItemController
        get() = Controller

    override fun areContentsTheSame(newItem: Item): Boolean {
        return problem == (newItem as? EcardAssistanceItem)?.problem
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return problem == (newItem as? EcardAssistanceItem)?.problem
    }

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.ecard_item_assistance, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as EcardAssistanceItem
            val problem = item.problem
            //正则换行
            val numPattern = " +".toRegex()
            val result = numPattern.replace(problem.content, "")

            holder.apply {
                title.text = problem.title
                textOfProblem.text = result
                contentOfText.visibility = if (item.isShowText) View.VISIBLE else View.GONE
                end.visibility = if (item.isShowEnd) {
                    View.VISIBLE
                } else View.GONE
                chart.visibility = if (item.isShowChart) View.VISIBLE else View.GONE
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.tv_assistance_item_title)
            val textOfProblem: TextView = itemView.findViewById(R.id.tv_assistance_item_content)
            val contentOfText: ConstraintLayout = itemView.findViewById(R.id.cl_assistance_text)
            val chart: ConstraintLayout = itemView.findViewById(R.id.cl_assistance_position)
            val end: ConstraintLayout = itemView.findViewById(R.id.cl_assistance_item_end)
        }
    }
}

fun MutableList<Item>.ecardAssistanceItem(problem: ProblemBean, isShowText: Boolean = true, isShowEnd: Boolean = false, isShowChart: Boolean = false) = add(EcardAssistanceItem(problem, isShowText, isShowEnd, isShowChart))