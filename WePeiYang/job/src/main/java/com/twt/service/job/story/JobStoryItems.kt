package com.twt.service.job.story

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.job.R
import com.twt.service.job.service.Notice
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class TopItem(val notice: Notice, val isDivide: Boolean) : Item {

    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.job_story_item_top, parent, false)
            return TopHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            val notice = (item as TopItem).notice
            (holder as TopHolder).apply {
                titleTop.text = notice.title
                dateTop.text = notice.date
                clickTop.text = notice.click.toString()
                contentTop.text = notice.content
                divide.visibility = if (item.isDivide) View.VISIBLE else View.GONE
            }
        }

    }

    class TopHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTop: TextView = itemView.findViewById(R.id.job_story_tv_title)
        val dateTop: TextView = itemView.findViewById(R.id.job_story_tv_date)
        val clickTop: TextView = itemView.findViewById(R.id.job_story_tv_click)
        val contentTop: TextView = itemView.findViewById(R.id.job_story_tv_content)
        val divide: View = itemView.findViewById(R.id.job_story_v_divide)
    }

    override val controller: ItemController get() = Controller
}

class PDItem(val dateTime: String, val place: String) : Item {
    private companion object Cotroller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.job_story_item_place_date, parent, false)
            return PDHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            val pdItem = item as PDItem
            (holder as PDHolder).apply {
                dateTime.text = pdItem.dateTime
                place.text = pdItem.place
            }
        }
    }

    class PDHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTime: TextView = itemView.findViewById(R.id.job_story_tv_date)
        val place: TextView = itemView.findViewById(R.id.job_story_tv_place)
    }

    override val controller: ItemController get() = Cotroller
}

class AttachItem(val attach_name: String, val attach: String,val storyActivity: StoryActivity) : Item {
    companion object Cotroller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.job_story_item_place_date,parent,false)
            return AttachHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            val attachItem = item as AttachItem
            (holder as AttachHolder).apply {
                attach_name.text = attachItem.attach_name
                val uri: Uri = Uri.parse(attachItem.attach)
                val intent = Intent(Intent.ACTION_VIEW,uri)
                attachItem.storyActivity.startActivity(intent)
            }
        }

    }

    class AttachHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val attach_name : TextView = itemView.findViewById(R.id.job_story_tv_attach)
    }

    override val controller: ItemController get() = Cotroller
}