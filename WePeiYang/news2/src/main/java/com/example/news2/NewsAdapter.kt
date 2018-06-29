package com.example.news2

import android.annotation.SuppressLint
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
import com.zhouwei.mzbanner.holder.MZHolderCreator
import com.zhouwei.mzbanner.holder.MZViewHolder

class NewsAdapter(val context: Context, val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<RecyclerView.ViewHolder?>(), View.OnClickListener {
    companion object {
        val IMAGE = 1
        val NOIMAGE = 2
        val BANNER = 3
    }

    val list: MutableList<RecyclerViewData> = mutableListOf()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            holder as MyBannerViewHolder
            newsBannerLiveData.bind(lifecycleOwner) {
                Log.e("Test1", it.toString())
                holder.banner.setPages(it?.carousel, object : MZHolderCreator<BannerViewHolder> {
                    override fun createViewHolder(): BannerViewHolder {
                        return BannerViewHolder()
                    }
                })
                holder.banner.setBannerPageClickListener(object : MZBannerView.BannerPageClickListener {
                    override fun onPageClick(p0: View?, p1: Int) {
                        var intent = Intent(context, WebActivity::class.java)
                        intent.putExtra("bannerUrl", it!!.carousel[p1].index)
                        context.startActivity(intent)
                    }
                })
            }
        } else {
            if (list[position].pic == "" || list[position].pic[0] == '/') {
                holder as NoImageViewHolder
                holder.title.text = list[position].subject
                val tp = holder.title.getPaint()
                tp.setFakeBoldText(true)
                holder.comments.setText(list[position].comments.toString())
                holder.visitCount.text = list[position].visitcount
                holder.itemView.setTag(list[position].index)
            } else {
                holder as ImageViewHolder
                holder.title.text = list[position].subject
                val tp = holder.title.getPaint()
                tp.setFakeBoldText(true)
                holder.comments.setText(list[position].comments.toString())
                holder.visitCount.text = list[position].visitcount
                Glide.with(context).load(list[position].pic).centerCrop().error(R.drawable.ic_news_error_small).into(holder.image)
                holder.itemView.setTag(list[position].index)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //val view: View = LayoutInflater.from(context).inflate(R.layout.news_noimage_layout, parent, false)
        return getViewHolderByViewType(viewType, parent)//NewsItemViewHolder(context, view)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return BANNER
        } else if (list[position].pic == "" || list[position].pic[0] == '/') {
            return NOIMAGE
        } else if (list[position].pic != "") {
            return IMAGE
        } else {
            return 4
        }
    }

    @SuppressLint("ResourceType")
    private fun getViewHolderByViewType(viewType: Int, parent: ViewGroup?): RecyclerView.ViewHolder {
        var holder: RecyclerView.ViewHolder? = null
        val viewNoImageLayout: View = LayoutInflater.from(context).inflate(R.layout.news_noimage_layout, parent, false)
        val viewImageLayout: View = LayoutInflater.from(context).inflate(R.layout.news_image_layout, parent, false)
        val bannerLayout: View = LayoutInflater.from(context).inflate(R.layout.news_banner, parent, false)
        viewImageLayout.setOnClickListener(this)
        viewNoImageLayout.setOnClickListener(this)
        if (viewType == IMAGE) {
            holder = ImageViewHolder(viewImageLayout)
        } else if (viewType == BANNER) {
            holder = MyBannerViewHolder(bannerLayout)
        } else if (viewType == NOIMAGE) {
            holder = NoImageViewHolder(viewNoImageLayout)
        }
        return holder!!
    }

    override fun onClick(v: View) {
        val intent = Intent(context, WebActivity::class.java)
        intent.putExtra("recyclerViewUrl", v.getTag() as Int)
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
        //这个是用于recyclerView的Banner的ViewHolder
        var banner: MZBannerView<Carousel> = itemView.findViewById(R.id.news_banner)
    }

    class BannerViewHolder : MZViewHolder<Carousel> {
        lateinit var myContext: Context


        //这个是用于banner里面的每个界面的viewHolder
        lateinit var title: TextView
        lateinit var image: ImageView
        lateinit var view: View
        override fun onBind(context: Context?, int: Int, carousel: Carousel) {
            if (context != null) {
                view.setTag(carousel.index)
                view.setOnClickListener {
                    val intent = Intent(myContext, WebActivity::class.java)
                    intent.putExtra("bannerUrl", view.getTag() as String)
                    myContext.startActivity(intent)
                }
                title.setTag(carousel.index)
                title.text = carousel.subject
                title.setOnClickListener {
                    val intent = Intent(myContext, WebActivity::class.java)
                    intent.putExtra("bannerUrl", view.getTag() as String)
                    myContext.startActivity(intent)
                }
                val tp = title.getPaint()
                tp.setFakeBoldText(true)
                Glide.with(context).load(carousel.pic).error(R.drawable.ic_news_error_big).centerCrop().into(image)
            }
        }

        override fun createView(context: Context?): View {
            if (context != null) {
                myContext = context
                view = LayoutInflater.from(context).inflate(R.layout.news_banner_item, null, false)
                title = view.findViewById(R.id.news_banner_item_title)
                image = view.findViewById(R.id.news_banner_item_image)
                //content = view.findViewById(R.id.news_content)
            }
            return view
        }
    }
}