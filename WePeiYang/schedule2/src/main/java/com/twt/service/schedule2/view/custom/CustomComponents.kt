package com.twt.service.schedule2.view.custom

import android.graphics.Color
import android.support.constraint.ConstraintLayout.LayoutParams.PARENT_ID
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.view.adapter.Item
import com.twt.service.schedule2.view.adapter.ItemController
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout

class SingleTextItem(val text: String) : Item {

    var builder: (TextView.() -> Unit)? = null

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            lateinit var textView: TextView
            val view = parent.context.constraintLayout {
                textView = textView {
                    text = "课程表设置"
                    id = View.generateViewId()
                    textSize = 16f
                    textColor = Color.BLACK
                }.lparams(width = wrapContent, height = wrapContent) {
                    startToStart = PARENT_ID
                    topToTop = PARENT_ID
                    bottomToBottom = PARENT_ID
                    margin = dip(16)
                }
                imageView {
                    backgroundColor = getColorCompat(R.color.common_lv4_divider)
                }.lparams(width = matchParent, height = dip(1)) {
                    bottomToBottom = PARENT_ID
                }
            }.apply {
                layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
            }
            return ViewHolder(view, textView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as SingleTextItem
            holder as ViewHolder
            holder.textView.text = item.text
            item.builder?.invoke(holder.textView)
        }

        class ViewHolder(itemView: View?, val textView: TextView) : RecyclerView.ViewHolder(itemView)

    }

    override val controller: ItemController = Controller

    override fun areItemsTheSame(newItem: Item): Boolean = newItem is SingleTextItem

    override fun areContentsTheSame(newItem: Item): Boolean {
        if (newItem !is SingleTextItem) return false
        return newItem.text == this.text
    }
}

fun MutableList<Item>.singleText(text: String) = add(SingleTextItem(text))
fun MutableList<Item>.singleText(text: String, builder: (TextView.() -> Unit)) = add(SingleTextItem(text).apply { this.builder = builder })
