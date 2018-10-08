package com.example.news2

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.bind
import com.zhouwei.mzbanner.MZBannerView
import com.zhouwei.mzbanner.holder.MZViewHolder

class NewsAdapter(val context: Context, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<RecyclerView.ViewHolder?>(), View.OnClickListener {
    companion object {
        const val IMAGE = 1
        const val NO_IMAGE = 2
        const val BANNER = 3
    }

    val list: MutableList<NewsListBean> = mutableListOf()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == BANNER -> {
                holder as MyBannerViewHolder
                newsBannerLiveData.bind(lifecycleOwner) {
                    Log.e("Test1", it.toString())
                    holder.banner.setPages(it?.carousel) { BannerViewHolder() }
                    holder.banner.setBannerPageClickListener { _, p1 ->
                        val intent = Intent(context, WebActivity::class.java)
                        intent.putExtra("bannerUrl", it?.carousel?.get(p1)?.index)
                        context.startActivity(intent)
                    }
                }
            }
            getItemViewType(position) == NO_IMAGE -> {
                holder as NoImageViewHolder
                holder.title.text = list[position].subject
                val tp = holder.title.paint
                tp.isFakeBoldText = true
                holder.comments.text = list[position].comments.toString()
                holder.visitCount.text = list[position].visitcount
                holder.itemView.tag = list[position].index
            }
            else -> {
                val newsIndex = position - 1
                holder as ImageViewHolder
                holder.title.text = list[newsIndex].subject
                val tp = holder.title.paint
                tp.isFakeBoldText = true
                holder.comments.text = list[newsIndex].comments.toString()
                holder.visitCount.text = list[newsIndex].visitcount
                if (list[newsIndex].pic[0] == '/') {

                    Glide.with(context)
                            .load("https://news.twt.edu.cn${list[newsIndex].pic}")
                            .centerCrop()
                            .error(R.drawable.ic_news_error_small)
                            .into(holder.image)
                } else {
                    Glide.with(context)
                            .load(list[newsIndex].pic)
                            .centerCrop()
                            .error(R.drawable.ic_news_error_small)
                            .into(holder.image)

                }
                holder.itemView.tag = list[newsIndex].index
            }
        }

    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolderByViewType(viewType, parent) // NewsItemViewHolder(context, view)
    }

    override fun getItemViewType(position: Int): Int {
        val newsIndex = position - 1
        return when {
            position == 0 -> BANNER
            list[newsIndex].pic == "" -> NO_IMAGE
            list[newsIndex].pic != "" -> IMAGE
            else -> 4
        }
    }

    private fun getViewHolderByViewType(viewType: Int, parent: ViewGroup?): RecyclerView.ViewHolder {
        var holder: RecyclerView.ViewHolder? = null
        val viewNoImageLayout: View = LayoutInflater.from(context).inflate(R.layout.news_noimage_layout, parent, false)
        val viewImageLayout: View = LayoutInflater.from(context).inflate(R.layout.news_image_layout, parent, false)
        val bannerLayout: View = LayoutInflater.from(context).inflate(R.layout.news_banner, parent, false)
        viewImageLayout.setOnClickListener(this)
        viewNoImageLayout.setOnClickListener(this)

        when (viewType) {
            IMAGE -> holder = ImageViewHolder(viewImageLayout)
            BANNER -> holder = MyBannerViewHolder(bannerLayout)
            NO_IMAGE -> holder = NoImageViewHolder(viewNoImageLayout)
        }
        return holder!!
    }

    override fun onClick(v: View) {
        val intent = Intent(context, WebActivity::class.java)
        intent.putExtra("recyclerViewUrl", v.tag as Int)
        context.startActivity(intent)
    }

    class NoImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.news_noimage_title)
        var visitCount: TextView = itemView.findViewById(R.id.news_noimage_visitcount)
        var comments: TextView = itemView.findViewById(R.id.news_noimage_commentscount)
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.news_image_image)
        var title: TextView = itemView.findViewById(R.id.news_image_title)
        var visitCount: TextView = itemView.findViewById(R.id.news_image_visitcount)
        var comments: TextView = itemView.findViewById(R.id.news_image_commentscount)
    }

    class MyBannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 这个是用于recyclerView的Banner的ViewHolder
        var banner: MZBannerView<Carousel> = itemView.findViewById(R.id.news_banner)
    }

    class BannerViewHolder : MZViewHolder<Carousel> {
        private lateinit var myContext: Context


        // 这个是用于banner里面的每个界面的viewHolder
        lateinit var title: TextView
        lateinit var image: ImageView
        lateinit var view: View
        override fun onBind(context: Context?, int: Int, carousel: Carousel) {
            if (context != null) {
                view.tag = carousel.index
                view.setOnClickListener {
                    val intent = Intent(myContext, WebActivity::class.java)
                    intent.putExtra("bannerUrl", view.tag as String)
                    myContext.startActivity(intent)
                }
                title.tag = carousel.index
                title.text = carousel.subject
                title.setOnClickListener {
                    val intent = Intent(myContext, WebActivity::class.java)
                    intent.putExtra("bannerUrl", view.tag as String)
                    myContext.startActivity(intent)
                }
                val tp = title.paint
                tp.isFakeBoldText = true

                Glide.with(context)
                        .load(carousel.pic)
                        .centerCrop()
                        .error(R.drawable.ic_news_error_big)
                        .into(image)
            }
        }

        override fun createView(context: Context?): View {
            if (context != null) {
                myContext = context
                view = LayoutInflater.from(context).inflate(R.layout.news_banner_item, null, false)
                title = view.findViewById(R.id.news_banner_item_title)
                image = view.findViewById(R.id.news_banner_item_image)
            }
            return view
        }
    }
}