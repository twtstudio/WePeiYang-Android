package com.yookiely.lostfond2.waterfall

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.lostfond2.R
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.yookiely.lostfond2.detail.DetailActivity
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.yookiely.lostfond2.service.Utils


class WaterfallTableAdapter(private var waterFallBean: List<MyListDataOrSearchBean>?,
                            val context: Context,
                            var lostOrFound: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class WaterfallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val waterfallItemPic: ImageView = itemView.findViewById(R.id.iv_waterfall_lost_pic)
        val waterfallItemThing: TextView = itemView.findViewById(R.id.tv_waterfall_thing_content)
        val waterfallItemData: TextView = itemView.findViewById(R.id.tv_waterfall_time_content)//data其实是date
        val waterfallItemLocation: TextView = itemView.findViewById(R.id.tv_waterfall_location_content)
        val waterfallItemTitle: TextView = itemView.findViewById(R.id.tv_waterfall_title_content)
        val waterfallItemRecapturePlace: TextView = itemView.findViewById(R.id.recapture_place)
        val waterfallItemRecaptureImage: ImageView = itemView.findViewById(R.id.recapture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lf2_item_waterfall, parent, false)

        return WaterfallViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataOfItem = waterFallBean!![position]
        val viewHolder = holder as WaterfallViewHolder

        Picasso.get().load(Utils.getPicUrl(dataOfItem.picture?.get(0)))
                .placeholder(R.drawable.lf_detail_np)
                .fit()
                .centerInside()
                .transform(getTransformation(viewHolder.waterfallItemPic.width))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .config(Bitmap.Config.RGB_565)
                .into(viewHolder.waterfallItemPic)

        viewHolder.apply {
            waterfallItemTitle.text = dataOfItem.title
            waterfallItemLocation.text = Utils.getDetailFilterOfPlace(Utils.campus) + "-" + dataOfItem.place
            waterfallItemThing.text = Utils.getType(dataOfItem.detail_type)
            waterfallItemData.text = dataOfItem.time

            if (lostOrFound == Utils.STRING_FOUND) {
                waterfallItemRecaptureImage.visibility = View.VISIBLE
                waterfallItemRecapturePlace.visibility = View.VISIBLE
                waterfallItemRecapturePlace.text = if (dataOfItem.recapture_place == "无" || dataOfItem.recapture_place == null) {
                    dataOfItem.recapture_place
                } else {
                    Utils.getGarden(dataOfItem.recapture_place) + dataOfItem.recapture_place + Utils.getExit(dataOfItem.recapture_entrance)
                }
            } else {
                waterfallItemRecaptureImage.visibility = View.GONE
                waterfallItemRecapturePlace.visibility = View.GONE
            }

            itemView.setOnClickListener { startDetailActivity(dataOfItem.id) }
        }
    }

    override fun getItemCount(): Int = waterFallBean?.size ?: 0

    private fun startDetailActivity(id: Int) {
        val bundle = Bundle()
        bundle.putInt(Utils.ID_KEY, id)
        bundle.putString(Utils.LOSTORFOUND_KEY, lostOrFound)
        val intent = Intent()
        intent.putExtras(bundle)
        intent.setClass(context, DetailActivity::class.java)
        context.startActivity(intent)
    }

    fun getTransformation(targetWidth: Int): Transformation {
        return object : Transformation {

            override fun transform(source: Bitmap): Bitmap {
                if (source.width == 0) {
                    return source
                }
                // 按宽高比例缩放图片
                val aspectRatio = source.height.toDouble() / source.width.toDouble()
                val targetHeight = (targetWidth * aspectRatio).toInt()
                if (targetHeight != 0 && targetWidth != 0) {
                    val result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false)
                    if (result !== source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle()
                    }
                    return result
                } else {
                    return source
                }

            }

            override fun key(): String {
                return "transformation" + " desiredWidth"
            }
        }
    }
}
