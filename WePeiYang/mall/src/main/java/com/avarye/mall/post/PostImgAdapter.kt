package com.avarye.mall.post

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.avarye.mall.R
import com.bumptech.glide.Glide
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import pub.devrel.easypermissions.EasyPermissions

/**
 * 添加一个空的item
 * 判断list是否有图片
 */
object NoSelectPic

/**
 * 上传图片recyclerView的adapter
 */
class PostImgAdapter(private val list: MutableList<Any>,
                     private val activity: PostActivity,
                     private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentPosition = 0
    private val iidList =  mutableListOf<String>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.iv_post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.mall_item_post_img, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder as ViewHolder
        holder.itemView.apply {
            setOnClickListener {
                currentPosition = position
                if (list[position] == NoSelectPic) {
                    checkPermAndOpenPic()
                } else {
                    showBigPic()
                }
            }
            setOnLongClickListener {
                if (list[position] != NoSelectPic) {
                    currentPosition = position
                    setPicEdit()
                }
                true
            }
        }

        val tmp = list[position]
        if (list.size > position && tmp != NoSelectPic) {
            Glide.with(context)
                    .load(tmp)
                    .into(holder.image)

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

    private fun removePic() {
        iidList.removeAt(currentPosition)
        list.removeAt(currentPosition)
        if (list[list.size - 1] != NoSelectPic) {
            addPic()
        }
        notifyItemChanged(currentPosition)
        notifyDataSetChanged()
    }

    fun changePic(pic: Any, iid: String) {
        list[currentPosition] = pic
        iidList[currentPosition] = iid
        notifyItemChanged(currentPosition)
        notifyDataSetChanged()
        if (currentPosition == (list.size - 1) && list.size < 4) {
            addPic()
        }
    }

    private fun setPicEdit() {
        val list = arrayOf<CharSequence>("更改图片", "删除图片", "取消")
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setItems(list) { dialog, item ->
            when (item) {
                0 -> checkPermAndOpenPic()
                1 -> removePic()
                2 -> dialog.dismiss()
            }
        }
        val alert = alertDialogBuilder.create()
        alert.show()
    }

    /**
     * 检查存储权限
     */
    private fun checkPermAndOpenPic() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            EasyPermissions.requestPermissions(activity, "需要外部存储来提供必要的缓存", 0,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            openSelectPic()
        }
    }

    /**
     * 用第三方库打开相册
     */
    @SuppressLint("ResourceType")
    private fun openSelectPic() = Matisse.from(activity)
            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
            .countable(true)
            .maxSelectable(1)
            .gridExpectedSize(activity.resources.getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .theme(R.style.Matisse_Dracula)
            .forResult(2)

    private fun showBigPic() {
        val dialog = Dialog(activity, R.style.edit_AlertDialog_style)
        dialog.apply {
            setContentView(R.layout.mall_dialog_detail_img)
            val imageView = findViewById<ImageView>(R.id.iv_detail_whole)

            when (val tmp = list[currentPosition]) {
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

    fun getIidList(): List<String> {
        return iidList
    }

}