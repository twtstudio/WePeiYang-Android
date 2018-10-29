package com.yookiely.lostfond2.release

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
import com.example.lostfond2.R
import com.yookiely.lostfond2.service.PermissionsUtils
import com.yookiely.lostfond2.service.Utils

//上传多图的recyclerview的adapter
class ReleasePicAdapter(val list: ArrayList<Any?>,
                        private val releaseActivity: ReleaseActivity,
                        val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentPosition = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val releasePic: ImageView = view.findViewById(R.id.release_cardview_pic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lf2_waterfall_release_pic_cardview, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder as ViewHolder
        holder.itemView.apply {
            setOnClickListener {
                currentPosition = position

                if (list[position] == null) {
                    PermissionsUtils.requestPermission(releaseActivity, PermissionsUtils.CODE_READ_EXTERNAL_STORAGE, releaseActivity.mPermissionGrant)
                } else {
                    showDialogOfPic()
                }
            }
            setOnLongClickListener {
                currentPosition = position
                releaseActivity.setPicEdit()
                true
            }
        }

        if (list.size > position && list[position] != null) {
            if (list[position] is String) {
                Glide.with(context)
                        .load(Utils.getPicUrl(list[position] as String))
                        .into(holder.releasePic)
            } else if (list[position] is Uri) {
                Glide.with(context)
                        .load(list[position] as Uri)
                        .into(holder.releasePic)
            }
        } else {
            Glide.with(context)
                    .load(Utils.getPicUrl("julao.jpg"))
                    .placeholder(R.drawable.lf_choose_pic)
                    .into(holder.releasePic)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun addPic() {
        list.add(null)
        notifyItemChanged(list.size)
    }

    fun removePic() {
        list.removeAt(currentPosition)
        if (list[list.size - 1] != null) {
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
        val dialog = Dialog(releaseActivity, R.style.edit_AlertDialog_style)
        dialog.setContentView(R.layout.lf2_dialog_detail_pic)
        val imageView = dialog.findViewById<ImageView>(R.id.detail_bigpic)

        if (list[currentPosition] is String) {
            Glide.with(context)
                    .load(Utils.getPicUrl(list[currentPosition] as String))
                    .into(imageView)
        } else {
            Glide.with(context)
                    .load(list[currentPosition])
                    .into(imageView)
        }

        dialog.setCanceledOnTouchOutside(true)
        val window = dialog.window
        val lp = window.attributes
        lp.x = 4
        lp.y = 4
        dialog.onWindowAttributesChanged(lp)
        imageView.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}