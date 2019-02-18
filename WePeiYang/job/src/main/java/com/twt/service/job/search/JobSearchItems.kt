package com.twt.service.job.search

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twt.service.job.R
import com.twt.service.job.service.InfoOrMeeting
import com.twt.service.job.service.searchHistory
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class SearchHistoryItem(val contentOfSH: String, val fragment: JobSearchHistoryFragment, val search: (String) -> Unit) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            return SearchHolder(parent.context.layoutInflater.inflate(R.layout.job_search_item_history, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as SearchHolder
            item as SearchHistoryItem
            holder.apply {
                contentOfSH.text = item.contentOfSH
                cleaner.setOnClickListener {
                    searchHistory.remove(item.contentOfSH)
                    item.fragment.searchHistory()
                }
                itemView.setOnClickListener { item.search(item.contentOfSH) }
            }
        }
    }

    class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentOfSH: TextView = itemView.findViewById(R.id.job_search_tv_history_keyword)
        val cleaner: ImageView = itemView.findViewById(R.id.job_search_iv_clear_one)
    }

    override val controller: ItemController get() = Controller
}

class ClearAllItem(val clearAll: () -> Unit) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            return ClearHolder(parent.context.layoutInflater.inflate(R.layout.job_search_item_clear, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ClearHolder
            item as ClearAllItem
            holder.clear.setOnClickListener { item.clearAll() }
        }

    }

    class ClearHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clear: ImageView = itemView.findViewById(R.id.job_search_iv_clear_all)
    }

    override val controller: ItemController get() = Controller
}

class InfoItem(val info: InfoOrMeeting, val isFirst: Boolean, val toDetail: (String) -> Unit) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            return InfoHolder(parent.context.layoutInflater.inflate(R.layout.job_search_item_result_info, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as InfoHolder
            item as InfoItem
            holder.apply {
                dateOfInfo.text = item.info.date
                click.text = item.info.click
                titleOfInfo.text = item.info.title
                itemView.setOnClickListener { item.toDetail(item.info.id) }
                divide.visibility = if (item.isFirst) View.GONE else View.VISIBLE
            }
        }
    }

    class InfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateOfInfo: TextView = itemView.findViewById(R.id.job_search_tv_info_date)
        val click: TextView = itemView.findViewById(R.id.job_search_tv_info_click)
        val titleOfInfo: TextView = itemView.findViewById(R.id.job_search_tv_info_title)
        val divide: View = itemView.findViewById(R.id.job_search_v_info_divide)
    }

    override val controller: ItemController get() = Controller
}

class MeetingItem(val info: InfoOrMeeting, val isFirst: Boolean, val toDetail: (Int) -> Unit) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            return MeetingHolder(parent.context.layoutInflater.inflate(R.layout.job_search_item_result_meeting, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as MeetingHolder
            item as MeetingItem
            holder.apply {
                dateOfMeeting.text = item.info.date
                click.text = item.info.click
                titleOfMeeting.text = item.info.title
                held_date.text = item.info.held_date
                held_time.text = item.info.held_time
                placeOfMeeting.text = item.info.place
                divide.visibility = if (item.isFirst) View.GONE else View.VISIBLE
            }
        }
    }

    class MeetingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateOfMeeting: TextView = itemView.findViewById(R.id.job_search_tv_meeting_date)
        val click: TextView = itemView.findViewById(R.id.job_search_tv_meeting_click)
        val titleOfMeeting: TextView = itemView.findViewById(R.id.job_search_tv_meeting_title)
        val held_date: TextView = itemView.findViewById(R.id.job_search_tv_meeting_held_date)
        val held_time: TextView = itemView.findViewById(R.id.job_search_tv_meeting_held_time)
        val placeOfMeeting: TextView = itemView.findViewById(R.id.job_search_tv_meeting_place)
        val divide: View = itemView.findViewById(R.id.job_search_v_meeting_divide)
    }

    override val controller: ItemController get() = Controller
}

fun MutableList<Item>.addSH(contentOfSH: String, fragment: JobSearchHistoryFragment, search: (String) -> Unit) = add(SearchHistoryItem(contentOfSH, fragment, search))
fun MutableList<Item>.addClear(clearAll: () -> Unit) = add(ClearAllItem(clearAll))
fun MutableList<Item>.addInfo(info: InfoOrMeeting, isFirst: Boolean, toDetail: (String) -> Unit) = add(InfoItem(info, isFirst, toDetail))
fun MutableList<Item>.addMeeting(info: InfoOrMeeting, isFirst: Boolean, toDetail: (Int) -> Unit) = add(MeetingItem(info, isFirst, toDetail))