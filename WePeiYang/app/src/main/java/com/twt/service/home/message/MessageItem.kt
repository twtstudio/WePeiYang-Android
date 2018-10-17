package com.twt.service.home.message

import android.arch.lifecycle.LifecycleOwner
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.*

class MessageItem (val owner: LifecycleOwner): Item {
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
            launch(UI + QuietCoroutineExceptionHandler) {

                val messageBean = MessageService.getMessage().await()
                if (messageBean.error_code == -1) {
                    Log.d("fucker", messageBean.toString())

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
                addItem(title, message)
            }
        }

        private class ViewHolder(itemView: View?, val homeItem: HomeItem, val linearLayout: LinearLayout) : RecyclerView.ViewHolder(itemView)


        fun _LinearLayout.addItem(title: String, message: String) {
            verticalLayout {
                textView {
                    text = title
                    textSize = 16f
                    textColor = Color.parseColor("#000000")
//                this.paint.isFakeBoldText
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
                        MessagePreferences.isDisplayMessage=true
                    }
                }.lparams(wrapContent, wrapContent)
            }.lparams(matchParent, wrapContent)
        }

    }

    override val controller: ItemController
        get() = Controller
}

fun MutableList<Item>.homeMessageItem(owner: LifecycleOwner) = add(MessageItem(owner))