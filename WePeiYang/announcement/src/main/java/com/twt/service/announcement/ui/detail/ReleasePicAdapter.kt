package com.twt.service.announcement.ui.activity.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.ui.detail.AskQuestionActivity

// 用来判断list中是否有图片
object noSelectPic {

}

// 上传多图的recyclerview的adapter
class ReleasePicAdapter(val list: MutableList<Any>,
                        private val releaseActivity: AskQuestionActivity,
                        val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentPosition = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val releasePic: ImageView = view.findViewById(R.id.anno_release_card_pic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.anno_release_pic_cardview, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder as ViewHolder
        holder.itemView.apply {
            setOnClickListener {
                currentPosition = position

                if (list[position] == noSelectPic) {
                    releaseActivity.checkPermAndOpenPic()
                } else {
                    showDialogOfPic()
                }
            }
            setOnLongClickListener {
                if (list[position] != noSelectPic) {
                    currentPosition = position
                    releaseActivity.setPicEdit()
                }
                true
            }
        }

        val tmp = list[position]

        if (list.size > position && tmp != noSelectPic) {
            when (tmp) {
                is Uri -> Glide.with(context)
                        .load(tmp)
                        .into(holder.releasePic)
            }
        } else {
            Glide.with(context)
                    .load(R.drawable.lf_choose_pic)
                    .into(holder.releasePic)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun addPic() {
        list.add(noSelectPic)
        notifyItemChanged(list.size)
    }

    fun removePic() {
        list.removeAt(currentPosition)
        if (list[list.size - 1] != noSelectPic) {
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



    private fun showDialogOfPic() {
        val dialog = Dialog(releaseActivity, R.style.edit_AlertDialog_style)
        dialog.apply {
            setContentView(R.layout.anno_dialog_detail_pic)
            val imageView = findViewById<ImageView>(R.id.anno_iv_detail_bigpic)
            val tmp = list[currentPosition]

            when (tmp) {
                is Uri -> Glide.with(context)
                        .load(tmp)
                        .into(imageView)
                else -> Glide.with(context)
                        .load(R.drawable.lf_detail_np)
                        .into(imageView)
            }

            setCanceledOnTouchOutside(true)
            val window = window
            val lp = window.attributes
            lp.x = 4
            lp.y = 4
            dialog.onWindowAttributesChanged(lp)
            imageView.setOnClickListener { dismiss() }
            show()
        }
    }



}