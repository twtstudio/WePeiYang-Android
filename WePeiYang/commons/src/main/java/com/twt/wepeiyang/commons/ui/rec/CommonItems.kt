package com.twt.wepeiyang.commons.ui.rec

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import org.jetbrains.anko.*
import kotlin.properties.Delegates

class SingleTextItem(val text: String, val builder: TextView.() -> Unit = {}) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            var textView: TextView by Delegates.notNull()
            val view = parent.context.frameLayout {
                textView = textView {
                    textSize = 12f
                    textColor = Color.parseColor("#B9B9B9")
                    typeface = Typeface.create("sans-serif-regular", Typeface.NORMAL)
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }.apply {
                layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent).apply {
                    verticalMargin = dip(8)
                }
            }
            return ViewHolder(view, textView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as SingleTextItem
            holder.textView.text = item.text
            holder.textView.apply(item.builder)
        }

        private class ViewHolder(itemView: View?, val textView: TextView) : RecyclerView.ViewHolder(itemView)

    }

    override fun areContentsTheSame(newItem: Item): Boolean = areItemsTheSame(newItem)

    override fun areItemsTheSame(newItem: Item): Boolean {
        if (newItem is SingleTextItem) {
            return newItem.text == text
        } else return false
    }

    override val controller: ItemController
        get() = Controller

}

fun MutableList<Item>.lightText(text: String) = add(SingleTextItem(text))
fun MutableList<Item>.lightText(text: String, builder: TextView.() -> Unit) = add(SingleTextItem(text, builder))
