package com.twt.service.ecard.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twt.service.ecard.view.TypeOfElse.*
import com.twt.service.ecard.R
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.ui.rec.*
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.startActivity

enum class TypeOfElse {
    TODAY, TITLE, DATE, TIPS, TEXT
}

class EcardElseItem(val text: String, val typeOfElse: TypeOfElse, val block: EcardElseItem.() -> Unit) : Item {
    override val controller: ItemController
        get() = Controller

    override fun areContentsTheSame(newItem: Item): Boolean {
        return text == (newItem as? EcardElseItem)?.text && typeOfElse == (newItem as? EcardElseItem)?.typeOfElse && block == (newItem as? EcardElseItem)?.block
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return text == (newItem as? EcardElseItem)?.text && typeOfElse == (newItem as? EcardElseItem)?.typeOfElse && block == (newItem as? EcardElseItem)?.block
    }

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.ecard_item_else, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as EcardElseItem

            when (item.typeOfElse) {
                TODAY -> showToday(holder, item)
                TITLE -> showTitle(holder, item)
                DATE -> showDate(holder, item)
                TEXT -> showText(holder, item)
                TIPS -> showTip(holder, item)
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val sign: View = itemView.findViewById(R.id.v_else_sign)
            val title: TextView = itemView.findViewById(R.id.tv_else_title)
            val moreImg: ImageView = itemView.findViewById(R.id.iv_else_more)
            val more: TextView = itemView.findViewById(R.id.tv_else_more)
            val date: TextView = itemView.findViewById(R.id.tv_else_date)
            val contentOfText: TextView = itemView.findViewById(R.id.tv_else_content)
            val underLine: View = itemView.findViewById(R.id.v_else_line)
            val tip: TextView = itemView.findViewById(R.id.tv_else_tip)
            val show: TextView = itemView.findViewById(R.id.tv_else_show)
        }

        private fun showTitle(holder: EcardElseItem.Controller.ViewHolder, item: EcardElseItem) {
            holder.apply {
                sign.visibility = View.VISIBLE
                title.visibility = View.VISIBLE
                title.text = item.text
            }
        }

        private fun showToday(holder: EcardElseItem.Controller.ViewHolder, item: EcardElseItem) {
            holder.apply {
                sign.visibility = View.VISIBLE
                moreImg.visibility = View.VISIBLE
                more.visibility = View.VISIBLE
                title.visibility = View.VISIBLE
                title.text = item.text

                more.setOnClickListener { item.block }
                moreImg.setOnClickListener { item.block }
            }
        }

        private fun showDate(holder: EcardElseItem.Controller.ViewHolder, item: EcardElseItem) {
            holder.apply {
                date.visibility = View.VISIBLE
                date.text = item.text
            }
        }

        private fun showText(holder: EcardElseItem.Controller.ViewHolder, item: EcardElseItem) {
            holder.apply {
                underLine.visibility = View.VISIBLE
                contentOfText.visibility = View.VISIBLE
                contentOfText.text = item.text
                contentOfText.setOnClickListener { item.block }
            }
        }

        private fun showTip(holder: EcardElseItem.Controller.ViewHolder, item: EcardElseItem) {
            holder.apply {
                tip.visibility = View.VISIBLE
                tip.text = item.text
                show.visibility = View.VISIBLE
                show.setOnClickListener { item.block }
            }
        }
    }
}

fun MutableList<Item>.ecardElseItem(text: String, typeOfElse: TypeOfElse, block: EcardElseItem.() -> Unit = {}) = add(EcardElseItem(text, typeOfElse, block))