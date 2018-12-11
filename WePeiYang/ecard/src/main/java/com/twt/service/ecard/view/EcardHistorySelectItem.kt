package com.twt.service.ecard.view

import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.twt.service.ecard.R
import com.twt.service.ecard.model.EcardPref
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class EcardHistorySelectItem(val selectionList: List<String>, val historySelectCallback: (day: Int) -> Unit) : Item {
    override val controller: ItemController
        get() = Controller

    override fun areContentsTheSame(newItem: Item): Boolean = true
    override fun areItemsTheSame(newItem: Item): Boolean = true

    companion object Controller: ItemController{
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.ecard_item_histroy_select, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as EcardHistorySelectItem
            val lengthList = item.selectionList
            val spinner = holder.spinner
            spinner.adapter = ArrayAdapter(holder.spinner.context, android.R.layout.simple_spinner_dropdown_item, lengthList)
            spinner.background.setColorFilter(Color.parseColor("#568FFF"), PorterDuff.Mode.SRC_ATOP)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    EcardPref.ecardHistorySpinnerIndex = position
                    try {
                        item.historySelectCallback.invoke(position)
                    } catch (e: Exception) {
                        e.printStackTrace() // 避免越界
                    }
                }
            }
            try {
                spinner.setSelection(EcardPref.ecardHistorySpinnerIndex, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val spinner: Spinner = itemView.findViewById(R.id.spinner_history_select)
        }
    }
}

fun MutableList<Item>.ecardHistorySelectItem(selectionList: List<String>, historySelectCallback: (day: Int) -> Unit) = add(EcardHistorySelectItem(selectionList, historySelectCallback))