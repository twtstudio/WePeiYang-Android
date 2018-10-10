package xyz.rickygao.gpa2.view

import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twt.wepeiyang.commons.ui.spanned
import org.jetbrains.anko.layoutInflater
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.service.GpaLiveData
import xyz.rickygao.gpa2.service.Term

/**
 * Created by asus on 2018/5/13.
 */
class GpaItem(val owner: LifecycleOwner) : Item {

    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val item = HomeItem(parent).apply {
                itemName.text = "GPA"
                setContentView(R.layout.card_item_gpa2)
            }
            return GpaItemViewHolder(item.rootView, item)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as GpaItemViewHolder
            item as GpaItem
            holder.bind(item.owner)
            GpaLiveData.bindNonNull(item.owner) {
                val x = it.stat.total.score.toString().substring(0, 4)
                val y = it.stat.total.gpa.toString().substring(0, 4)
                val text = "加权 <span style=\"color:#DBB86B\";>${x}</span>&nbsp; / &nbsp;绩点 <span style=\"color:#DBB86B\";>${y}</span>"
                holder.homeItem.itemContent.text = text.spanned
            }


        }

        private class GpaItemViewHolder(itemView: View, val homeItem: HomeItem) : RecyclerView.ViewHolder(itemView) {
            private val cardView: CardView = itemView.findViewById(R.id.card_item_gpa2)
            //            val textView : TextView = itemView.findViewById(R.id.gpa2_text)
            val gpaMiniLineChartView: GpaMiniLineChartView = itemView.findViewById(R.id.gpa_mini)

            fun bind(lifecycleOwner: LifecycleOwner) {
                cardView.setOnClickListener {
                    val intent = Intent(itemView.context, GpaActivity::class.java)
                    itemView.context.startActivity(intent)
                }
                GpaLiveData.bindNonNull(lifecycleOwner) {


                    it.data.asSequence().map {
                        GpaMiniLineChartView.DataWithDetail(it.stat.gpa, it.name)
                    }.toMutableList().let {
                        gpaMiniLineChartView.dataWithDetail = it
                    }
//
//                it.data.asSequence().map(Term::stat).map {
//                    GpaMiniLineChartView.DataWithDetail(it.score)
//                }.toMutableList().let {
//                    holder.gpaMiniLineChartView.dataWithDetail = it
//                }

                    // attempt to refresh chart view while new data coming


                }
            }
        }

    }

    override val controller: ItemController
        get() = Controller


}


fun MutableList<Item>.gpaNewHomeItem(owner: LifecycleOwner) = add(GpaItem(owner))
