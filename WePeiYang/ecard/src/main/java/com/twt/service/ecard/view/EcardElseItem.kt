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
    TITLE, DATE, TIPS, TEXT
}

class EcardElseItem(val text: String, val typeOfElse: TypeOfElse) : Item {
    override val controller: ItemController
        get() = Controller

    override fun areContentsTheSame(newItem: Item): Boolean {
        return text == (newItem as? EcardElseItem)?.text && typeOfElse == (newItem as? EcardElseItem)?.typeOfElse
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return text == (newItem as? EcardElseItem)?.text && typeOfElse == (newItem as? EcardElseItem)?.typeOfElse
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
                TITLE -> showTitle(holder, item)
                DATE ->
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val sign: View = itemView.findViewById(R.id.v_else_sign)
            val title: TextView = itemView.findViewById(R.id.tv_else_title)
            val moreImg: ImageView = itemView.findViewById(R.id.iv_else_more)
            val more: TextView = itemView.findViewById(R.id.tv_else_more)
        }
    }
}

private fun showTitle(holder: EcardElseItem.Controller.ViewHolder, item: EcardElseItem) {
    holder.apply {
        sign.visibility = View.VISIBLE
        moreImg.visibility = View.VISIBLE
        more.visibility = View.VISIBLE
        title.visibility = View.VISIBLE
        title.text = item.text

        more.setOnClickListener { view ->
            view.context.startActivity<EcardPreviousActivity>()
            mtaClick("ecard_用户查看消费流水详情")
        }
        moreImg.setOnClickListener { view ->
            view.context.startActivity<EcardPreviousActivity>()
            mtaClick("ecard_用户查看消费流水详情")
        }
    }
}

pri