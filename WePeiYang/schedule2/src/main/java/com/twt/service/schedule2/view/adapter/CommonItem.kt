package com.twt.service.schedule2.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twt.service.schedule2.R
import org.jetbrains.anko.layoutInflater

class IndicatorTextItem(val text: String) : Item {
    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.schedule_item_indicator, parent, false)
            return IndicatorTextViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as IndicatorTextViewHolder
            item as IndicatorTextItem
            holder.indicatorTextView.text = item.text
        }

        private class IndicatorTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val indicatorTextView: TextView = itemView.findViewById(R.id.tv_course_indicator)
        }

    }

    override val controller: ItemController get() = Controller
}

fun MutableList<Item>.indicatorText(text: String) = add(IndicatorTextItem(text))

class IconLabelItem(val model: CourseDetailViewModel) : Item {
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.schedule_item_detail, parent, false)
            return IconLabelViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as IconLabelViewHolder
            item as IconLabelItem
            val viewModel = item.model
            holder.apply {
                iconImageView.setImageResource(viewModel.imgResId)
                detailTextView.text = viewModel.content
                container.setOnClickListener {
                    viewModel.clickBlock.invoke(it)
                }
            }
        }

        private class IconLabelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val iconImageView: ImageView = itemView.findViewById(R.id.iv_item_detail)
            val detailTextView: TextView = itemView.findViewById(R.id.tv_item_detail)
            val container: ViewGroup = itemView.findViewById(R.id.container_item_detail)
        }

    }

    override val controller: ItemController get() = Controller
}

fun MutableList<Item>.iconLabel(model: CourseDetailViewModel) = add(IconLabelItem(model))

fun MutableList<Item>.iconLabel(init: CourseDetailViewModel.() -> Unit) = iconLabel(CourseDetailViewModel().apply(init))

data class CourseDetailViewModel(var imgResId: Int = 0, var content: String = "", var clickBlock: (View) -> Unit = {})