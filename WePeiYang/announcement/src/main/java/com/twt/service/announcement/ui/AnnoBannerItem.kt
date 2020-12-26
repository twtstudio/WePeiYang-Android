package com.twt.service.announcement.ui

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.twt.service.announcement.R
import com.twt.service.announcement.ui.main.AnnoActivity
import com.twt.wepeiyang.commons.ui.rec.*
import org.jetbrains.anko.*

class AnnoBannerItem : Item {
    override fun areItemsTheSame(newItem: Item) = true

    override fun areContentsTheSame(newItem: Item) = true

    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        @SuppressLint("SetTextI18n")
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val item = BannerGeneralItem(parent).apply {
                setContentView(R.layout.anno_item_banner)
            }
//            val homeItem = HomeItem(parent)
//            val view = RecyclerView(parent.context)
//            view.apply {
//                layoutManager = LinearLayoutManager(parent.context)
//                itemAnimator = DefaultItemAnimator()
//                layoutParams = FrameLayout.LayoutParams(matchParent, matchParent).apply {
//                    horizontalPadding = dip(8)
//                }
//            }
//            homeItem.apply {
//                itemName.text = "校务专区"
//                setContentView(R.layout.anno_item_general_banner)
//                itemContent.visibility = View.VISIBLE
//                setContentView(view)
//            }
//            return MyViewHolder(homeItem.rootView, homeItem, view)
            return MyViewHolder(item.rootView, item)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as MyViewHolder
            holder.homeItem.apply {
                rootView.setOnClickListener {
                    it.context.startActivity<AnnoActivity>()
                }
            }
        }

        private class MyViewHolder(itemView: View, val homeItem: BannerGeneralItem) : RecyclerView.ViewHolder(itemView)

    }
}

fun MutableList<Item>.annoBannerItem() = add(AnnoBannerItem())