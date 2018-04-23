package com.twt.service.schedule2.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.jetbrains.anko.layoutInflater

interface Item {
    val controller: ItemController
}

interface ItemController {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item)
}

class ItemAdapter(val items: List<Item>, private val inflater: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val controllers = items.asSequence()
            .map(Item::controller)
            .distinct()
            .toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            controllers[viewType].onCreateViewHolder(parent)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            items[position].controller.onBindViewHolder(holder, items[position])


    override fun getItemViewType(position: Int) = controllers.indexOf(items[position].controller)
}

fun RecyclerView.withItems(init: MutableList<Item>.() -> Unit) {
    adapter = ItemAdapter(mutableListOf<Item>().apply(init), context.layoutInflater)
}
