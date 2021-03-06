package com.twt.service.ecard.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.twt.service.ecard.R
import com.twt.service.ecard.model.EcardProportionalBarDataBean
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class EcardProportionalItem(val list: List<EcardProportionalBarDataBean>) : Item {
    override val controller: ItemController
        get() = Controller

    override fun areContentsTheSame(newItem: Item): Boolean {
        return false
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return false
    }

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.ecard_item_proportional_bar, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as EcardProportionalItem
            val totalList = item.list
            val a = totalList.filter { it.type == "canteen" }[0].total.toInt()
            val b = totalList.filter { it.type != "others" }.fold(0) { p, q -> p + q.total.toInt() }
            val c = totalList.fold(0) { p, q -> p + q.total.toInt() }
            holder.proportionalBar.apply {
                max = c
                progress = a
                secondaryProgress = b
            }

            totalList.forEach { data ->
                when (data.type) {
                    "canteen" -> holder.apply {
                        textOfDining.text = "食堂: ${data.total}"
                    }
                    "supermarket" -> holder.apply {
                        textOfShopping.text = "超市: ${data.total}"
                    }
                    "others" -> holder.apply {
                        textOfElse.text = "其他: ${data.total}"
                    }
                }
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val proportionalBar: ProgressBar = itemView.findViewById(R.id.pb_proportional_bar)
        val textOfDining: TextView = itemView.findViewById(R.id.tv_proportional_dining)
        val textOfShopping: TextView = itemView.findViewById(R.id.tv_proportional_shopping)
        val textOfElse: TextView = itemView.findViewById(R.id.tv_proportional_else)
    }
}

fun MutableList<Item>.ecardProportionalItem(list: List<EcardProportionalBarDataBean>) = add(EcardProportionalItem(list))