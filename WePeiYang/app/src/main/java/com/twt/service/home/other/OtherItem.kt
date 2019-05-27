package com.twt.service.home.other

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.twt.service.schedule2.view.exam.ExamTableActivity
import com.tjuwhy.yellowpages2.view.YellowPageActivity
import com.yookiely.lostfond2.waterfall.WaterfallActivity
import com.twt.service.home.user.FragmentActivity
import com.twt.service.job.home.JobHomeActivity
import com.twt.service.mall.MallActivity
import com.twt.service.news.NewsActivity
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.retrox.bike.bike.ui.main.BikeActivity
import com.twtstudio.service.tjwhm.exam.home.ExamHomeActivity
import com.twtstudio.tjliqy.party.ui.home.PartyActivity
import org.jetbrains.anko.*

class OtherItem : Item {
    override fun areItemsTheSame(newItem: Item) = true

    override fun areContentsTheSame(newItem: Item) = true

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val homeItem = HomeItem(parent)
            val view = parent.context.verticalLayout {
                layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent).apply {
                    horizontalPadding = dip(32)
                    topPadding = dip(4)
                    bottomPadding = dip(4)
                }
            }
            homeItem.setContentView(view)
            return ViewHolder(homeItem.rootView, homeItem, view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            holder.homeItem.apply {
                itemName.text = "OTHERS"
                itemContent.text = "功能列表"
                itemContent.setOnClickListener {
                    it.context.startActivity<FragmentActivity>("frag" to "Tool")
                }
            }
            val layout = holder.linearLayout as _LinearLayout
            layout.apply {
                removeAllViews()
                addItem("商城", "天大二手交易平台") {
                    mtaClick("app_首页OtherItem商城Item")
                    it.context.startActivity<MallActivity>()
                }
                addItem("黄页", "天大电话簿") {
                    mtaClick("app_首页OtherItem黄页Item")
                    it.context.startActivity<YellowPageActivity>()
                }
                addItem("刷题", "沉迷学习，日渐头秃") {
                    mtaClick("app_首页OtherItem刷题Item")
                    it.context.startActivity<ExamHomeActivity>()
                }
                addItem("失物招领", "我的心丢了，你看到了吗？") {
                    mtaClick("app_首页OtherItem失物招领Item")
                    it.context.startActivity<WaterfallActivity>()
                }
                addItem("就业", "就业信息，一网打尽") {
                    mtaClick("app_首页OtherItem中就业Item")
                    it.context.startActivity<JobHomeActivity>()
                }
                addItem("新闻", "环宇北洋，心识天下") {
                    mtaClick("app_首页OtherItem中新闻Item")
                    it.context.startActivity<NewsActivity>()
                }
                addItem("学生党建", "和微北洋共建社会主义") {
                    mtaClick("app_首页OtherItem党建Item")
                    it.context.startActivity<PartyActivity>()
                }
                addItem("自行车", "通览状态，畅爽骑行") {
                    mtaClick("app_首页OtherItem自行车Item")
                    it.context.startActivity<BikeActivity>()
                }
                addItem("考表", "一年用两次") {
                    mtaClick("app_首页OtherItem考表Item")
                    it.context.startActivity<ExamTableActivity>()
                }
            }
        }

        private class ViewHolder(itemView: View?, val homeItem: HomeItem, val linearLayout: LinearLayout) : RecyclerView.ViewHolder(itemView)

        fun _LinearLayout.addItem(title: String, message: String, onclick: (View) -> Unit) {
            verticalLayout {
                textView {
                    text = title
                    textSize = 16f
                    textColor = Color.parseColor("#444444")
                }.lparams(matchParent, wrapContent)
                textView {
                    text = message
                    textSize = 12f
                    textColor = Color.parseColor("#444444")
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(4)
                }
                setOnClickListener {
                    onclick(it)
                }
            }.lparams(matchParent, wrapContent) {
                verticalMargin = dip(4)
            }
        }

    }

    override val controller: ItemController
        get() = Controller
}

fun MutableList<Item>.homeOthers() = add(OtherItem())
