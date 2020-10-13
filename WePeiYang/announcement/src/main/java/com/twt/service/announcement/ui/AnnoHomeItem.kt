package com.twt.service.announcement.ui

import android.annotation.SuppressLint
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.twt.service.announcement.ui.main.AnnoActivity
import com.twt.wepeiyang.commons.ui.rec.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.startActivity

class AnnoHomeItem : Item {
    override fun areItemsTheSame(newItem: Item) = true

    override fun areContentsTheSame(newItem: Item) = true

    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        @SuppressLint("SetTextI18n")
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val homeItem = HomeItem(parent)
            val view = RecyclerView(parent.context)
            view.apply {
                layoutManager = LinearLayoutManager(parent.context)
                itemAnimator = DefaultItemAnimator()
                layoutParams = FrameLayout.LayoutParams(matchParent, matchParent).apply {
                    horizontalPadding = dip(8)
                }
            }
            homeItem.apply {
                itemName.text = "校务专区（测试中）"
                itemContent.visibility = View.VISIBLE
                setContentView(view)
            }
            return MyViewHolder(homeItem.rootView, homeItem, view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as MyViewHolder
            holder.homeItem.apply {
                itemContent.text = "进入"
                rootView.setOnClickListener {
                    it.context.startActivity<AnnoActivity>()
                }
            }

            holder.recyclerView.refreshAll {
                lightText("欢迎提出建议")
            }
        }

        private class MyViewHolder(itemView: View, val homeItem: HomeItem, val recyclerView: RecyclerView) : RecyclerView.ViewHolder(itemView)

    }
}

fun MutableList<Item>.annoHomeItem() = add(AnnoHomeItem())