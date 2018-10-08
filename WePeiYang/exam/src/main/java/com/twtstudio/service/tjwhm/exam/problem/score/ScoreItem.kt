package com.twtstudio.service.tjwhm.exam.problem.score

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
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
import com.twtstudio.service.tjwhm.exam.commons.toSelectionIndex
import com.twtstudio.service.tjwhm.exam.problem.Result
import com.twtstudio.service.tjwhm.exam.problem.SelectionItem
import com.twtstudio.service.tjwhm.exam.problem.TestOneProblemData
import com.twtstudio.service.tjwhm.exam.problem.selectionItem
import org.jetbrains.anko.layoutInflater

/**
 * Created by tjwhm@TWTStudio at 9:27 PM,2018/8/7.
 * Happy coding!
 */

class ScoreItem(val index: Int, val context: Context, val problemData: TestOneProblemData, val resultData: Result) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                ItemViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_score, parent, false))

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ItemViewHolder
            item as ScoreItem
            holder.apply {
                tvProblemTitle?.text = (item.index + 1).toString() + "." + Html.fromHtml(item.problemData.content).toString()
                tvRightAnswer?.text = "正确答案：${item.resultData.true_answer}"
                tvUserAnswer?.text = "你的答案：${item.resultData.answer}"
                if (item.resultData.is_true == 1) tvUserAnswer?.setTextColor(ContextCompat.getColor(item.context, R.color.examTextBlue))
                else tvUserAnswer?.setTextColor(ContextCompat.getColor(item.context, R.color.examTextRed))
                rvScoreSelections?.layoutManager = LinearLayoutManager(item.context)
                rvScoreSelections?.withItems {
                    repeat(item.problemData.option.size) {
                        selectionItem(null, it.toSelectionIndex(), item.problemData.option[it], SelectionItem.NONE)
                    }
                }
            }
        }

    }

    private class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvProblemTitle: TextView? = itemView?.findViewById(R.id.tv_score_item_problem_title)
        val rvScoreSelections: RecyclerView? = itemView?.findViewById(R.id.rv_score_item_selections)
        val tvRightAnswer: TextView? = itemView?.findViewById(R.id.tv_score_right_answer)
        val tvUserAnswer: TextView? = itemView?.findViewById(R.id.tv_score_user_answer)
        val ivHelp: ImageView? = itemView?.findViewById(R.id.iv_score_help)
    }
}

fun MutableList<Item>.scoreItem(index: Int, context: Context, problemData: TestOneProblemData, resultData: Result) = add(ScoreItem(index, context, problemData, resultData))