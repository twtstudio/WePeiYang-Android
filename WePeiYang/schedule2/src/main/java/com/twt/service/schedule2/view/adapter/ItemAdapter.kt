package com.twt.service.schedule2.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.schedule2.R
import org.jetbrains.anko.layoutInflater

interface Item {
    val controller: ItemController
}

interface ItemController {
    fun onCreateViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item)
}

class ItemAdapter(val items: List<Item>, private val inflater: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val strategies = items.asSequence()
            .map { it.controller }
            .distinct()
            .toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            strategies[viewType].onCreateViewHolder(parent, inflater)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            items[position].controller.onBindViewHolder(holder, items[position])


    override fun getItemViewType(position: Int) = strategies.indexOf(items[position].controller)
}

class LabelItem(val text: String) : Item {

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
            val view = inflater.inflate(R.layout.schedule_item_refresh_info, parent, false) // 假的
            return LabelItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            val labelItem = item as LabelItem
            val labelHolder = holder as LabelItemViewHolder
            labelHolder.textView.text = labelItem.text
        }

        private class LabelItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(0)
        }
    }

    override val controller: ItemController get() = Controller
}

fun MutableList<Item>.label(text: String) {
    add(LabelItem(text))
}

fun MutableList<Item>.label(init: LabelItem.() -> Unit) {
    add(LabelItem("Fake").apply(init))
}

fun RecyclerView.withItems(init: MutableList<Item>.() -> Unit) {
    adapter = ItemAdapter(mutableListOf<Item>().apply(init), context.layoutInflater)
}

val datalist = listOf("text")
