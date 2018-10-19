package com.twt.service.home.message

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemController
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
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
            launch(UI + QuietCoroutineExceptionHandler) {
                val messageBean = MessageService.getMessage().await()
                if (messageBean.error_code == -1) {
                    Log.d("messagedetail", messageBean.toString())
                    //通过新的网请和本地的isdisplayMessage判断来实现Message是否显示
                    if(!MessagePreferences.isDisplayMessage && MessagePreferences.messageTitle != messageBean.data!!.title){
                        MessagePreferences.isDisplayMessage = true
                    }
                    //获取item的内容
                        MessagePreferences.messageTitle = messageBean.data!!.title
                        MessagePreferences.messageContent = messageBean.data!!.message
                }
            }
            holder.homeItem.apply {
                itemName.text = "MESSAGE"
            }
            val title = MessagePreferences.messageTitle
            val message = MessagePreferences.messageContent
            val layout = holder.linearLayout as _LinearLayout
            layout.apply {
                addItem(title, message){
                    ((((holder.itemView).parent) as? RecyclerView)?.adapter as? ItemAdapter)?.remove(item) //实现回掉：获取homeItem的adapter进行对主页item的移除和添加item
                }
            }
        }

        private class ViewHolder(itemView: View?, val homeItem: HomeItem, val linearLayout: LinearLayout) : RecyclerView.ViewHolder(itemView)


        fun _LinearLayout.addItem(title: String, message: String,callBack:()->Unit={}) {
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
                    textColor = Color.parseColor("#00A1E9")
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    bottomPadding = 10
                    setOnClickListener{
                        MessagePreferences.isDisplayMessage=false
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