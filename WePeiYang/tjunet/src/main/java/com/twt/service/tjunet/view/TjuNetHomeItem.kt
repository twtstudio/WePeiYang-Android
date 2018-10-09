package com.twt.service.tjunet.view

import android.arch.lifecycle.LifecycleOwner
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.twt.service.tjunet.R
import com.twt.service.tjunet.viewmodel.TjuNetViewModel
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import kotlinx.android.synthetic.main.tjunet_home_network.view.*
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.startActivity

class TjuNetHomeItem(val lifecycleOwner: LifecycleOwner) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.tjunet_home_network, parent, false)
            val homeItem = HomeItem(parent)
            homeItem.apply {
                itemName.text = "NETWORK"
            }
            homeItem.setContentView(view)
            return ViewHolder(homeItem.rootView, homeItem, view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as TjuNetHomeItem
            val contentView = holder.contentView
            holder.homeItem.apply {
                itemContent.text = "详细设置"
                itemContent.setOnClickListener {
                    it.context.startActivity<TjuNetActivity>()
                }
            }
            TjuNetViewModel.liveStatus.bindNonNull(item.lifecycleOwner) {
                contentView.apply {
                    tv_big_title.text = it.ssid
                    tv_content.text = it.ip
                    color_circle.color = if (it.connected) Color.parseColor("#748165") else Color.parseColor("#dcc373")
                    tv_status.text = it.message
                    btn_action.apply {
                        if (!it.connected) {
                            text = "刷新/登录校园网"
                            setOnClickListener {
                                TjuNetViewModel.refreshNetworkInfo()
                                TjuNetViewModel.login(context)
                            }
                        } else {
                            text = "断开校园网"
                            setOnClickListener {
                                TjuNetViewModel.refreshNetworkInfo()
                                TjuNetViewModel.logout(context)
                            }
                        }
                    }
                }
            }
        }

        private class ViewHolder(itemView: View?, val homeItem: HomeItem, val contentView: View) : RecyclerView.ViewHolder(itemView)
    }

    override val controller: ItemController
        get() = Controller

}

fun MutableList<Item>.homeTjuNetItem(lifecycleOwner: LifecycleOwner) = add(TjuNetHomeItem(lifecycleOwner))