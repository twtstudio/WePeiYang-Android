package com.avarye.mall.post

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.avarye.mall.R
import com.bumptech.glide.Glide

// 用来判断list中是否有图片
object NoSelectPic

class PostImgAdapter(val list: MutableList<Any>, private val activity: PostActivity, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentPosition = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.iv_upload)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.mall_item_upload_img, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder as ViewHolder
        holder.itemView.apply {
            setOnClickListener {
                currentPosition = position
                if (list[position] == NoSelectPic) {

                }
            }
        }
        val tmp = list[position]

        if (list.size > position && tmp != NoSelectPic) {
            when (tmp) {

                is Uri -> Glide.with(context)
                        .load(tmp)
                        .into(holder.image)
            }
        } else {
            Glide.with(context)
                    .load(R.drawable.mall_pic_add)
                    .into(holder.image)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun addPic() {
        list.add(NoSelectPic)
        notifyItemChanged(list.size)
    }

    fun removePic() {
        list.removeAt(currentPosition)
        if (list[list.size - 1] != NoSelectPic) {
            addPic()
        }
        notifyItemChanged(currentPosition)
        notifyDataSetChanged()
    }

    fun changePic(pic: Any) {
        list[currentPosition] = pic
        notifyItemChanged(currentPosition)
        notifyDataSetChanged()
        if (currentPosition == (list.size - 1) && list.size < 5) {
            addPic()
        }
    }

    fun addPicUrl(urlList: List<String>) {
        list.addAll(0, urlList)
        notifyDataSetChanged()
    }

    private fun showDialogOfPic() {
        val dialog = Dialog(activity, R.style.edit_AlertDialog_style)
        dialog.apply {
            setContentView(R.layout.mall_dailog_detail_img)
            val imageView = findViewById<ImageView>(R.id.iv_detail_whole)
            val tmp = list[currentPosition]

            when (tmp) {
//                is String -> Glide.with(context)
//                        .load(Utils.getPicUrl(tmp))
//                        .into(imageView)
                is Uri -> Glide.with(context)
                        .load(tmp)
                        .into(imageView)
                else -> Glide.with(context)
                        .load(R.drawable.mall_ic_null)
                        .into(imageView)
            }

            setCanceledOnTouchOutside(true)
            val window = window
            val lp = window!!.attributes
            lp.x = 4
            lp.y = 4
            dialog.onWindowAttributesChanged(lp)
            imageView.setOnClickListener { dismiss() }
            show()
        }
    }

}