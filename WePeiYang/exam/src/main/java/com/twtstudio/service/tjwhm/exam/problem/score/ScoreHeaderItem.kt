package com.twtstudio.service.tjwhm.exam.problem.score

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.problem.ScoreBean
import org.jetbrains.anko.layoutInflater
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by tjwhm@TWTStudio at 10:19 PM, 2018/10/16.
 * Happy coding!
 */

class ScoreHeaderItem(val testTime: Long, val scoreBean: ScoreBean) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                ItemViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_score_header, parent, false))

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ItemViewHolder
            item as ScoreHeaderItem
            holder.apply {
                tvProblemNum?.text = "25"
                var radioText = ((item.scoreBean.correct_num.toDouble() / (item.scoreBean.correct_num + item.scoreBean.error_num).toDouble()).toString() + "0000").substring(2, 4)
                if (radioText[0] == '0') radioText = radioText.removeRange(0, 1)
                tvProblemRadio?.text = "正确率：$radioText%"
                tvWrongNum?.text = item.scoreBean.error_num.toString()
                tvTime?.text = SimpleDateFormat("mm:ss").format(Date(item.testTime))
            }
        }

    }

    private class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvProblemNum: TextView? = itemView?.findViewById(R.id.tv_score_problem_num)
        val tvProblemRadio: TextView? = itemView?.findViewById(R.id.tv_score_problem_radio)
        val tvTime: TextView? = itemView?.findViewById(R.id.tv_score_time)
        val tvWrongNum: TextView? = itemView?.findViewById(R.id.tv_score_wrong_num)
    }
}

fun MutableList<Item>.scoreHeaderItem(testTime: Long, scoreBean: ScoreBean) = add(ScoreHeaderItem(testTime, scoreBean))