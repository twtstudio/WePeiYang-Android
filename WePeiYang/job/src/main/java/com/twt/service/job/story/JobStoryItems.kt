package com.twt.service.job.story

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import com.twt.service.job.R
import com.twt.service.job.service.NoticeAfter
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class TopItem(val notice: NoticeAfter?, val isDivide: Boolean) : Item {

    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.job_story_item_top, parent, false)
            return TopHolder(view)
        }

        @SuppressLint("SetJavaScriptEnabled")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            val notice = (item as TopItem).notice
            (holder as TopHolder).apply {
                titleTop.text = notice?.title
                dateTop.text = notice?.date
                clickTop.text = notice?.click.toString()
                contentTop.loadData(notice?.content?.getHtmlData(), "text/html", "UTF-8")
                val webViewSetting = contentTop.settings
                webViewSetting.apply {
                    loadsImagesAutomatically = true
                    supportZoom()
                    javaScriptEnabled = true
                }
                divide.visibility = if (item.isDivide) View.VISIBLE else View.GONE
            }
        }

        private fun String.getHtmlData(): String {
            val head = "<head><style> " +
                    "img {max-width: 100% !important;height: auto !important;}" +
                    "* {color: #222222;background-color: rgb(242,242,242);line-height: 1.6;}" +
                    "</style></head>"
            return "<html>$head<body>$this</body></html>"
        }
    }

    class TopHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTop: TextView = itemView.findViewById(R.id.job_story_tv_title)
        val dateTop: TextView = itemView.findViewById(R.id.job_story_tv_date)
        val clickTop: TextView = itemView.findViewById(R.id.job_story_tv_click)
        val contentTop: WebView = itemView.findViewById(R.id.job_story_tv_content)
        val divide: View = itemView.findViewById(R.id.job_story_v_divide)
    }


    override val controller: ItemController get() = Controller
}

class PDItem(val dateTime: String, val place: String) : Item {
    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.job_story_item_place_date, parent, false)
            return PDHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as PDItem
            (holder as PDHolder).apply {
                dateTime.text = item.dateTime
                place.text = item.place
            }
        }
    }

    class PDHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTime: TextView = itemView.findViewById(R.id.job_story_tv_date)
        val place: TextView = itemView.findViewById(R.id.job_story_tv_place)
    }

    override val controller: ItemController get() = Controller
}

class AttachItem(val attach_name: String, val attach: String, val storyActivity: StoryActivity) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.job_story_item_attach, parent, false)
            return AttachHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as AttachItem
            (holder as AttachHolder).apply {
                attach_name.text = item.attach_name
                itemConstraintLayout.setOnClickListener {
                    val uri: Uri = Uri.parse(item.attach)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    item.storyActivity.startActivity(intent)
                }
            }
        }

    }

    class AttachHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val attach_name: TextView = itemView.findViewById(R.id.job_story_tv_attach)
        val itemConstraintLayout: ConstraintLayout = itemView.findViewById(R.id.job_story_item_attach)
    }

    override val controller: ItemController get() = Controller
}

fun MutableList<Item>.top(notice: NoticeAfter, isDivide: Boolean) = add(TopItem(notice, isDivide))
