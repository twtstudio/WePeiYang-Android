package xyz.rickygao.gpa2.view

import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.service.GpaLiveData
import xyz.rickygao.gpa2.service.GpaPreferences


class GpaHomeItem(val owner: LifecycleOwner) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val item = HomeItem(parent).apply {
                itemName.text = "GPA"
                setContentView(R.layout.gpa2_item_home)
            }
            return GpaItemViewHolder(item.rootView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as GpaItemViewHolder
            item as GpaHomeItem
            holder.bind(item.owner)
        }

        private class GpaItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val cardView: CardView = itemView.findViewById(R.id.card_item_gpa)
            private val scoreTv: TextView = itemView.findViewById(xyz.rickygao.gpa2.R.id.tv_score)
            private val gpaTv: TextView = itemView.findViewById(xyz.rickygao.gpa2.R.id.tv_gpa)
            private val creditTv: TextView = itemView.findViewById(xyz.rickygao.gpa2.R.id.tv_credit)

            fun bind(lifecycleOwner: LifecycleOwner) {

                cardView.setOnClickListener {
                    val intent = Intent(itemView.context, GpaActivity::class.java)
                    itemView.context.startActivity(intent)
                }

                GpaLiveData.bindNonNull(lifecycleOwner) {
                    it.stat.total.let {
                        val fake = "***"
                        if (GpaPreferences.isDisplayGpa) {
                            if (GpaPreferences.isBoomGpa) {
                                scoreTv.text = "100.0"
                                gpaTv.text = "4.0"
                            } else {
                                scoreTv.text = it.score.toString()
                                gpaTv.text = it.gpa.toString()
                            }
                            creditTv.text = it.credit.toString()
                        } else {
                            scoreTv.text = fake
                            gpaTv.text = fake
                            creditTv.text = fake
                        }
                    }
                }

            }
        }

    }

    override val controller: ItemController
        get() = Controller
}

fun MutableList<Item>.gpaHomeItem(owner: LifecycleOwner) = add(GpaHomeItem(owner))



