package com.twtstudio.service.tjwhm.exam.problem

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.commons.selectionIndexToInt
import org.jetbrains.anko.layoutInflater

class SelectionItem(val fragment: ProblemFragment?, val selectionIndex: String, val selectionContent: String, val status: Int) : Item {
    override val controller: ItemController
        get() = Controller

    constructor(selectionItem: SelectionItem, status: Int) : this(selectionItem.fragment, selectionItem.selectionIndex, selectionItem.selectionContent, status)

    companion object Controller : ItemController {

        const val TRUE = 0
        const val FALSE = 1
        const val NONE = 2
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                ItemViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_selection, parent, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ItemViewHolder
            item as SelectionItem
            holder.apply {
                tvSelectionIndex?.text = item.selectionIndex
                tvSelectionContent?.text = item.selectionContent
                when (item.status) {
                    TRUE -> tvSelectionIndex?.apply {
                        setBackgroundResource(R.drawable.exam_shape_selection_true_index)
                        item.fragment?.context?.let { ContextCompat.getColor(it, R.color.white_color) }?.let { setTextColor(it) }
                    }
                    FALSE -> tvSelectionIndex?.apply {
                        setBackgroundResource(R.drawable.exam_shape_selection_wrong_index)
                        item.fragment?.context?.let { ContextCompat.getColor(it, R.color.white_color) }?.let { setTextColor(it) }
                    }
                    NONE -> tvSelectionIndex?.apply {
                        setBackgroundResource(R.drawable.exam_shape_selection_index)
                        item.fragment?.context?.let { ContextCompat.getColor(it, R.color.black_color) }?.let { setTextColor(it) }
                    }
                }

                itemView.setOnClickListener {
                    item.fragment?.onSelectionItemClick(item.selectionIndex.selectionIndexToInt())
                }
            }
        }
    }

    private class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvSelectionIndex: TextView? = itemView?.findViewById(R.id.tv_selection_index)
        val tvSelectionContent: TextView? = itemView?.findViewById(R.id.tv_selection_content)
    }
}

fun MutableList<Item>.selectionItem(fragment: ProblemFragment?, selectionIndex: String, selectionContent: String, status: Int) = add(SelectionItem(fragment, selectionIndex, selectionContent, status))
fun MutableList<Item>.selectionItem(selectionItem: SelectionItem, status: Int) = add(SelectionItem(selectionItem, status))
