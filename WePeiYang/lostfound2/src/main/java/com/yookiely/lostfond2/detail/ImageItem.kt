package com.yookiely.lostfond2.detail

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.lostfond2.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater
import android.view.LayoutInflater
import com.yookiely.lostfond2.service.Utils


class ImageItem(val url : String, val  context1 : Activity) : Item {

    private companion object Controller : ItemController{
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lf_item_imageitem,parent,false)
            return ImageItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ImageItemViewHolder
            item as ImageItem
            Glide.with(item.context1)
                    .load(Utils.getPicUrl(item.url))
                    .placeholder(R.drawable.lf_detail_np)
                    .error(R.drawable.lf_detail_np)
                    .into(holder.image)
            holder.image.setOnClickListener {
                val inflater = LayoutInflater.from(item.context1)
                val imgEntryView = inflater.inflate(R.layout.lf2_dialog_detail_pic, null)
                // 加载自定义的布局文件
                val dialog = android.support.v7.app.AlertDialog.Builder(item.context1).create()
                val img = imgEntryView.findViewById(R.id.detail_bigpic) as ImageView
                Glide.with(item.context1)
                        .load(Utils.getPicUrl(item.url))
                        .placeholder(R.drawable.lf_detail_np)
                        .error(R.drawable.lf_detail_np)
                        .into(img)

                // 这个是加载网络图片的，可以是自己的图片设置方法
                // imageDownloader.download(imageBmList.get(0),img);
                dialog.setView(imgEntryView) // 自定义dialog
                dialog.show()
                // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
                imgEntryView.setOnClickListener(View.OnClickListener { dialog.cancel() })
            }


        }

    }

    private class  ImageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.image_image)

    }


    override val controller: ItemController
        get() = ImageItem
}

fun MutableList<Item>.setImage(url : String,context1: Activity) = add(ImageItem(url,context1))