package com.twt.service.ecard.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import com.twt.service.ecard.R
import com.twt.service.ecard.model.EcardLineChartDataBean
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class EcardChartItem(val lineChartDataList: List<EcardLineChartDataBean>) : Item {
    override val controller: ItemController
        get() = Controller

    override fun areContentsTheSame(newItem: Item): Boolean {
        return false
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return false
    }

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.ecard_item_chart, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as EcardChartItem
            val lineChart = holder.chartView
            lineChart.distanceOfBegin = 0.0
            lineChart.viewHolder = holder
            item.lineChartDataList.asSequence().map {
                EcardChartView.DataWithDetail(it.count.toDouble(), "${it.date.substring(4, 6)}/${it.date.substring(6, 8)}")
            }.toList().let {
                val dataWithDetail = it.toMutableList()
                dataWithDetail.reverse()
                holder.month.text = "${dataWithDetail.first().year.split("/")[0].toInt()}天"
                dataWithDetail.add(0, EcardChartView.DataWithDetail((dataWithDetail[0].data * 2 / 3), " "))
                dataWithDetail.add(EcardChartView.DataWithDetail((dataWithDetail.last().data * 4 / 3), " "))
                lineChart.dataWithDetail = dataWithDetail
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val chartView: EcardChartView = itemView.findViewById(R.id.ecv_chart)
            val month: TextView = itemView.findViewById(R.id.tv_chart_month)
        }
    }
}

fun MutableList<Item>.ecardChartItem(lineChartDataList: List<EcardLineChartDataBean>) = add(EcardChartItem(lineChartDataList))