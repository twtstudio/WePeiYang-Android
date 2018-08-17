package com.twtstudio.service.tjwhm.exam.problem

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.service.tjwhm.exam.R
import org.jetbrains.anko.layoutInflater

/**
 * Created by tjwhm@TWTStudio at 1:42 AM,2018/8/17.
 * Happy coding!
 */

class ProblemIndexItem(val activity: ProblemActivity, val index: Int, val problemIndex: ProblemIndex) : Item {

    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                ItemViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_problem_index, parent, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ItemViewHolder
            item as ProblemIndexItem
            holder.apply {
                tvProblemIndex?.apply {
                    text = (item.index + 1).toString()
                    setTextColor(ContextCompat.getColor(item.activity, R.color.examBlackText))
                    when (item.problemIndex) {
                        is ProblemIndex.TRUE, ProblemIndex.NOW.TRUE -> setTextColor(ContextCompat.getColor(item.activity, R.color.examBlueText))
                        is ProblemIndex.WRONG, ProblemIndex.NOW.WRONG -> setTextColor(ContextCompat.getColor(item.activity, R.color.examRedText))
                    }
                    when (item.problemIndex) {
                        is ProblemIndex.NOW -> {
                            setBackgroundResource(R.drawable.exam_selection_index_shape)
                        }
                    }
                }
                itemView.setOnClickListener { item.activity.onProblemIndexItemClick(item.index) }
                setIsRecyclable(false)
            }
        }
    }

    private class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvProblemIndex: TextView? = itemView?.findViewById(R.id.tv_item_problem_index)
    }
}

fun MutableList<Item>.problemIndexItem(activity: ProblemActivity, num: Int, problemIndex: ProblemIndex) = add(ProblemIndexItem(activity, num, problemIndex))