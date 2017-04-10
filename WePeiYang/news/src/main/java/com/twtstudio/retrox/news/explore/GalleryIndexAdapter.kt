package com.twtstudio.retrox.news.explore

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.bumptech.glide.Glide
import com.twtstudio.retrox.news.R
import com.twtstudio.retrox.news.api.bean.FangcunBean
import com.twtstudio.retrox.news.api.bean.GalleryIndexBean
import com.twtstudio.retrox.news.explore.gallery.GalleryActivity

/**
 * Created by retrox on 09/04/2017.
 */
class GalleryIndexAdapter(val context: Context, val layoutHelper: LayoutHelper, val list: MutableList<GalleryIndexBean> = ArrayList()) : DelegateAdapter.Adapter<GalleryIndexAdapter.GalleryIndexHolder>(){

    override fun onBindViewHolder(holder: GalleryIndexHolder?, position: Int) {
        val data = list[position]
        holder?.apply {
            Glide.with(context).load(data.coverThumbnailUrl).into(image)
            title.text = data.title
            image.setOnClickListener {
                val intent = Intent(context,GalleryActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GalleryIndexHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_explore_gallery_index,parent,false)
        return GalleryIndexHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return layoutHelper
    }

    override fun getItemViewType(position: Int): Int {
        return 4
    }

    fun refreshData(list: List<GalleryIndexBean>){
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    class GalleryIndexHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val image = itemView?.findViewById(R.id.image) as ImageView
        val title = itemView?.findViewById(R.id.text_gallery_title) as TextView
    }
}