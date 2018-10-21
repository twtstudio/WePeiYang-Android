package com.yookiely.lostfond2.waterfall

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lostfond2.R
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.detail.DetailActivity
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.yookiely.lostfond2.service.Utils

class WaterfallTableAdapter(var waterFallBean: List<MyListDataOrSearchBean>?,
                            val context: Context,
                            var lostOrFound: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class WaterfallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val waterfallItemPic = itemView.findViewById<ImageView>(R.id.lost_pic)!!
        val waterfallItemThing = itemView.findViewById<TextView>(R.id.thing_content)!!
        val waterfallItemData = itemView.findViewById<TextView>(R.id.data_content)!!//data其实是date
        val waterfallItemLocation = itemView.findViewById<TextView>(R.id.location_content)!!
        val waterfallItemTitle = itemView.findViewById<TextView>(R.id.title_content)!!
        val waterfallItemRecapturePlace = itemView.findViewById<TextView>(R.id.recapture_place)!!
        val waterfallItemRecaptureImage = itemView.findViewById<ImageView>(R.id.recapture)!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lf2_item_waterfall, parent, false)

        return WaterfallViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val dataOfItem = waterFallBean!![position]
        val viewHolder = holder as WaterfallViewHolder

        if (dataOfItem.picture == null) {
            Glide.with(context)
                    .load(Utils.getPicUrl("julao.jpg"))
                    .placeholder(R.drawable.lf_detail_np)
                    .into(viewHolder.waterfallItemPic)
        } else {
            val piclist: List<String> = dataOfItem.picture.split(",")
            Glide.with(context)
                    .load(Utils.getPicUrl(piclist[0]))
                    .asBitmap()
                    .placeholder(R.drawable.lf_waterfall_nopic)
                    .into(viewHolder.waterfallItemPic)
        }

        viewHolder.apply {
            waterfallItemTitle.text = dataOfItem.title
            waterfallItemLocation.text = Utils.getDetailFilterOfPlace(Hawk.get("campus")) + "-" + dataOfItem.place
            waterfallItemThing.text = dataOfItem.name
            waterfallItemData.text = dataOfItem.time

            if (lostOrFound == "found") {
                waterfallItemRecaptureImage.visibility = View.VISIBLE
                waterfallItemRecapturePlace.visibility = View.VISIBLE
                waterfallItemRecapturePlace.text = dataOfItem.recapture_place + Utils.getExit(dataOfItem.recapture_entrance)
            } else {
                waterfallItemRecaptureImage.visibility = View.GONE
                waterfallItemRecapturePlace.visibility = View.GONE
            }
        }
        viewHolder.itemView.setOnClickListener { startDetailActivity(dataOfItem.id) }
    }

    override fun getItemCount(): Int = waterFallBean?.size ?: 0

    private fun startDetailActivity(id: Int) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("lostOrFound", lostOrFound)
        val intent = Intent()
        intent.putExtras(bundle)
        intent.setClass(context, DetailActivity::class.java)
        context.startActivity(intent)
    }

}