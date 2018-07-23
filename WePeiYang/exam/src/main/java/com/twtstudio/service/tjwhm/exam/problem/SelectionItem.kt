package com.twtstudio.service.tjwhm.exam.problem

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.service.tjwhm.exam.R
import org.jetbrains.anko.layoutInflater

class SelectionItem(val context: Context, val selectionIndex: String, val selectionContent: String) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                ItemViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_selection, parent, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ItemViewHolder
            item as SelectionItem
            holder.apply {
                tvSelectionIndex?.text = item.selectionIndex
                tvSelectionContent?.text = item.selectionContent
            }
        }

    }

    private class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvSelectionIndex: TextView? = itemView?.findViewById(R.id.tv_selection_index)
        val tvSelectionContent: TextView? = itemView?.findViewById(R.id.tv_selection_content)
    }
}

fun MutableList<Item>.selectionItem(context: Context, selectionIndex: String, selectionContent: String) = add(SelectionItem(context, selectionIndex, selectionContent))
