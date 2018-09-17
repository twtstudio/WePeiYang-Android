package com.example.lostfond2.release

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.lostfond2.R
import com.example.lostfond2.service.PermissionsUtils
import com.example.lostfond2.service.Utils
import kotlinx.android.synthetic.main.dialog_detail_pic.view.*

class ReleasePicadapter(val list : ArrayList<Any?>,
                        val releaseActivity: ReleaseActivity, val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var currentPosition = 0

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val release_pic = view.findViewById<ImageView>(R.id.detail_bigpic)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_detail_pic, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        val layoutParams = holder.release_pic.layoutParams
        layoutParams.apply {
            width = 80 * getDensity().toInt()
            height = 80 * getDensity().toInt()
        }
        holder.release_pic.layoutParams = layoutParams
        holder.itemView.setOnClickListener {
            currentPosition = position

            if (list[position] == null) {
                PermissionsUtils.requestPermission(releaseActivity, PermissionsUtils.CODE_READ_EXTERNAL_STORAGE, releaseActivity.mPermissionGrant)
//                addPic()
            } else {
                showDialogOfPic()
            }
        }

        holder.itemView.setOnLongClickListener {
            currentPosition = position
            releaseActivity.setPicEdit()
            true
        }

        if (list[position] != null) {
            if (list[position] is String) {
                Glide.with(context)
                        .load(Utils.getPicUrl(list[position] as String))
                        .into(holder.release_pic)
            } else if (list[position] is Uri) {
                Glide.with(context)
                        .load(list[position] as Uri)
                        .into(holder.release_pic)
            }
        } else {
            Glide.with(context)
                    .load(Utils.getPicUrl("julao.jpg"))
                    .placeholder(R.drawable.lf_choose_pic)
                    .into(holder.release_pic)
        }
    }

    override fun getItemCount(): Int = list.size

    fun addPic() {
        list.add(null)
        notifyItemChanged(list.size)
    }

    fun removePic() {
        list.removeAt(currentPosition)
        notifyItemChanged(currentPosition)
        notifyDataSetChanged()
    }

    fun changePic(pic : Any) {
        list[currentPosition] = pic
        notifyItemChanged(currentPosition)
        notifyDataSetChanged()
        if (currentPosition == (list.size - 1)) {
            addPic()
        }
    }

    private fun getDensity() : Float{
        val wm = releaseActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return dm.density
    }

    private fun showDialogOfPic() {
        val dialog = Dialog(releaseActivity, R.style.edit_AlertDialog_style)
        dialog.setContentView(R.layout.dialog_detail_pic)
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