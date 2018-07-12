package com.twt.service.home.other

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.twt.service.home.user.FragmentActivity
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.retrox.bike.bike.ui.main.BikeActivity
import com.twtstudio.tjliqy.party.ui.home.PartyActivity
import org.jetbrains.anko.*

class OtherItem : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val homeItem = HomeItem(parent)
            val view = parent.context.verticalLayout {
                layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent).apply {
                    horizontalPadding = dip(32)
                    topPadding = dip(4)
                    bottomPadding = dip(4)
                }
            }
            homeItem.setContentView(view)
            return ViewHolder(homeItem.rootView, homeItem, view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            holder.homeItem.apply {
                itemName.text = "OTHERS"
                itemContent.text = "功能列表"
                itemContent.setOnClickListener {
                    it.context.startActivity<FragmentActivity>("frag" to "Tool")
                }
            }
            val layout = holder.linearLayout as _LinearLayout
            layout.apply {
                addItem("学生党建", "微北洋党建模块") {
                    it.context.startActivity<PartyActivity>()
                }
                addItem("自行车", "进入自行车功能") {
                    it.context.startActivity<BikeActivity>()
                }
                addItem("新闻", "查看天外天新闻") {
                    it.context.startActivity<FragmentActivity>("frag" to "News")
                }
            }
        }

        private class ViewHolder(itemView: View?, val homeItem: HomeItem, val linearLayout: LinearLayout) : RecyclerView.ViewHolder(itemView)

        fun _LinearLayout.addItem(title: String, message: String, onclick: (View) -> Unit) {
            verticalLayout {
                textView {
                    text = title
                    textSize = 16f
                    textColor = Color.parseColor("#444444")
                    typeface = Typeface.create("sans-serif-regular", Typeface.NORMAL)
                }.lparams(matchParent, wrapContent)
                textView {
                    text = message
                    textSize = 12f
                    textColor = Color.parseColor("#444444")
                    typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(4)
                }
                setOnClickListener {
                    onclick(it)
                }
            }.lparams(matchParent, wrapContent) {
                verticalMargin = dip(4)
            }
        }

    }

    override val controller: ItemController
        get() = Controller

}

fun MutableList<Item>.homeOthers() = add(OtherItem())