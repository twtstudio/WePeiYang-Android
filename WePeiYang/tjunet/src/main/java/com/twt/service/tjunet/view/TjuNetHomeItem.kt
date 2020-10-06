package com.twt.service.tjunet.view

import android.arch.lifecycle.LifecycleOwner
import android.graphics.Color
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.tjunet.R
import com.twt.service.tjunet.viewmodel.TjuNetViewModel
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import kotlinx.android.synthetic.main.tjunet_home_network.view.*
import org.jetbrains.anko.*
import kotlin.properties.Delegates

class TjuNetHomeItem(val lifecycleOwner: LifecycleOwner) : Item {
    override fun areItemsTheSame(newItem: Item) = true
    override fun areContentsTheSame(newItem: Item) = true

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
//            val view = parent.context.layoutInflater.inflate(R.layout.tjunet_home_network, parent, false)
            val homeItem = HomeItem(parent)
            homeItem.apply {
                itemName.text = "NETWORK"
            }

            var holder: ViewHolder by Delegates.notNull()
            val view = parent.context.verticalLayout {
                gravity = Gravity.CENTER
                val hint = textView {
                    text = "施工中，请耐心等待哦"
                    textSize = 18f
                    typeface = ResourcesCompat.getFont(context, R.font.montserrat_regular)
                    textColor = Color.GRAY
                }.lparams {
                    verticalMargin = dip(6)
                }
                holder = ViewHolder(homeItem.rootView, this, hint)
            }
            homeItem.setContentView(view)
            return holder
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
//            holder as ViewHolder
//            item as TjuNetHomeItem
//            val contentView = holder.contentView
//            holder.homeItem.apply {
//                itemContent.text = "详细设置"
//                itemContent.setOnClickListener {
//                    mtaClick("app_首页上网Item详细设置")
//                    it.context.startActivity<TjuNetActivity>()
//                }
//            }
//            TjuNetViewModel.liveStatus.bindNonNull(item.lifecycleOwner) {
//                contentView.apply {
//                    tv_big_title.text = it.ssid
//                    tv_content.text = it.ip
//                    color_circle.color = if (it.connected) Color.parseColor("#3BCBFF") else Color.parseColor("#FF5D64")
//                    tv_status.text = it.message
//                    btn_action.apply {
//                        if (!it.connected) {
//                            text = "刷新/登录校园网"
//                            setOnClickListener {
//                                TjuNetViewModel.refreshNetworkInfo()
//                                TjuNetViewModel.login(context)
//                            }
//                        } else {
//                            text = "断开校园网"
//                            setOnClickListener {
//                                TjuNetViewModel.refreshNetworkInfo()
//                                TjuNetViewModel.logout(context)
//                            }
//                        }
//                    }
//                }
//            }
        }

        class ViewHolder(itemView: View?, val rootView: View, val hint: TextView) : RecyclerView.ViewHolder(itemView)


//        private class ViewHolder(itemView: View?, val homeItem: HomeItem, val contentView: View) : RecyclerView.ViewHolder(itemView)

    }

    override val controller: ItemController
        get() = Controller

}

fun MutableList<Item>.homeTjuNetItem(lifecycleOwner: LifecycleOwner) = add(TjuNetHomeItem(lifecycleOwner))