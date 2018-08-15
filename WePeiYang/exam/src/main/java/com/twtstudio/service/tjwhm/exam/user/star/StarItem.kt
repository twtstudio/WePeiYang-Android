package com.twtstudio.service.tjwhm.exam.user.star

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.ext.toLessonType
import com.twtstudio.service.tjwhm.exam.ext.toProblemType
import com.twtstudio.service.tjwhm.exam.ext.toSelectionIndex
import com.twtstudio.service.tjwhm.exam.problem.SelectionItem
import com.twtstudio.service.tjwhm.exam.problem.selectionItem
import com.twtstudio.service.tjwhm.exam.user.Ques
import org.jetbrains.anko.layoutInflater

/**
 * Created by tjwhm@TWTStudio at 12:16 PM,2018/8/15.
 * Happy coding!
 */

class StarItem(val context: Context, val quesData: Ques) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                ItemViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_star, parent, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ItemViewHolder
            item as StarItem
            holder.apply {
                tvLessonType?.text = item.quesData.class_id.toLessonType()
                tvProblemType?.text = item.quesData.type.toProblemType()
                tvProblemTitle?.text = Html.fromHtml(item.quesData.content)
                tvAnswer?.text = "题目答案：${item.quesData.answer}"
                rvSelections?.layoutManager = LinearLayoutManager(item.context)
                rvSelections?.withItems {
                    for (i in 0 until item.quesData.option.size)
                        selectionItem(null, i.toSelectionIndex(), item.quesData.option[i], SelectionItem.NONE)
                }
            }
        }
    }

    private class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvLessonType: TextView? = itemView?.findViewById(R.id.tv_star_lesson_type)
        val tvProblemType: TextView? = itemView?.findViewById(R.id.tv_star_problem_type)
        val tvProblemTitle: TextView? = itemView?.findViewById(R.id.tv_star_problem_title)
        val rvSelections: RecyclerView? = itemView?.findViewById(R.id.rv_star_selection)
        val tvAnswer: TextView? = itemView?.findViewById(R.id.tv_star_answer)
        val ivStar: ImageView? = itemView?.findViewById(R.id.iv_star_star)
    }
}

fun MutableList<Item>.starItem(context: Context, quesData: Ques) = add(StarItem(context, quesData))