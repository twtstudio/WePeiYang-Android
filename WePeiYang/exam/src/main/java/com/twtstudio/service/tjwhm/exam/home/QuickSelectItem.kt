package com.twtstudio.service.tjwhm.exam.home

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.service.tjwhm.exam.R
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.layoutInflater

/**
 * Created by tjwhm@TWTStudio at 5:04 PM, 2018/9/22.
 * Happy coding!
 */

class QuickSelectItem(val context: Context, val courseID: Int, val courseName: String) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                ItemViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_quick_select, parent, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as QuickSelectItem
            holder as ItemViewHolder
            holder.apply {
                tvName?.text = item.courseName
                itemView.setOnClickListener {
                    Toasty.info(item.context, item.courseID.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView? = itemView?.findViewById(R.id.tv_quick_select_name)
    }
}

fun MutableList<Item>.quickSelectItem(context: Context, courseID: Int, courseName: String) = add(QuickSelectItem(context, courseID, courseName))