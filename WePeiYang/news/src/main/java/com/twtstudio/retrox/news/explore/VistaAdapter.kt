package com.twtstudio.retrox.news.explore

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.twtstudio.retrox.news.R
import com.twtstudio.retrox.news.api.bean.FangcunBean

/**
 * Created by retrox on 09/04/2017.
 */
class VistaAdapter(val context: Context, val layoutHelper: LayoutHelper, val list: MutableList<FangcunBean.DataBean> = ArrayList()) : DelegateAdapter.Adapter<VistaAdapter.VistaHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VistaHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_explore_vista, parent, false)
        return VistaHolder(view)
    }

    override fun onBindViewHolder(holder: VistaHolder?, position: Int) {
        val data = list[position]
        holder?.apply {
            title.text = data.name
            content.text = data.desc.replace("\n",",")
            val url = "https://photograph.twtstudio.com/upload/" + data.cover
            Logger.d(url)
            Glide.with(context).load(url).crossFade().into(image)
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return layoutHelper
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    fun refreshData(list: List<FangcunBean.DataBean>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class VistaHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val image = itemView?.findViewById(R.id.cover_image) as ImageView
        val title = itemView?.findViewById(R.id.text_title) as TextView
        val content = itemView?.findViewById(R.id.text_content) as TextView
    }
}