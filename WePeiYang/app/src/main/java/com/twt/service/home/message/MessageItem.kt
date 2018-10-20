package com.twt.service.home.message

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.twt.service.R
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.*

class MessageItem : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val homeItem = HomeItem(parent)
            val view = parent.context.verticalLayout {
                layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent).apply {
                    horizontalPadding = dip(16)
                    topPadding = dip(4)
                    bottomPadding = dip(4)
                }
            }
            homeItem.setContentView(view)
            return ViewHolder(homeItem.rootView, homeItem, view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            holder.homeItem.itemName.text = "MESSAGE"
            val title = MessagePreferences.messageTitle
            val message = MessagePreferences.messageContent
            val layout = holder.linearLayout as _LinearLayout
            layout.apply {
                addItem(title, message) {
                    //实现回调：获取homeItem的itemManager并调用其refreshAll进行对主页item增添删除的刷新
                    val itemManager = ((((holder.itemView).parent) as? RecyclerView)?.adapter as? ItemAdapter)?.itemManager
                    itemManager?.refreshAll(itemManager.filterNot { it is MessageItem })
                }
            }
        }

        private class ViewHolder(itemView: View?, val homeItem: HomeItem, val linearLayout: LinearLayout) : RecyclerView.ViewHolder(itemView)

        //给添加的item添加回调实现拓展点击事件
        private fun _LinearLayout.addItem(title: String, message: String, callBack: () -> Unit = {}) {
            verticalLayout {
                textView {
                    text = title
                    textSize = 16f
                    textColor = Color.parseColor("#000000")
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    topPadding = 4
                    bottomPadding = 6
                }.lparams(wrapContent, wrapContent)
                textView {
                    text = message
                    textSize = 14f
                    textColor = Color.parseColor("#444444")
                    bottomPadding = 10
                }.lparams(wrapContent, wrapContent)
                textView {
                    text = "我知道了"
                    textSize = 16f
                    textColor = resources.getColor(R.color.colorAccent)
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    bottomPadding = 10
                    setOnClickListener {
                        MessagePreferences.isDisplayMessage = false
                        callBack()
                    }
                }.lparams(wrapContent, wrapContent)
            }.lparams(matchParent, wrapContent)
        }

    }

    override val controller: ItemController
        get() = Controller
}

fun MutableList<Item>.homeMessageItem() = add(MessageItem())
