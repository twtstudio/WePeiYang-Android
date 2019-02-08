package com.twt.service.job.home

import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.job.R
import com.twt.service.job.service.*
import com.twt.service.job.story.StoryActivity
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

/**
 * 管理所有的item
 */

// "招聘会" 标签下列列表项所包含信息与其他三个标签不一样，且没有置顶项。
class FairItem(val common: HomeDataL, val isFirst: Boolean, val jobFragment: JobFragment) : Item {
    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.job_home_item_one, parent, false)
            return FairViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            val fairItem = item as FairItem
            (holder as FairViewHolder).apply {
                fairTitle.text = fairItem.common.title
                fairHeldDate.text = fairItem.common.held_date
                fairHeldTime.text = fairItem.common.held_time
                fairTextViewPlace.text = fairItem.common.place
                fairTextViewClick.text = fairItem.common.click
                fairTextViewDate.text = fairItem.common.date
                fairDivide.visibility = if (fairItem.isFirst) View.GONE else View.VISIBLE
                fair.setOnClickListener {
                    startActivity(fairItem.common.id,fairItem.jobFragment)
                }
            }
        }

    }

    class FairViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fair = itemView.findViewById<ConstraintLayout>(R.id.job_home_item_one)
        val fairDivide: View = itemView.findViewById(R.id.job_v_one_divide)
        val fairTitle: TextView = itemView.findViewById(R.id.job_tv_one_title)
        val fairHeldDate: TextView = itemView.findViewById(R.id.job_tv_one_held_date)
        val fairHeldTime: TextView = itemView.findViewById(R.id.job_tv_one_held_time)
        val fairTextViewPlace: TextView = itemView.findViewById(R.id.job_tv_one_place)
        val fairTextViewClick: TextView = itemView.findViewById(R.id.job_tv_one_click)
        val fairTextViewDate: TextView = itemView.findViewById(R.id.job_tv_one_date)
    }

    override val controller: ItemController get() = Controller
}

// 剩下三种类型，拿到的数据格式不一样，为了使用统一的Item，并且需要信息不多，所以直接传信息
class ThreeItem(val dataR: HomeDataR, val isFirst: Boolean, val jobFragment: JobFragment) : Item {
    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.job_home_item_three, parent, false)
            return ThreeViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            val threeItem = item as ThreeItem
            (holder as ThreeViewHolder).apply {
                threeTextViewClick.text = threeItem.dataR.click
                threeTitle.text = threeItem.dataR.title
                threeDate.text = threeItem.dataR.date
                threeTop.visibility = if (threeItem.dataR.important == "1") View.VISIBLE else View.GONE
                threeDivide.visibility = if (threeItem.isFirst) View.GONE else View.VISIBLE
                three.setOnClickListener {
                    startActivity(threeItem.dataR.id,threeItem.jobFragment)
                }
            }
            // 本应在此处设置点击事件，但是在 activity 里的 fragment 里的 item 如何实现两个 activity 通信
        }

    }

    class ThreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val three  = itemView.findViewById<ConstraintLayout>(R.id.job_home_item_three)
        val threeDivide: View = itemView.findViewById(R.id.job_v_three_divide)
        val threeTextViewClick: TextView = itemView.findViewById(R.id.job_tv_three_click)
        val threeTop: TextView = itemView.findViewById(R.id.job_tv_three_top)
        val threeTitle: TextView = itemView.findViewById(R.id.job_tv_three_title)
        val threeDate: TextView = itemView.findViewById(R.id.job_tv_three_date)
    }

    override val controller: ItemController get() = Controller
}

private fun startActivity(id: String,fragment: JobFragment) {
    val intent = Intent(fragment.activity,StoryActivity::class.java)
    intent.putExtra(KEY_ID,id)
    intent.putExtra(KEY_KIND,fragment.kind)
    fragment.activity!!.startActivity(intent)
}

fun MutableList<Item>.fair(common: HomeDataL, isFirst: Boolean, jobFragment: JobFragment) = add(FairItem(common, isFirst, jobFragment))
fun MutableList<Item>.three(dataR: HomeDataR, isFirst: Boolean, jobFragment: JobFragment) = add(ThreeItem(dataR, isFirst, jobFragment))
