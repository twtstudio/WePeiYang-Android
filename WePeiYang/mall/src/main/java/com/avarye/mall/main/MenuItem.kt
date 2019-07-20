package com.avarye.mall.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class MenuItem : Item {
    var builder: (ViewHolder.() -> Unit)? = null

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            lateinit var name: TextView
            val view = parent.context.verticalLayout {
                name = textView {

                }
            }
            return ViewHolder(view, name)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as MenuItem
            item.builder?.invoke(holder)
        }

        class ViewHolder(
                itemView: View,
                val name: TextView
        ) : RecyclerView.ViewHolder(itemView)

    }


    override val controller: ItemController
        get() = MenuItem
}

fun MutableList<Item>.addMenuItem(builder: MenuItem.Controller.ViewHolder.() -> Unit) = add(MenuItem().apply { this.builder = builder })
fun MutableList<Item>.addMenuItem() = add(MenuItem())