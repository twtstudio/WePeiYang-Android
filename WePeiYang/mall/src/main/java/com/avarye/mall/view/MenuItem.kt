package com.avarye.mall.view

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.*
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController

class MenuItem : Item {
    var builder: (ViewHolder.() -> Unit)? = null

    companion object Controller : ItemController {

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = ConstraintLayout(parent.context)
            val name = TextView(parent.context)
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