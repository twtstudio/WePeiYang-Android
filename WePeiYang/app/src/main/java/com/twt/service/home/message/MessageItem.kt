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
                    // todo Oct.20th 18 by tjwhm: Log 和注释中的单词也应该好好写，驼峰还是下划线之类的，
                    // todo                       否则会有拼写检查提示，很难受，顺便修改一下注释中的错别字 (回调)。
                    // todo                       行数 43 44 63 70
                    Log.d("messagedetail", messageBean.toString())
                    //通过新的网请和本地的isdisplayMessage判断来实现Message是否显示
                    // todo Oct.20th 18 by tjwhm: 这里逻辑有问题，现在新通知和本地缓存的通知是否一致的判断在 messageItem 的 onBindViewHolder() 里面，
                    // todo                       然而如果通知有变化的话，isDisplay 是 false 不会加载 messageItem ，也就不会调用 onBindViewHolder() 方法，
                    // todo                       所以现在如果通知有变动的话还是显示不出来。
                    // todo                       应该将判断放在 Activity 里，来保证每次打开微北洋都会执行判断。
                    if (!MessagePreferences.isDisplayMessage && MessagePreferences.messageTitle != messageBean.data!!.title) {
                        MessagePreferences.isDisplayMessage = true
                    }
                    // todo Oct.20th 18 by tjwhm: 没有进行非空判断，data 不能直接!!，这样在后台不返回数据的时候 (比如服务器崩溃) 微北洋首页也会闪退
                    // todo                       应进行判断后再使用 !!
                    //获取item的内容
                    MessagePreferences.messageTitle = messageBean.data!!.title
                    MessagePreferences.messageContent = messageBean.data!!.message
                }
            }
            // todo Oct.20th 18 by tjwhm: 单个语句就不要 apply 了，直接执行就好
            holder.homeItem.apply {
                itemName.text = "MESSAGE"
            }
            val title = MessagePreferences.messageTitle
            val message = MessagePreferences.messageContent
            val layout = holder.linearLayout as _LinearLayout
            layout.apply {
                addItem(title, message) {
                    val itemManager = ((((holder.itemView).parent) as? RecyclerView)?.adapter as? ItemAdapter)?.itemManager
                    itemManager?.refreshAll(itemManager.filterNot { it is MessageItem }) //实现回掉：获取homeItem的itemManager并调用其refreshall进行对主页item增添删除的刷新
                }
            }
        }

        private class ViewHolder(itemView: View?, val homeItem: HomeItem, val linearLayout: LinearLayout) : RecyclerView.ViewHolder(itemView)

        //给添加的item添加回掉实现拓展点击事件
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
                    // todo Oct.20th 18 by tjwhm: 下面这个 color 应改为 colorAccent (参见 app 模块的 colors.xml)
                    textColor = Color.parseColor("#00A1E9")
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
